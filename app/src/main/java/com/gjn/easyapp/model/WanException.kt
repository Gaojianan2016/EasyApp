package com.gjn.easyapp.model

import java.lang.RuntimeException

data class WanException(
    val code: Int,
    override val message: String? = ""
): RuntimeException()