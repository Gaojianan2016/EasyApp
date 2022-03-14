package com.gjn.easyapp.easyutils

/**
 * 实现单例伴生类
 * */
open class SingletonCompanionImpl<out T, in A>(initializer: (A) -> T) {
    private var initializer: ((A) -> T)? = initializer
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val instance1 = instance
        if (instance1 != null) {
            return instance1
        }

        return synchronized(this) {
            val instance2 = instance
            if (instance2 != null) {
                instance2
            } else {
                val typedInstance = initializer!!(arg)
                instance = typedInstance
                initializer = null
                typedInstance
            }
        }
    }
}