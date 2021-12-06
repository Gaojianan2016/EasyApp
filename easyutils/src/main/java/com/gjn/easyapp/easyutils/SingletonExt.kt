package com.gjn.easyapp.easyutils

/**
 * 实现单例伴生类
 * */
open class SingletonCompanionImpl<out T, in A>(initializer: (A) -> T) {
    private var initializer: ((A) -> T)? = initializer
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i1 = instance
        if (i1 != null) {
            return i1
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val typedInstance = initializer!!(arg)
                instance = typedInstance
                initializer = null
                typedInstance
            }
        }
    }
}