package com.chase.models.tasks

import com.chase.models.osrs.OsrsClueScrollTier
import com.chase.models.osrs.OsrsSkill
import com.chase.models.sources.ItemSourceType
import kotlinx.serialization.Serializable

@Serializable
sealed class Task {

    abstract val tier: TaskTier

    class ObtainItemTask(
        override val tier: TaskTier,
        val itemId: Int,
        val itemSourceId: Int,
    ) : Task()

    class ObtainXpTask(
        override val tier: TaskTier,
        val skill: OsrsSkill,
        val amount: Int,
    ) : Task()

    class CompleteClueScrollsTask(
        override val tier: TaskTier,
        val clueType: OsrsClueScrollTier,
        val amount: Int,
    ) : Task()

    class ObtainCollectionLogSlotsTask(
        override val tier: TaskTier,
        val amount: Int,
        val itemSourceId: Int?, // from a specific thing
        val itemSourceType: ItemSourceType?, // from a general category
    ) : Task()
}