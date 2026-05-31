package com.chase.utilities

import com.chase.models.items.ItemTag
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.models.tasks.Task
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider

class TaskRenderer(
    val itemProvider: ItemProvider,
    val itemSourceProvider: ItemSourceProvider,
) {
    suspend fun renderTaskAsString(task: Task): String = when (task) {
        is Task.CompleteClueScrollsTask -> "Complete ${task.amount} of ${task.clueType} clue scrolls"
        is Task.ObtainAnyItemFromSourceTagTask -> "Obtain any item from any ${task.itemSourceTag.display()}"
        is Task.ObtainAnyItemFromSourceTypeTask -> "Obtain any item from any ${task.itemSourceType.display()}"
        is Task.ObtainAnyItemFromSpecificSourceTask -> "Obtain any item from ${itemSource(task.itemSourceId)}"
        is Task.ObtainCollectionLogSlotsTask -> "Obtain ${task.amount} collection log slots from ${""}"
        is Task.ObtainItemWithTagTask -> "Obtain any ${task.tag.display()}"
        is Task.ObtainSpecificItemFromSpecificSourceTask -> "Obtain ${item(task.itemId)} from ${itemSource(task.itemSourceId)}"
        is Task.ObtainSpecificItemTask -> "Obtain ${item(task.itemId)}"
        is Task.ObtainXpInSkillTask -> "Obtain ${task.amount} of xp in ${task.skill}"
    }

    private suspend fun item(id: Int) = itemProvider.get(id)?.name ?: "Unknown Item"

    private suspend fun itemSource(id: Int) = when (val s = itemSourceProvider.get(id)) {
        null -> "Unknown Source"
        else -> "${s.name}${if (s.type == ItemSourceType.Monster) "s" else ""}"
    }

    private fun ItemSourceTag.display() = when (this) {
        ItemSourceTag.Slayer -> "Slayer Boss/Monster"
        ItemSourceTag.Wave -> "Wave based Minigame"
        ItemSourceTag.Skilling -> "skilling"
        ItemSourceTag.GodWarsDungeon -> "boss in the God Wars Dungeon"
        ItemSourceTag.Wilderness -> "boss in the Wilderness"
        ItemSourceTag.EarlyGame -> "'early game' boss"
        ItemSourceTag.MidGame -> "'mid game' boss"
        ItemSourceTag.HighLevel -> "'high level' boss"
        ItemSourceTag.DT2 -> "DT2 boss"
        ItemSourceTag.ProfitBoss -> "'profit' boss"
        ItemSourceTag.Barrows -> "the Barrows Brothers"
        ItemSourceTag.Dks -> "the Daggonath Kings"
        ItemSourceTag.MoonsOfPeril -> "the Moons of Peril"
        ItemSourceTag.Draconic -> "Draconic boss"
        ItemSourceTag.SingleCombat -> "boss you fight solo"
        ItemSourceTag.MultiCombat -> "boss you can fight in a team"
    }

    private fun ItemSourceType.display() = when (this) {
        ItemSourceType.Monster -> "monster"
        ItemSourceType.Boss -> "boss"
        ItemSourceType.DemiBoss -> "demiboss"
        ItemSourceType.Raid -> "raid"
        ItemSourceType.Npc -> "npc"
        ItemSourceType.Shop -> "shop"
        ItemSourceType.Object -> "object"
        ItemSourceType.Minigame -> "minigame"
        ItemSourceType.Clue -> "clue"
    }

    private fun ItemTag.display() = when (this) {
        ItemTag.Slayer -> "slayer unique"
        ItemTag.ClueUnique -> "clue scroll unique"
        ItemTag.ClueScroll -> "clue scroll"
        ItemTag.Resource -> "resource"
        ItemTag.Pet -> "pet"
        ItemTag.Tool -> "tool"
        ItemTag.Weapon -> "weapon"
        ItemTag.Armor -> "armor"
        ItemTag.Prayer -> "prayer gear"
        ItemTag.Spec -> "special attack item"
        ItemTag.Tank -> "tank gear"
        ItemTag.Jar -> "jar"
        ItemTag.Cosmetic -> "cosmetic item"
        ItemTag.Barrows -> "Barrows unique"
        ItemTag.Ahrims -> "piece of ${this.name} equipment"
        ItemTag.Karils -> "piece of ${this.name} equipment"
        ItemTag.Dharoks -> "piece of ${this.name} equipment"
        ItemTag.Guthans -> "piece of ${this.name} equipment"
        ItemTag.Torags -> "piece of ${this.name} equipment"
        ItemTag.Veracs -> "piece of ${this.name} equipment"
        ItemTag.DkRings -> "DKs Ring"
        ItemTag.MoonsOfPeril -> "Moons of Peril unique"
        ItemTag.BloodMoon -> "piece of ${this.name} equipment"
        ItemTag.BlueMoon -> "piece of ${this.name} equipment"
        ItemTag.EclipseMoon -> "piece of ${this.name} equipment"
        ItemTag.WildernessShard -> "Odium or Malediction Shard"
        ItemTag.WildernessRing -> "wilderness ring"
        ItemTag.WildernessWeapon -> "wilderness weapon"
        ItemTag.WildernessWeaponUpgrade -> "wilderness weapon upgrade"
        ItemTag.VoidwakerComponent -> "Voidwaker component"
        ItemTag.DragonItem -> "Dragon item"
        ItemTag.Guthix -> "${this.name} aligned item"
        ItemTag.Saradomin -> "${this.name} aligned item"
        ItemTag.Zamorak -> "${this.name} aligned item"
        ItemTag.Armadyl -> "${this.name} aligned item"
        ItemTag.Bandos -> "${this.name} aligned item"
        ItemTag.Zaros -> "${this.name} aligned item"
        ItemTag.Seren -> "${this.name} aligned item"
        ItemTag.Tukeken -> "${this.name} aligned item"
        ItemTag.Elidinis -> "${this.name} aligned item"
        ItemTag.Component -> "upgrade component"
        ItemTag.Upgrade -> "permanent account progress unlock"
        ItemTag.Range -> "ranged armor or weapon"
        ItemTag.Melee -> "melee armor or weapon"
        ItemTag.Magic -> "magic armor or weapon"
        ItemTag.Dt2Quartz -> "DT2 Quartz"
        ItemTag.Dt2Ring -> "DT2 Ring Vestige"
        ItemTag.SraPiece -> "Soul Reaper Axe Component"
        ItemTag.ArmadylArmor -> "piece of Armadyl Armor"
        ItemTag.BandosArmor -> "piece of Bandos Armor"
        ItemTag.TorvaArmor -> "piece of Torva Armor"
        ItemTag.InquisitorsItem -> "piece of the Inquisitors set"
        ItemTag.OathplateArmor -> "piece of the Oathplate set"
        ItemTag.VirtusArmor -> "piece of Virtus robes"
        ItemTag.SunfireArmor -> "piece of Sunfire armor"
        ItemTag.AncestralArmor -> "piece of Ancestral robes"
        ItemTag.MasoriArmor -> "piece of masori armor"
        ItemTag.JusticiarArmor -> "piece of justiciar armor"
        ItemTag.ChambersUnique -> "Chambers of Xeric unique"
        ItemTag.TheatreUnique -> "Theatre of Blood unique"
        ItemTag.TombsUnique -> "Tombs of Amascut unique"
        ItemTag.SlayerHelmRecolor -> "item used to recolor a slayer helm"
        ItemTag.MegaRare -> "Megerare Weapon"
    }
}