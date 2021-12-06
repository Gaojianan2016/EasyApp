package com.gjn.easyapp.easyutils

import android.net.Uri
import java.io.File

fun String.toUri(): Uri = Uri.parse(this)

fun File.toUri(): Uri = Uri.fromFile(this)

