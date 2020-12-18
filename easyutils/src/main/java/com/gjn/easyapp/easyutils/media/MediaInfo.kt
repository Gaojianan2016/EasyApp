package com.gjn.easyapp.easyutils.media

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaInfo(
    var name: String,
    var path: String = "",
    var parent: String = "",
    var parentPath: String = "",
    var mimeType: String = "",
    var width: Int = 0,
    var height: Int = 0,
    var size: Int = 0,
    var addTime: Int = 0,
    var duration: Int = 0,
    var orientation: Int = 0,
    var rotation: Int = 0,
    var resolution: String? = null,
    var thumbnailPath: String? = null,
    var thumbnailBitmap: Bitmap? = null,
    var isVideo: Boolean = false,
    var dirSize: Int = 0,
    var expand: String? = null
): Parcelable