package com.chase.generator.parameters

import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import kotlinx.serialization.Serializable

@Serializable
class ItemSourceFilter(
    val itemSources: Filter<Int>? = null,
    val itemSourceTypes: Filter<ItemSourceType>? = null,
    val itemSourceTags: Filter<ItemSourceTag>? = null,
)