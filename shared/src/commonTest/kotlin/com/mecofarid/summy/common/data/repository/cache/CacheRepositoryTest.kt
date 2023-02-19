package com.mecofarid.summy.common.data.repository.cache

import com.mecofarid.summy.common.data.DataException
import com.mecofarid.summy.common.data.NetworkException
import com.mecofarid.summy.common.data.Operation
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.datasource.DeleteDatasource
import com.mecofarid.summy.common.data.datasource.GetDatasource
import com.mecofarid.summy.common.data.datasource.PutDatasource
import com.mecofarid.summy.common.either.Either
import com.mecofarid.summy.common.mocks.model.Dummy
import com.mecofarid.summy.common.mocks.model.anyDummy
import com.mecofarid.summy.utils.anyList
import com.mecofarid.summy.utils.randomInt
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CacheRepositoryTest {

    @MockK
    private lateinit var cacheGetDatasource: GetDatasource<List<Dummy>, DataException>

    @MockK
    private lateinit var cachePutDatasource: PutDatasource<List<Dummy>, List<Dummy>, DataException>

    @MockK
    private lateinit var cacheDeleteDatasource: DeleteDatasource<List<Dummy>, DataException>


    @MockK
    private lateinit var mainGetDatasource: GetDatasource<List<Dummy>, DataException>

    @MockK
    private lateinit var mainPutDatasource: PutDatasource<List<Dummy>, List<Dummy>, DataException>

    @MockK
    private lateinit var mainDeleteDatasource: DeleteDatasource<List<Dummy>, DataException>

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - data fetched from main and cached when sync-main is requested`() = runTest {
        val cacheData = anySuccessfulData()
        val mainData = anySuccessfulData()
        val repository = givenRepository()
        val query = anyQuery()
        coEvery { mainGetDatasource.get(any()) } returns mainData
        coEvery { cachePutDatasource.put(any(), mainData.value) } returns cacheData
        coEvery { cacheGetDatasource.get(any()) } returns cacheData

        val actualData = repository.get(query, Operation.MainThenCache)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainGetDatasource.get(query) }
        coVerify(exactly = 1) { cacheGetDatasource.get(query) }
        coVerify(exactly = 1) { cachePutDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - data fetched from cache when sync-main is requested and main data source fails`() = runTest {
        val cacheData = anyData()
        val mainException = listOf(
            NetworkException.HttpException(randomInt()),
            NetworkException.ConnectionException()
        ).random()
        val mainData = Either.Left(mainException)
        val repository = givenRepository()
        val query = anyQuery()
        coEvery { mainGetDatasource.get(any()) } returns mainData
        coEvery { cacheGetDatasource.get(any()) } returns cacheData

        val actualData = repository.get(query, Operation.MainThenCache)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainGetDatasource.get(query) }
        coVerify(exactly = 0) { cachePutDatasource.put(any(), any()) }
        coVerify(exactly = 1) { cacheGetDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - data fetched from cache when cache-else-main-sync is requested and cache data source succeeds`() =
        runTest {
            val successCacheData = anySuccessfulData()
            val cacheData = anyData()
            val repository = givenRepository()
            val query = anyQuery()
            coEvery { cacheGetDatasource.get(any()) } returns successCacheData andThen cacheData

            val actualData = repository.get(query, Operation.CacheThenMain)

            assertEquals(actualData, actualData)
            coVerify(exactly = 1) { cacheGetDatasource.get(query) }
            coVerify(exactly = 0) { cachePutDatasource.put(any(), any()) }
            coVerify(exactly = 0) { mainGetDatasource.get(query) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val cacheDataInitial = anyFailureData()
        val mainData = anySuccessfulData()
        val cacheDataAfterCache = anySuccessfulData()
        val repository = givenRepository()
        val query = anyQuery()
        coEvery { cacheGetDatasource.get(any()) } returns cacheDataInitial
        coEvery { mainGetDatasource.get(any()) } returns mainData
        coEvery { cachePutDatasource.put(any(), mainData.value) } returns cacheDataAfterCache

        val actualData = repository.get(query, Operation.CacheThenMain)

        assertEquals(actualData, actualData)
        coVerify(exactly = 2) { cacheGetDatasource.get(query) }
        coVerify(exactly = 1) { mainGetDatasource.get(query) }
        coVerify(exactly = 1) { cachePutDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - error is returned when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = anyQuery()
        val expectedData = anyFailureData()
        coEvery { cacheGetDatasource.get(any()) } returns expectedData
        coEvery { mainGetDatasource.get(any()) } returns anyFailureData()

        val actualData = repository.get(query, Operation.MainThenCache)

        assertEquals(actualData, expectedData)
        coVerify(exactly = 1) { cacheGetDatasource.get(query) }
        coVerify(exactly = 0) { cachePutDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainGetDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GET - error is returned when both data sources fail when cache-else-main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = anyQuery()
        val expectedData = anyFailureData()
        coEvery { cacheGetDatasource.get(any()) } returns expectedData
        coEvery { mainGetDatasource.get(any()) } returns anyFailureData()

        val actualData = repository.get(query, Operation.CacheThenMain)

        assertEquals(actualData, expectedData)
        coVerify(exactly = 2) { cacheGetDatasource.get(query) }
        coVerify(exactly = 0) { cachePutDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainGetDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `PUT - main data source is called when cache-else-main-sync is requested and cache source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyData()
            coEvery { cachePutDatasource.put(any(), any()) } returns data
            coEvery { mainPutDatasource.put(any(), any()) } returns expectedData

            val actualData = repository.put(query, Operation.CacheThenMain, data.value)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cachePutDatasource.put(query, data.value) }
            coVerify(exactly = 1) { mainPutDatasource.put(query, data.value) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `PUT - main data source is not called when cache-else-main-sync is requested and cache source fails`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyFailureData()
            coEvery { cachePutDatasource.put(any(), any()) } returns expectedData

            val actualData = repository.put(query, Operation.CacheThenMain, data.value)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cachePutDatasource.put(query, data.value) }
            coVerify(exactly = 0) { mainPutDatasource.put(any(), any()) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `PUT - cache data source is called when main-else-cache-sync is requested and main source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyData()
            coEvery { mainPutDatasource.put(any(), any()) } returns data
            coEvery { cachePutDatasource.put(any(), any()) } returns expectedData

            val actualData = repository.put(query, Operation.MainThenCache, data.value)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cachePutDatasource.put(query, data.value) }
            coVerify(exactly = 1) { mainPutDatasource.put(query, data.value) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `PUT - cache data source is not called when main-else-cache-sync is requested and main source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyFailureData()
            coEvery { mainPutDatasource.put(any(), any()) } returns expectedData

            val actualData = repository.put(query, Operation.MainThenCache, data.value)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { mainPutDatasource.put(query, data.value) }
            coVerify(exactly = 0) { cachePutDatasource.put(any(), any()) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `DELETE - main data source is called when cache-else-main-sync is requested and cache source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyData()
            coEvery { cacheDeleteDatasource.delete(any()) } returns data
            coEvery { mainDeleteDatasource.delete(any()) } returns expectedData

            val actualData = repository.delete(query, Operation.CacheThenMain)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cacheDeleteDatasource.delete(query) }
            coVerify(exactly = 1) { mainDeleteDatasource.delete(query) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `DELETE - main data source is not called when cache-else-main-sync is requested and cache source fails`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val expectedData = anyFailureData()
            coEvery { cacheDeleteDatasource.delete(any()) } returns expectedData

            val actualData = repository.delete(query, Operation.CacheThenMain)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cacheDeleteDatasource.delete(query) }
            coVerify(exactly = 0) { mainDeleteDatasource.delete(any()) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `DELETE - cache data source is called when main-else-cache-sync is requested and main source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val data = anySuccessfulData()
            val expectedData = anyData()
            coEvery { mainDeleteDatasource.delete(any()) } returns data
            coEvery { cacheDeleteDatasource.delete(any()) } returns expectedData

            val actualData = repository.delete(query, Operation.MainThenCache)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { cacheDeleteDatasource.delete(query) }
            coVerify(exactly = 1) { mainDeleteDatasource.delete(query) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `DELETE - cache data source is not called when main-else-cache-sync is requested and main source succeeds`() =
        runTest {
            val repository = givenRepository()
            val query = anyQuery()
            val expectedData = anyFailureData()
            coEvery { mainDeleteDatasource.delete(any()) } returns expectedData

            val actualData = repository.delete(query, Operation.MainThenCache)

            assertEquals(actualData, expectedData)
            coVerify(exactly = 1) { mainDeleteDatasource.delete(query) }
            coVerify(exactly = 0) { cacheDeleteDatasource.delete(any()) }
        }

    private fun givenRepository() = CacheRepository(
        cachePutDatasource,
        cacheGetDatasource,
        cacheDeleteDatasource,
        mainPutDatasource,
        mainGetDatasource,
        mainDeleteDatasource
    )

    private fun anyQuery() = object : Query {}

    private fun anyData() =
        listOf(
            anySuccessfulData(),
            anyFailureData()
        ).random()

    private fun anySuccessfulData() = Either.Right(anyList { anyDummy() })

    private fun anyFailureData() = Either.Left(DataException.DataNotFoundException())
}
