package com.gjn.easyapp.network

open class RequestAction<Type> {

    var api: (suspend () -> Type)? = null

    fun api(block: suspend () -> Type) {
        this.api = block
    }
}