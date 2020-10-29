package com.gjn.easyapp.easyutils

import java.lang.reflect.Field

/**
 * 判断类是否被注解
 * */
fun Any.containAnnotation(clazz: Class<out Annotation>): Boolean =
    javaClass.apply {
        annotations
    }.isAnnotationPresent(clazz)

/**
 * 判断Field是否被注解
 * */
fun Field.containAnnotation(clazz: Class<out Annotation>): Boolean =
    apply {
        annotations
    }.isAnnotationPresent(clazz)

/**
 * 获取注解的对象
 * */
fun Any.getAnnotation(clazz: Class<out Annotation>): Annotation? =
    if (containAnnotation(clazz)) {
        javaClass.getAnnotation(clazz)
    } else {
        null
    }

/**
 * 获取注解的成员变量
 * */
fun Any.getField(clazz: Class<out Annotation>): List<Field> {
    val result = mutableListOf<Field>()
    for (field in javaClass.declaredFields) {
        if (field.containAnnotation(clazz)) {
            result.add(field)
        }
    }
    return result
}