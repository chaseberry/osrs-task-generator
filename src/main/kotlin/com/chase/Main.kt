package com.chase

import com.chase.models.sources.ItemDrop
import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.utilities.ItemSourceBuilder
import com.chase.utilities.findEnum

fun main() {

    ItemSourceBuilder().getItems().forEach { println(it) }

}