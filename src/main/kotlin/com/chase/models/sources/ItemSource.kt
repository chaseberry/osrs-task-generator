package com.chase.models.sources

import kotlinx.serialization.Serializable

@Serializable
class ItemSource(
    val id: Int,
    val name: String,
    val category: ItemSourceCategory,
    val drops: List<ItemDrop>,
    val rollsPerHour: Int,
    val tags: List<ItemSourceTag>,
) {
}