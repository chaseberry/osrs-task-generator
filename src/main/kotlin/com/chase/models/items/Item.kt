package com.chase.models.items

import kotlinx.serialization.Serializable

@Serializable
class Item(
    val id: Int,
    val name: String,
    val tags: List<ItemTag>,
) {
}