package com.gjn.easyapp.model

import java.lang.RuntimeException

class GankException(
    val code: Int,
    override val message: String? = ""
): RuntimeException()