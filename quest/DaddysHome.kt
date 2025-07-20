package com.recursive.quester.quest

import com.recursive.quester.Bot
import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.VarbitRequirement
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.reward.ExperienceReward
import com.recursive.quester.framework.reward.ItemReward
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.ConditionalStep
import com.recursive.quester.framework.steps.NpcStep
import com.recursive.quester.framework.steps.ObjectStep
import com.recursive.quester.framework.steps.QuestStep
import com.recursive.quester.framework.util.getLogger
import com.recursive.quester.runelite.ItemCollections
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.osrs.data.ItemID
import com.runemate.game.api.osrs.data.NpcID
import com.runemate.game.api.osrs.data.NullObjectID
import com.runemate.game.api.osrs.data.ObjectID

class DaddysHome(private val bot: Bot) : BasicQuestHelper() {
    private val log = getLogger("DaddysHome")

    // Items Required
    lateinit var plank10: ItemRequirement
    lateinit var nails20: ItemRequirement
    lateinit var bolt5: ItemRequirement
    lateinit var hammer: ItemRequirement
    lateinit var saw: ItemRequirement
    lateinit var waxwoodLog3: ItemRequirement
    lateinit var waxwoodPlank3: ItemRequirement
    lateinit var bolt2: ItemRequirement
    lateinit var bolt3: ItemRequirement
    lateinit var nails2: ItemRequirement
    lateinit var nails4: ItemRequirement
    lateinit var plank: ItemRequirement
    lateinit var plank3: ItemRequirement
    lateinit var plank2: ItemRequirement

    // Items Recommended
    lateinit var lumberyardTeleport: ItemRequirement
    lateinit var varrockTeleport3: ItemRequirement

    // Requirements
    lateinit var removedChair: VarbitRequirement
    lateinit var removedTable: VarbitRequirement
    lateinit var removedTable2: VarbitRequirement
    lateinit var removedStool: VarbitRequirement
    lateinit var removedStool2: VarbitRequirement
    lateinit var removedCampbed: VarbitRequirement
    lateinit var removedCarpet: VarbitRequirement
    lateinit var repairedCampbed: VarbitRequirement
    lateinit var repairedCarpet: VarbitRequirement
    lateinit var repairedStool: VarbitRequirement
    lateinit var repairedTable: VarbitRequirement
    lateinit var repairedChair: VarbitRequirement
    lateinit var repairedStool2: VarbitRequirement
    lateinit var repairedTable2: VarbitRequirement

    // NPC Steps
    lateinit var talkToMarlo: NpcStep
    lateinit var talkToYarlo: NpcStep
    lateinit var talkToYarloAgain: NpcStep
    lateinit var talkToOperator: NpcStep
    lateinit var talkToYarloOnceMore: NpcStep
    lateinit var talkToMarloToFinish: NpcStep

    // Object Steps
    lateinit var removeChair: ObjectStep
    lateinit var removeCarpet: ObjectStep
    lateinit var removeStool: ObjectStep
    lateinit var removeStool2: ObjectStep
    lateinit var removeTable: ObjectStep
    lateinit var removeTable2: ObjectStep
    lateinit var removeCampbed: ObjectStep
    lateinit var searchCrate: ObjectStep
    lateinit var buildChair: ObjectStep
    lateinit var buildCarpet: ObjectStep
    lateinit var buildStool: ObjectStep
    lateinit var buildStool2: ObjectStep
    lateinit var buildTable: ObjectStep
    lateinit var buildTable2: ObjectStep
    lateinit var buildCampbed: ObjectStep

    override fun setupRequirements() {
        plank10 = ItemRequirement("Plank", ItemID.PLANK, 10)
        bolt5 = ItemRequirement("Bolt of cloth", ItemID.BOLT_OF_CLOTH, 5)
        nails20 = ItemRequirement("Nails (bring more in case you fail with some)", ItemCollections.NAILS, 14)
        hammer = ItemRequirement("Hammer", ItemCollections.HAMMER)
        saw = ItemRequirement("Saw", ItemCollections.SAW)
        waxwoodLog3 = ItemRequirement("Waxwood log", ItemID.WAXWOOD_LOG, 3)
        waxwoodPlank3 = ItemRequirement("Waxwood plank", ItemID.WAXWOOD_PLANK, 3)

        bolt2 = ItemRequirement("Bolt of cloth", ItemID.BOLT_OF_CLOTH, 2)
        bolt3 = ItemRequirement("Bolt of cloth", ItemID.BOLT_OF_CLOTH, 3)
        nails2 = ItemRequirement("Nails", ItemCollections.NAILS, 2)
        nails4 = ItemRequirement("Nails", ItemCollections.NAILS, 4)
        plank = ItemRequirement("Plank", ItemID.PLANK)
        plank3 = ItemRequirement("Plank", ItemID.PLANK, 3)
        plank2 = ItemRequirement("Plank", ItemID.PLANK, 2)
        lumberyardTeleport = ItemRequirement("Lumberyard Teleport", ItemID.LUMBERYARD_TELEPORT)
        varrockTeleport3 = ItemRequirement("Varrock Teleports", ItemID.VARROCK_TELEPORT, 3)

        log.debug("Requirements set up")
    }

    private fun setupConditions() {
        removedCampbed = VarbitRequirement(10568, 2)
        removedCarpet = VarbitRequirement(10569, 2)
        removedStool = VarbitRequirement(10564, 2)
        removedTable = VarbitRequirement(10567, 2)
        removedChair = VarbitRequirement(10565, 2)
        removedStool2 = VarbitRequirement(10563, 2)
        removedTable2 = VarbitRequirement(10566, 2)

        repairedCampbed = VarbitRequirement(10568, 3)
        repairedCarpet = VarbitRequirement(10569, 3)
        repairedStool = VarbitRequirement(10564, 3)
        repairedTable = VarbitRequirement(10567, 3)
        repairedChair = VarbitRequirement(10565, 3)
        repairedStool2 = VarbitRequirement(10563, 3)
        repairedTable2 = VarbitRequirement(10566, 3)
    }

    private fun setupSteps() {
        talkToMarlo = NpcStep.builder()
            .forNpc(10429)
            .at(Coordinate(3241, 3471, 0))
            .withText("Talk to Marlo in north east Varrock.")
            .withDialogSteps(
                "What kind of favour do you want me to do?",
                "Tell me more about the job.",
                "Tell me where he lives, and I'll do the job."
            )
            .build()

        talkToYarlo = NpcStep.builder()
            .forNpc(NpcID.OLD_MAN_YARLO)
            .at(Coordinate(3240, 3395, 0))
            .withText("Talk to Old Man Yarlo in south Varrock.")
            .build()

        talkToYarloAgain = NpcStep.builder()
            .forNpc(NpcID.OLD_MAN_YARLO)
            .at(Coordinate(3240, 3395, 0))
            .withText("Talk to Old Man Yarlo in south Varrock again.")
            .withDialogStep("Skip Yarlo's lecture. He'll offer it later if you like.")
            .build()

        talkToYarloOnceMore = NpcStep.builder()
            .forNpc(NpcID.OLD_MAN_YARLO)
            .at(Coordinate(3240, 3395, 0))
            .withText("Talk to Old Man Yarlo in south Varrock.")
            .build()

        talkToMarloToFinish = NpcStep.builder()
            .forNpc(NpcID.MARLO)
            .at(Coordinate(3241, 3471, 0))
            .withText("Talk to Marlo in north east Varrock to complete the quest.")
            .withDialogStep("Yeah, what have you got for me?")
            .build()

        removeCampbed = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40303)
            .at(Coordinate(3242, 3398, 0))
            .withText("Remove the camp bed in the house.")
            .withAction("Remove")
            .build()

        removeCarpet = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40304)
            .at(Coordinate(3239, 3395, 0))
            .withText("Remove the carpet in the house.")
            .withAction("Remove")
            .build()

        removeStool = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40301)
            .at(Coordinate(3239, 3394, 0))
            .withText("Remove the stool in the house.")
            .withAction("Demolish")
            .build()

        removeTable = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40224)
            .at(Coordinate(3240, 3394, 0))
            .withText("Remove the table in the house.")
            .withAction("Demolish")
            .build()

        removeChair = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40302)
            .at(Coordinate(3241, 3393, 0))
            .withText("Remove the chair in the house.")
            .withAction("Demolish")
            .build()

        removeTable2 = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40223)
            .at(Coordinate(3245, 3394, 0))
            .withText("Remove the table 2 in the house.")
            .withAction("Demolish")
            .build()

        removeStool2 = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40300)
            .at(Coordinate(3244, 3394, 0))
            .withText("Remove the stool 2 in the house.")
            .withAction("Demolish")
            .build()

        searchCrate = ObjectStep.builder()
            .forObject(ObjectID.CRATES_40214)
            .at(Coordinate(3243, 3398, 0))
            .withText("Search the crates in Yarlo's house for waxwood logs.")
            .withAction("Search")
            .build()

        talkToOperator = NpcStep.builder()
            .forNpc(NpcID.SAWMILL_OPERATOR)
            .at(Coordinate(3302, 3492, 0))
            .withText("Talk to the Sawmill Operator north east of Varrock to make waxwood planks.")
            .requiring(waxwoodLog3)
            .withDialogStep("I need some waxwood planks for Old Man Yarlo.")
            .build()

        buildCampbed = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40303)
            .at(Coordinate(3242, 3398, 0))
            .withText("Build the camp bed in the house.")
            .requiring(waxwoodPlank3, bolt2, hammer, saw)
            .withAction("Build")
            .build()

        buildCarpet = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40304)
            .at(Coordinate(3239, 3395, 0))
            .withText("Build the carpet in the house.")
            .requiring(bolt3, saw, hammer)
            .withAction("Build")
            .build()

        buildStool = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40301)
            .at(Coordinate(3239, 3394, 0))
            .withText("Build the stool in the house.")
            .requiring(plank, nails2, saw, hammer)
            .withAction("Build")
            .build()

        buildTable = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40224)
            .at(Coordinate(3240, 3394, 0))
            .withText("Build the table in the house.")
            .requiring(plank3, nails4, saw, hammer)
            .withAction("Build")
            .build()

        buildChair = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40302)
            .at(Coordinate(3241, 3393, 0))
            .withText("Build the chair in the house.")
            .requiring(plank2, nails2, saw, hammer)
            .withAction("Build")
            .build()

        buildTable2 = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40223)
            .at(Coordinate(3245, 3394, 0))
            .withText("Build the table 2 in the house.")
            .requiring(plank3, nails4, saw, hammer)
            .withAction("Build")
            .build()

        buildStool2 = ObjectStep.builder()
            .forObject(NullObjectID.NULL_40300)
            .at(Coordinate(3244, 3394, 0))
            .withText("Build the stool 2 in the house.")
            .requiring(plank, nails2, saw, hammer)
            .withAction("Build")
            .build()

        log.debug("Steps set up")
    }


    override fun loadSteps(): Map<Int, QuestStep> {
        setupRequirements()
        setupConditions()
        setupSteps()

        val steps = mutableMapOf<Int, QuestStep>()

        // Starting the quest
        steps[0] = talkToMarlo
        steps[1] = talkToYarlo

        // Removing furniture
        val removeItems = ConditionalStep.builder().withDefaultStep(removeCampbed)
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet,
                removedStool,
                removedTable,
                removedChair,
                removedStool2,
                removedTable2
            ),
            talkToYarloAgain
        )
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet,
                removedStool,
                removedTable,
                removedChair,
                removedStool2
            ),
            removeTable2
        )
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet,
                removedStool,
                removedTable,
                removedChair
            ),
            removeStool2
        )
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet,
                removedStool,
                removedTable
            ),
            removeChair
        )
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet,
                removedStool
            ),
            removeTable
        )
        removeItems.addStep(
            Conditions(
                LogicType.AND,
                removedCampbed,
                removedCarpet
            ),
            removeStool
        )
        removeItems.addStep(removedCampbed, removeCarpet)

        steps[2] = removeItems.build().apply { config.text = "Remove all furniture." }
        steps[3] = talkToYarloAgain
        steps[4] = talkToYarloAgain

        // Repairing furniture
        val repairFurniture = ConditionalStep.builder().withDefaultStep(buildCarpet)
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair,
                repairedStool2,
                repairedTable2,
                repairedCampbed
            ),
            talkToYarloOnceMore
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair,
                repairedStool2,
                repairedTable2,
                ItemRequirement("Waxwood plank", ItemID.WAXWOOD_PLANK, 3)
            ),
            buildCampbed
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair,
                repairedStool2,
                repairedTable2,
                ItemRequirement("Waxwood log", ItemID.WAXWOOD_LOG, 3)
            ),
            talkToOperator
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair,
                repairedStool2,
                repairedTable2
            ),
            searchCrate
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair,
                repairedStool2
            ),
            buildTable2
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable,
                repairedChair
            ),
            buildStool2
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool,
                repairedTable
            ),
            buildChair
        )
        repairFurniture.addStep(
            Conditions(
                LogicType.AND,
                repairedCarpet,
                repairedStool
            ),
            buildTable
        )
        repairFurniture.addStep(repairedCarpet, buildStool)

        steps[5] = repairFurniture.build()
        steps[6] = repairFurniture.build()
        steps[7] = repairFurniture.build()
        steps[8] = repairFurniture.build()
        steps[9] = repairFurniture.build()

        // Finishing quest
        steps[10] = talkToMarloToFinish
        steps[11] = talkToMarloToFinish
        steps[12] = talkToMarloToFinish

        return steps
    }

    override fun getVar(): Int = Varbits.load(10570)?.value ?: 1

    override fun getItemRequirements(): List<ItemRequirement> = listOf(plank10, nails20, bolt5, hammer, saw)

    override fun getItemRecommended(): List<ItemRequirement> = listOf(
        lumberyardTeleport,
        varrockTeleport3
    )

    override fun getExperienceRewards(): List<ExperienceReward> = listOf(
        ExperienceReward(Skill.CONSTRUCTION, 400)
    )

    override fun getItemRewards(): List<ItemReward> = listOf(
        ItemReward("Planks", ItemID.PLANK, 25),
        ItemReward("Oak Planks", ItemID.OAK_PLANK, 10),
        ItemReward("Mithril Nails", ItemID.MITHRIL_NAILS, 50),
        ItemReward("Steel Bars", ItemID.STEEL_BAR, 5),
        ItemReward("Bolt of Cloth", ItemID.BOLT_OF_CLOTH, 8),
        ItemReward("House Teleport Tablets", ItemID.TELEPORT_TO_HOUSE, 5),
        ItemReward("Falador Teleport Tablet", ItemID.FALADOR_TELEPORT, 1),
        ItemReward("POH in Rimmington or 1,000 Coins", ItemID.COINS_995, 1)
    )

    override fun getPanels(): List<PanelDetails> = listOf(
        PanelDetails(
            header = "Helping Yarlo & Marlo",
            steps = listOf(
                talkToMarlo,
                talkToYarlo,
                removeCampbed,
                talkToYarloAgain,
                buildCarpet,
                searchCrate,
                talkToOperator,
                buildCampbed,
                talkToYarloOnceMore,
                talkToMarloToFinish
            ),
            requirements = listOf(plank10, nails20, bolt5, hammer, saw)
        )
    )
}