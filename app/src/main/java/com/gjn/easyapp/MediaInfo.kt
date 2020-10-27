package com.gjn.easyapp

import android.graphics.Bitmap

data class MediaInfo(
    var name: String? = null,
    var path: String? = null,
    var parent: String? = null,
    var parentPath: String? = null,
    var mimeType: String? = null,
    var width: Int? = 0,
    var height: Int? = 0,
    var size: Int? = 0,
    var duration: Int? = 0,
    var orientation: Int? = 0,
    var rotation: Int? = 0,
    var resolution: String? = null,
    var thumbnailPath: String? = null,
    var thumbnailBitmap: Bitmap? = null,
    var isVideo: Boolean = false,
    var dirSize: Int? = 0,
    var expand: String? = null
)