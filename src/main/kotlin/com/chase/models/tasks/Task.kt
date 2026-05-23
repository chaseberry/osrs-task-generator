package com.chase.models.tasks

import com.chase.models.Model
import com.chase.models.osrs.OsrsClueScrollTier
import com.chase.models.osrs.OsrsSkill
import com.chase.models.sources.ItemSourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Task : Model {

    abstract val tier: TaskTier
    abstract override val id: Int

    @SerialName("ObtainItem")
    data class ObtainItemTask(
        override val id: Int,
        override val tier: TaskTier,
        val itemId: Int,
        val itemSourceId: Int,
    ) : Task()

    @SerialName("ObtainXp")
    data class ObtainXpTask(
        override val id: Int,
        override val tier: TaskTier,
        val skill: OsrsSkill,
        val amount: Int,
    ) : Task()

    @SerialName("CompleteClueScrolls")
    data class CompleteClueScrollsTask(
        override val id: Int,
        override val tier: TaskTier,
        val clueType: OsrsClueScrollTier,
        val amount: Int,
    ) : Task()

    @SerialName("ObtainCollectionLogSlots")
    data class ObtainCollectionLogSlotsTask(
        override val id: Int,
        override val tier: TaskTier,
        val amount: Int,
        val itemSourceId: Int?, // from a specific thing
        val itemSourceType: ItemSourceType?, // from a general category
    ) : Task()

    fun withNewId(newId: Int): Task = when (this) {
        is ObtainItemTask -> copy(id = newId)
        is ObtainXpTask -> copy(id = newId)
        is CompleteClueScrollsTask -> copy(id = newId)
        is ObtainCollectionLogSlotsTask -> copy(id = newId)
    }
}