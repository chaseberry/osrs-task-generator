package com.chase.utilities

import com.chase.models.osrs.OsrsClueScrollTier
import com.chase.models.osrs.OsrsSkill
import com.chase.models.sources.ItemSourceType
import com.chase.models.tasks.Task
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType

class TaskBuilder : Builder<Task>("Task") {

    override suspend fun newItem(): Task = when (enumParam("type", TaskType::class)) {
        TaskType.ObtainItem -> Task.ObtainItemTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            itemId = param("itemId") { toIntOrNull() },
            itemSourceId = param("itemSourceId") { toIntOrNull() },
        )

        TaskType.ObtainXp -> Task.ObtainXpTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            skill = enumParam("skill", OsrsSkill::class),
            amount = param("amount") { toIntOrNull() },
        )

        TaskType.CompleteClueScrolls -> Task.CompleteClueScrollsTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            clueType = enumParam("clue type", OsrsClueScrollTier::class),
            amount = param("amount") { toIntOrNull() },
        )

        TaskType.ObtainCollectionLogSlots -> Task.ObtainCollectionLogSlotsTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            amount = param("amount") { toIntOrNull() },
            itemSourceId = param("itemSourceId") { toIntOrNull() },
            itemSourceType = enumParam("itemSourceType", ItemSourceType::class),
        )
    }

}