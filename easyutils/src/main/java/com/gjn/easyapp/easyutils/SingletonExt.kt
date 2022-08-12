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
        if (instance1 != null) return instance1
        synchronized(this) {
            val instance2 = instance
            if (instance2 != null) return instance2
            val instance3 = initializer!!(arg)
            instance = instance3
            initializer = null
            return instance3
        }
    }
}

/**
 * 实现单例初始化伴生类
 * */
open class SingletonCompanionInitializer<T> {

    @Volatile
    protected var instance: T? = null

    protected fun initialize(block: () -> T): T {
        val instance1 = instance
        if (instance1 != null) return instance1
        synchronized(this) {
            val instance2 = instance
            if (instance2 != null) return instance2
            val instance3 = block.invoke()
            instance = instance3
            return instance3
        }
    }
}