package com.mecofarid.summy.common.data

sealed class Operation {

    // Sync cache data source with main datasource
    object MainThenCache : Operation()

    // Get data from cache, if it fails sync cache datasource with main datasource
    object CacheThenMain : Operation()
}
