package com.chase.utilities

import com.chase.models.tasks.Task
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider

class TaskRenderer(
    val itemProvider: ItemProvider,
    val itemSourceProvider: ItemSourceProvider,
) {
    suspend fun renderTaskAsString(task: Task): String = when(task) {
        is Task.CompleteClueScrollsTask -> "Complete ${task.amount} of ${task.clueType} clue scrolls"
        is Task.ObtainAnyItemFromSourceTagTask -> "Obtain any item from ${task.itemSourceTag}"
        is Task.ObtainAnyItemFromSourceTypeTask -> "Obtain any item from ${task.itemSourceType}"
        is Task.ObtainAnyItemFromSpecificSourceTask -> "Obtain any item from ${itemSource(task.itemSourceId)}"
        is Task.ObtainCollectionLogSlotsTask -> "Obtain ${task.amount} collection log slots from ${TODO()}"
        is Task.ObtainItemWithTagTask -> "Obtain any item from ${task.tag}"
        is Task.ObtainSpecificItemFromSpecificSourceTask -> "Obtain ${item(task.itemId)} from ${itemSource(task.itemSourceId)}"
        is Task.ObtainSpecificItemTask -> "Obtain ${item(task.itemId)}"
        is Task.ObtainXpInSkillTask -> "Obtain ${task.amount} of xp in ${task.skill}"
    }

    private suspend fun item(id: Int) = itemProvider.get(id)?.name ?: "Unknown Item"

    private suspend fun itemSource(id: Int) = itemSourceProvider.get(id)?.name ?: "Unknown Source"
}