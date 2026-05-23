package com.chase.generator

import com.chase.models.items.ItemTag
import kotlinx.serialization.Serializable

@Serializable
sealed class ItemFilter {

    class WhiteList(
        val ids: List<Int>,
    ): ItemFilter()

    class BlackList(
        val ids: List<Int>,
    ): ItemFilter()

    class HasTag(
        val tags: List<ItemTag>,
    )


}