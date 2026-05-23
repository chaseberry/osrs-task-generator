package com.chase.generator.parameters

import com.chase.models.items.ItemTag
import kotlinx.serialization.Serializable

@Serializable
class ItemFilter(
    val items: Filter<Int>? = null,
    val tags: Filter<ItemTag>? = null,
)