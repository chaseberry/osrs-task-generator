package com.chase.points.parameters

import com.chase.generator.parameters.ItemSourceFilter
import com.chase.models.items.ItemTag
import kotlinx.serialization.Serializable

@Serializable
class PointAssignmentParameters(
    val itemSourceFilter: ItemSourceFilter,
    val pointsModifier: Map<ItemTag, Double>? = null,
    val killsForOnePoint: Int? = null,
)