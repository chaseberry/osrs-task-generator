package com.chase.utilities

import com.chase.models.sources.ItemSourceType
import com.chase.points.AssignedPoints
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider

class PointRenderer(
    val itemProvider: ItemProvider,
    val itemSourceProvider: ItemSourceProvider,
) {

    suspend fun renderPointsAsString(points: AssignedPoints): String =
        """${itemSource(points.itemSourceId)}${points.killsForOnePoint?.let { "\n  > $it kc: 1pt" }}
${points.points.map { "> ${item(it.itemId)}: ${it.points.points()}" }.joinToString("\n  ", prefix = "  ")}
"""

    private suspend fun item(id: Int) = itemProvider.get(id)?.name ?: "Unknown Item"

    private suspend fun itemSource(id: Int) = when (val s = itemSourceProvider.get(id)) {
        null -> "Unknown Source"
        else -> "${s.name}${if (s.type == ItemSourceType.Monster) "s" else ""}"
    }

    private fun Int.points() = if (this == 1) "1pt" else "${this}pts"

}