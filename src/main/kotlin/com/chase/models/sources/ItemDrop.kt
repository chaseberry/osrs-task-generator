package com.chase.models.sources

import com.chase.models.items.Item
import kotlinx.serialization.Serializable

@Serializable
data class ItemDrop(
    val itemId: Int,
    val dropRate: Int, // 1 / $dropRate
) {

    fun item(): Item = TODO()

}