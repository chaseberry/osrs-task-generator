package com.chase.models.sources

import kotlinx.serialization.Serializable

@Serializable
data class ItemDrop(
    val itemId: Int,
    val dropRate: Int, // 1 / $dropRate
)