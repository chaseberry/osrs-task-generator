package com.chase.models.items

import com.chase.models.Model
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    override val id: Int,
    val name: String,
    val tags: List<ItemTag>,
) : Model