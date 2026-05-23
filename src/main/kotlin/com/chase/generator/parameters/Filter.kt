package com.chase.generator.parameters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Filter<T : Any> {

    @Serializable
    @SerialName("WhiteList")
    class WhiteList<T : Any>(
        val values: List<T>,
    ) : Filter<T>()

    @Serializable
    @SerialName("BlackList")
    class BlackList<T : Any>(
        val values: List<T>,
    ) : Filter<T>()

    fun whiteListValues(): List<T>? = (this as? WhiteList<T>)?.values
    fun blackListValues(): List<T>? = (this as? BlackList<T>)?.values

    fun valid(value: T) =
        (this as? WhiteList<T>)?.values?.contains(value) ?: !(this as BlackList<T>).values.contains(value)

    fun valid(values: List<T>) =
        (this as? WhiteList<T>)?.values?.any { it in values } ?: (this as BlackList<T>).values.none { it in values }
}