package com.chase.utilities

import com.chase.models.osrs.OsrsClueScrollTier
import com.chase.models.osrs.OsrsSkill
import com.chase.models.sources.ItemSourceType
import com.chase.models.tasks.Task
import com.chase.models.tasks.Task.*
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType

class TaskBuilder : Builder<Task>("Task") {

    override suspend fun newItem(): Task = when (enumParam("type", TaskType::class)) {
        TaskType.ObtainSpecificItemFromSpecificSource -> ObtainSpecificItemFromSpecificSourceTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            expectedHouts = param("expected hours") { toIntOrNull() },
            itemId = param("itemId") { toIntOrNull() },
            itemSourceId = param("itemSourceId") { toIntOrNull() },
        )

        TaskType.ObtainAnyItemFromSpecificSource -> TODO()
        TaskType.ObtainSpecificItem -> TODO()
        TaskType.ObtainItemWithTag -> TODO()
        TaskType.ObtainAnyItemFromSourceType -> TODO()
        TaskType.ObtainAnyItemFromSourceTag -> TODO()

        TaskType.ObtainXpInSkill -> ObtainXpInSkillTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            expectedHouts = param("expected hours") { toIntOrNull() },

            skill = enumParam("skill", OsrsSkill::class),
            amount = param("amount") { toIntOrNull() },
        )

        TaskType.CompleteClueScrolls -> CompleteClueScrollsTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            expectedHouts = param("expected hours") { toIntOrNull() },

            clueType = enumParam("clue type", OsrsClueScrollTier::class),
            amount = param("amount") { toIntOrNull() },
        )

        TaskType.ObtainCollectionLogSlots -> ObtainCollectionLogSlotsTask(
            id = param("id") { toIntOrNull() },
            tier = enumParam("tier", TaskTier::class),
            expectedHouts = param("expected hours") { toIntOrNull() },
            amount = param("amount") { toIntOrNull() },
            itemSourceId = param("itemSourceId") { toIntOrNull() },
            itemSourceType = enumParam("itemSourceType", ItemSourceType::class),
        )

    }

}