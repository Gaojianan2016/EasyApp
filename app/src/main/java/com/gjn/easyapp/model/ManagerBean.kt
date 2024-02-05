package com.gjn.easyapp.model

data class ManagerBean(
    val name : String,
    val level: Int,
    val permissions: List<String>,
)
