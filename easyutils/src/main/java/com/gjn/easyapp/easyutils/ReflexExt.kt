package com.gjn.easyapp.easyutils

import org.jetbrains.annotations.TestOnly
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * 完整类名转Class对象 例如：com.gjn.easyapp.easyutils.ReflexExt -> ReflexExt.Class
 * */
fun <T> String.toClazz(): Class<T>? =
    try {
        toClass() as Class<T>
    } catch (e: ClassNotFoundException) {
        null
    }

/**
 * 完整类名转Class com.gjn.easyapp.easyutils.ReflexExt -> Class
 * */
fun String.toClass(initialize: Boolean? = null, loader: ClassLoader? = null): Class<*> {
    if (initialize == null || loader == null) {
        return Class.forName(this)
    }
    return Class.forName(this, initialize, loader)
}

/**
 * 创建一个无参对象
 * */
fun <T> String.newInstanceClazz(
    parameterTypes: Array<Class<*>> = arrayOf(),
    vararg initArgs: Any?
): T? = toClazz<T>()?.newInstanceClazz(parameterTypes, initArgs)

/**
 * 创建一个无参对象
 * */
fun <T> Class<T>.newInstanceClazz(
    parameterTypes: Array<Class<*>> = arrayOf(),
    vararg initArgs: Any?
): T? {
    if (!isStaticPublic()) return null
    return try {
        getConstructor(*parameterTypes).newInstance(initArgs)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 获取当前类的参数
 * */
fun Any.getDeclaredFields(): Array<Field> = javaClass.declaredFields

/**
 * 获取当前类的方法
 * */
fun Any.getDeclaredMethods(): Array<Method> = javaClass.declaredMethods

/**
 * 执行方法
 * */
fun Any.invokeMethod(
    methodName: String,
    className: String? = null,
    parameterTypes: Array<Class<*>?>? = null,
    vararg args: Any?
): Any? =
    try {
        val clazz = if (className.isNullOrEmpty()) javaClass else className.toClass()
        val method = if (parameterTypes == null) {
            clazz.getMethod(methodName)
        } else {
            clazz.getMethod(methodName, *parameterTypes)
        }
        if (!method.modifiers.isPublic()) {
            method.isAccessible = true
        }
        method.invoke(this, *args)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 执行声明方法
 * */
fun Any.invokeDeclaredMethod(
    methodName: String,
    className: String? = null,
    parameterTypes: Array<Class<*>?>? = null,
    vararg args: Any?
): Any? =
    try {
        val clazz = if (className.isNullOrEmpty()) javaClass else className.toClass()
        val method = if (parameterTypes == null) {
            clazz.getDeclaredMethod(methodName)
        } else {
            clazz.getDeclaredMethod(methodName, *parameterTypes)
        }
        if (!method.modifiers.isPublic()) {
            method.isAccessible = true
        }
        method.invoke(this, *args)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 设置成员参数
 * */
fun Any.setField(fieldName: String, value: Any?) {
    try {
        val field = javaClass.getField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.set(this, value)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 获取成员对象
 * */
fun Any.getField(fieldName: String): Any? =
    try {
        val field = javaClass.getField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.get(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 设置声明类成员参数
 * */
fun Any.setDeclaredField(fieldName: String, value: Any?) {
    try {
        val field = javaClass.getDeclaredField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.set(this, value)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 获取声明类成员对象
 * */
fun Any.getDeclaredField(fieldName: String): Any? =
    try {
        val field = javaClass.getDeclaredField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.get(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 判断类是否是静态公共类
 * */
fun Class<*>.isStaticPublic(): Boolean {
    return if (javaClass.name.contains("$")) {
        javaClass.modifiers.isStaticPublic()
    } else {
        javaClass.modifiers.isPublic()
    }
}

/**
 * 判断Modifier对象是否是公开的
 * */
fun Int.isPublic(): Boolean = Modifier.toString(this).contains("public")

/**
 * 判断Modifier对象是否是静态公开的
 * */
fun Int.isStaticPublic(): Boolean = Modifier.toString(this).contains("public static")

@TestOnly
fun Any.printInfo() {
    val clazz = javaClass
    println("--------基础属性--------")
    println("name ${clazz.name}")
    println("simpleName ${clazz.simpleName}")
    println("canonicalName ${clazz.canonicalName}")
    println("modifiers ${Modifier.toString(clazz.modifiers)}")
    println("superclass ${clazz.superclass}")
    println("--------接口属性--------")
    for (item in clazz.interfaces) {
        println("-> $item")
    }
    println("--------注解(继承)--------")
    for (item in clazz.annotations) {
        println("--> $item")
    }
    println("--------注解(当前)--------")
    for (item in clazz.declaredAnnotations) {
        println("--> $item")
    }
    println("--------构造函数(继承)--------")
    for (item in clazz.constructors) {
        println("---> $item")
    }
    println("--------构造函数(当前)--------")
    for (item in clazz.declaredConstructors) {
        println("---> $item")
    }
    println("--------方法(继承)--------")
    for (item in clazz.methods) {
        println("----> $item")
    }
    println("--------方法(当前)--------")
    for (item in clazz.declaredMethods) {
        println("----> $item")
    }
    println("--------参数(继承)--------")
    for (item in clazz.fields) {
        println("-----> $item")
    }
    println("--------参数(当前)--------")
    for (item in clazz.declaredFields) {
        println("-----> $item")
    }
}