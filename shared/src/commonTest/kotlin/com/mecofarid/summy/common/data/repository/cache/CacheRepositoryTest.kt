package com.mecofarid.summy.common.data.repository.cache

import com.mecofarid.summy.common.data.DataException
import com.mecofarid.summy.common.data.NetworkException
import com.mecofarid.summy.common.data.Operation
import com.mecofarid.summy.common.data.Query
import com.mecofarid.summy.common.data.datasource.Datasource
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
    private lateinit var cacheDatasource: Datasource<List<Dummy>, DataException>

    @MockK
    private lateinit var mainDatasource: Datasource<List<Dummy>, DataException>

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() = runTest {
        val cacheData = Either.Right(anyList { anyDummy() })
        val mainData = Either.Right(anyList { anyDummy() })
        val repository = givenRepository()
        val query = anyQuery
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.put(query, mainData.value) } returns cacheData
        coEvery { cacheDatasource.get(query) } returns cacheData

        val actualData  = repository.get(query, Operation.MainThenCache)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when sync-main is requested and main data source fails`() = runTest {
        val cacheData =
            listOf(
                Either.Right(anyList { anyDummy() }),
                Either.Left(DataException.DataNotFoundException())
            ).random()
        val mainException = listOf(
            NetworkException.HttpException(randomInt()),
            NetworkException.ConnectionException()
        ).random()
        val mainData = Either.Left(mainException)
        val repository = givenRepository()
        val query = anyQuery
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.get(query) } returns cacheData

        val actualData = repository.get(query, Operation.MainThenCache)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when cache-else-main-sync is requested`() = runTest {
        val successCacheData = Either.Right(anyList { anyDummy() })
        val cacheData =
            listOf(
                Either.Right(anyList { anyDummy() }),
                Either.Left(DataException.DataNotFoundException())
            ).random()
        val repository = givenRepository()
        val query = anyQuery
        coEvery { cacheDatasource.get(query) } returns successCacheData andThen cacheData

        val actualData = repository.get(query, Operation.CacheThenMain)

        assertEquals(actualData, actualData)
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 0) { mainDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val cacheDataInitial = Either.Left(DataException.DataNotFoundException())
        val mainData = Either.Right(anyList { anyDummy() })
        val cacheDataAfterCache = Either.Right(anyList { anyDummy() })
        val repository = givenRepository()
        val query = anyQuery
        coEvery { cacheDatasource.get(query) } returns cacheDataInitial
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.put(query, mainData.value) } returns cacheDataAfterCache

        val actualData  = repository.get(query, Operation.CacheThenMain)

        assertEquals(actualData, actualData)
        coVerify(exactly = 2) { cacheDatasource.get(query) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = anyQuery
        val expectedData = Either.Left(DataException.DataNotFoundException())
        coEvery { cacheDatasource.get(query) } returns expectedData
        coEvery { mainDatasource.get(query) } returns Either.Left(DataException.DataNotFoundException())

        val actualData  = repository.get(query, Operation.MainThenCache)

        assertEquals(actualData, expectedData)
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when cache-else-main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = anyQuery
        val expectedData = Either.Left(DataException.DataNotFoundException())
        coEvery { cacheDatasource.get(query) } returns expectedData
        coEvery { mainDatasource.get(query) } returns Either.Left(DataException.DataNotFoundException())

        val actualData  = repository.get(query, Operation.CacheThenMain)

        assertEquals(actualData, expectedData)
        coVerify(exactly = 2) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
    }

    private fun givenRepository() = CacheRepository(
        cacheDatasource,
        mainDatasource
    )

    private val anyQuery = object : Query{}
}
