package com.chase.models.tasks

import com.chase.models.Model
import com.chase.models.osrs.OsrsClueScrollTier
import com.chase.models.osrs.OsrsSkill
import com.chase.models.sources.ItemSourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Task(
    val type: TaskType,
) : Model {

    abstract val tier: TaskTier
    abstract override val id: Int

    @SerialName("ObtainSpecificItemFromSpecificSource")
    data class ObtainSpecificItemFromSpecificSourceTask(
        override val id: Int,
        override val tier: TaskTier,
        val itemId: Int,
        val itemSourceId: Int,
    ) : Task(TaskType.ObtainSpecificItemFromSpecificSource)

    @SerialName("ObtainAnyItemFromSpecificSource")
    data class ObtainAnyItemFromSpecificSourceTask(
        override val id: Int,
        override val tier: TaskTier,
    ) : Task(TaskType.ObtainAnyItemFromSpecificSource)

    @SerialName("ObtainSpecificItem")
    data class ObtainSpecificItemTask(
        override val id: Int,
        override val tier: TaskTier,
    ) : Task(TaskType.ObtainSpecificItem)

    @SerialName("ObtainItemWithTag")
    data class ObtainItemWithTagTask(
        override val id: Int,
        override val tier: TaskTier,
    ) : Task(TaskType.ObtainItemWithTag)

    @SerialName("ObtainAnyItemFromSourceType")
    data class ObtainAnyItemFromSourceTypeTask(
        override val id: Int,
        override val tier: TaskTier,
    ) : Task(TaskType.ObtainAnyItemFromSourceType)

    @SerialName("ObtainAnyItemFromSourceTag")
    data class ObtainAnyItemFromSourceTagTask(
        override val id: Int,
        override val tier: TaskTier,
    ) : Task(TaskType.ObtainAnyItemFromSourceTag)

    @SerialName("ObtainXpInSkill")
    data class ObtainXpInSkillTask(
        override val id: Int,
        override val tier: TaskTier,
        val skill: OsrsSkill,
        val amount: Int,
    ) : Task(TaskType.ObtainXpInSkill)

    @SerialName("CompleteClueScrolls")
    data class CompleteClueScrollsTask(
        override val id: Int,
        override val tier: TaskTier,
        val clueType: OsrsClueScrollTier,
        val amount: Int,
    ) : Task(TaskType.CompleteClueScrolls)

    @SerialName("ObtainCollectionLogSlots")
    data class ObtainCollectionLogSlotsTask(
        override val id: Int,
        override val tier: TaskTier,
        val amount: Int,
        val itemSourceId: Int?, // from a specific thing
        val itemSourceType: ItemSourceType?, // from a general category
    ) : Task(TaskType.CompleteClueScrolls)

    fun withNewId(newId: Int): Task = when (this) {
        is ObtainSpecificItemFromSpecificSourceTask -> copy(id = newId)
        is ObtainXpInSkillTask -> copy(id = newId)
        is CompleteClueScrollsTask -> copy(id = newId)
        is ObtainCollectionLogSlotsTask -> copy(id = newId)
        is ObtainAnyItemFromSourceTagTask -> copy(id = newId)
        is ObtainAnyItemFromSourceTypeTask -> copy(id = newId)
        is ObtainAnyItemFromSpecificSourceTask -> copy(id = newId)
        is ObtainItemWithTagTask -> copy(id = newId)
        is ObtainSpecificItemTask -> copy(id = newId)
    }
}