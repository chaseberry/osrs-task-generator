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

}