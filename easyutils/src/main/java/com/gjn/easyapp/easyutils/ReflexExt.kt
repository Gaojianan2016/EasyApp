package com.gjn.easyapp.easyutils

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * 完整类名转Class对象 例如：com.gjn.easyapp.easyutils.ReflexExt -> ReflexExt.Class
 * */
fun <T> String.toClass(): Class<T>? =
    try {
        Class.forName(this) as Class<T>
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
        null
    }

/**
 * 创建一个无参对象
 * */
@JvmOverloads
fun <T> String.newInstanceClazz(
    parameterTypes: Array<Class<*>> = arrayOf(),
    initArgs: Array<Any> = arrayOf()
): T? = toClass<T>()?.newInstanceClazz(parameterTypes, initArgs)

/**
 * 创建一个无参对象
 * */
@JvmOverloads
fun <T> Class<T>.newInstanceClazz(
    parameterTypes: Array<Class<*>> = arrayOf(),
    initArgs: Array<Any> = arrayOf()
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
 * 执行当前类方法
 * */
@JvmOverloads
fun Any.invokeDeclaredMethod(
    methodName: String,
    parameterTypes: Array<Class<*>> = arrayOf(),
    args: Array<Any> = arrayOf()
): Any? = try {
    val method = javaClass.getDeclaredMethod(methodName, *parameterTypes)
    if (!method.modifiers.isPublic()) {
        method.isAccessible = true
    }
    method.invoke(this, args)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * 设置当前类父类成员参数
 * */
fun Any.setField(fieldName: String, any: Any?) {
    try {
        val field = javaClass.getField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.set(this, any)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 设置当前类成员参数
 * */
fun Any.setDeclaredField(fieldName: String, any: Any?) {
    try {
        val field = javaClass.getDeclaredField(fieldName)
        if (!field.modifiers.isPublic()) {
            field.isAccessible = true
        }
        field.set(this, any)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 判断类是否是可以操作的
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

fun Class<*>.printInfo() {
    println("--------基础属性--------")
    println("name $name")
    println("simpleName $simpleName")
    println("canonicalName $canonicalName")
    println("modifiers ${Modifier.toString(modifiers)}")
    println("superclass $superclass")
    println("--------接口属性--------")
    for (item in interfaces) {
        println("-> $item")
    }
    println("--------注解(继承)--------")
    for (item in annotations) {
        println("--> $item")
    }
    println("--------注解(当前)--------")
    for (item in declaredAnnotations) {
        println("--> $item")
    }
    println("--------构造函数(继承)--------")
    for (item in constructors) {
        println("---> $item")
    }
    println("--------构造函数(当前)--------")
    for (item in declaredConstructors) {
        println("---> $item")
    }
    println("--------方法(继承)--------")
    for (item in methods) {
        println("----> $item")
    }
    println("--------方法(当前)--------")
    for (item in declaredMethods) {
        println("----> $item")
    }
    println("--------参数(继承)--------")
    for (item in fields) {
        println("-----> $item")
    }
    println("--------参数(当前)--------")
    for (item in declaredFields) {
        println("-----> $item")
    }
}