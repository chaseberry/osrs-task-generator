package com.chase.generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GenerationFilter<T : Any> {

    @Serializable
    @SerialName("WhiteList")
    class WhiteList<T : Any>(
        val values: List<T>,
    ) : GenerationFilter<T>()

    @Serializable
    @SerialName("BlackList")
    class BlackList<T : Any>(
        val values: List<T>,
    ) : GenerationFilter<T>()

}