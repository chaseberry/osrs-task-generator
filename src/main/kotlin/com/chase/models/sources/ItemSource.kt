package com.chase.models.sources

import com.chase.models.Model
import kotlinx.serialization.Serializable

@Serializable
data class ItemSource( // monster, npc giving, object, scenary?
    override val id: Int,
    val name: String,
    val type: ItemSourceType, // are type/ids unique or shared
    val drops: List<ItemDrop>,
    val rollsPerHour: Int,
    val tags: List<ItemSourceTag>,
): Model {
}