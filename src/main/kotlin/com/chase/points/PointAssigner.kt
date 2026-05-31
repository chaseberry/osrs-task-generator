package com.chase.points

import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.points.parameters.PointAssignmentParameters
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import kotlinx.coroutines.flow.toList

class PointAssigner(
    val parameters: PointAssignmentParameters,
    val itemSourceProvider: ItemSourceProvider,
    val itemProvider: ItemProvider,

    ) {

    suspend fun calculatePoints() {
        val sources = itemSourceProvider.query(
            only = parameters.itemSourceFilter.itemSources?.whiteListValues(),
            except = parameters.itemSourceFilter.itemSources?.blackListValues(),
            types = ItemSourceType.entries.filter {
                parameters.itemSourceFilter.itemSourceTypes?.valid(it) ?: true
            },
            tags = ItemSourceTag.entries.filter {
                parameters.itemSourceFilter.itemSourceTags?.valid(it) ?: true
            },
        ).toList()

        if (sources.isEmpty() || sources.all { it.drops.isEmpty() }) {
            return
        }

        val base = sources.flatMap { it.drops }.minBy { it.dropRate }.dropRate.toByte()

        val points = sources.flatMap {
            it.drops.map {
                val typeMod = itemProvider.get(it.itemId)?.tags?.mapNotNull { parameters.pointsModifier?.get(it) }?.minOrNull()

                val itemMod = typeMod ?: 1.0
                (itemProvider.get(it.itemId)?.name) to (itemMod * (it.dropRate / base))
            }
        }

        points.forEach {
            println(it)
        }
    }

}