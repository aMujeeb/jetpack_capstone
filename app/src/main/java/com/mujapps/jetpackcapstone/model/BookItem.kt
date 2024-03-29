package com.mujapps.jetpackcapstone.model

data class BookItem(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo?,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)