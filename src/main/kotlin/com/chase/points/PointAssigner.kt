package com.chase.points

import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.points.parameters.PointAssignmentParameters
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.utilities.combineDropRates
import kotlinx.coroutines.flow.toList
import kotlin.math.roundToInt

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

        val base = sources.flatMap { it.drops }.minBy { it.dropRate }.dropRate.toDouble()

        val mod = parameters.killsForOnePoint?.let { base / it } ?: 1.0

        val points = sources.flatMap { src ->
            val combined = parameters.combineTags?.let { tags ->
                tags.associateWith {
                    src.drops.filter {
                        itemProvider.get(it.itemId)?.tags?.any { it in tags } ?: false
                    }
                }
            }?.filter { it.value.isNotEmpty() }?.mapValues {
                val m = parameters.pointsModifier?.get(it.key) ?: 1.0
                (m * (combineDropRates(it.value) / base)).round()
            }

            src.drops.mapNotNull {
                itemProvider.get(it.itemId)?.let { item ->
                    val typeMod = item.tags.mapNotNull { parameters.pointsModifier?.get(it) }.minOrNull()

                    val itemMod = mod * (typeMod ?: 1.0)

                    (item.name) to (item.tags.firstNotNullOfOrNull {
                        combined?.get(it)
                    } ?: (itemMod * (it.dropRate / base)).round())
                }
            }
        }

        points.forEach {
            println(it)
        }
    }

    private fun Double.round(): Int = ((this * 10.0).roundToInt() / 10)

}