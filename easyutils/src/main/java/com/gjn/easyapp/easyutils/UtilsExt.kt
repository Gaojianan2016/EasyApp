package com.gjn.easyapp.easyutils

import android.net.Uri

fun String.uri(): Uri = Uri.parse(this)