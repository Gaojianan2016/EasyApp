package com.gjn.easyapp.easyutils

import android.net.Uri
import android.provider.MediaStore
import java.io.File

inline val EXTERNAL_MEDIA_IMAGE_URI: Uri
    get() = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

inline val EXTERNAL_MEDIA_VIDEO_URI: Uri
    get() = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

inline val EXTERNAL_MEDIA_AUDIO_URI: Uri
    get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

fun String.toUri(): Uri = Uri.parse(this)