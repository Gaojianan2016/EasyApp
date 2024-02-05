package com.gjn.easyapp.easyutils

/**
 * 映射接口 实现快速映射
 * */
interface Mapper<I, O> {
    fun map(input: I): O
}

/**
 * 非空列表映射接口 实现快速映射
 * */
interface ListMapper<I, O> : Mapper<List<I>, List<O>>

class ListMapperImpl<I, O>(private val mapper: Mapper<I, O>) : ListMapper<I, O> {
    override fun map(input: List<I>) = input.map { mapper.map(it) }
}

/**
 * 可空列表映射接口 实现快速映射
 * */
interface NullableListMapper<I, O> : Mapper<List<I>?, List<O>?>

class NullableListMapperImpl<I, O>(private val mapper: Mapper<I, O>) : NullableListMapper<I, O> {
    override fun map(input: List<I>?) =
        if (input.isNullOrEmpty()) null else input.map { mapper.map(it) }

    fun mapNotNull(input: List<I>?) = map(input).orEmpty()
}