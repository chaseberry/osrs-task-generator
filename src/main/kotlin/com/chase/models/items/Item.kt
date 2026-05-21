package com.chase.models.items

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val name: String,
    val tags: List<ItemTag>,
) {
}