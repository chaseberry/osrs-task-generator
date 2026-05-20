package com.chase.models.sources

import kotlinx.serialization.Serializable

@Serializable
class ItemSource( // monster, npc giving, object, scenary?
    val id: Int,
    val name: String,
    val type: ItemSourceType, // are type/ids unique or shared
    val drops: List<ItemDrop>,
    val rollsPerHour: Int,
    val tags: List<ItemSourceTag>,
) {
}