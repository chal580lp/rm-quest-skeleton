package com.recursive.quester.quest


import com.recursive.quester.Bot
import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.*
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.requirements.widget.WidgetTextRequirement
import com.recursive.quester.framework.requirements.zone.Zone
import com.recursive.quester.framework.requirements.zone.ZoneRequirement
import com.recursive.quester.framework.reward.ExperienceReward
import com.recursive.quester.framework.reward.ItemReward
import com.recursive.quester.framework.reward.QuestPointReward
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.*
import com.recursive.quester.framework.util.getLogger
import com.recursive.quester.runelite.ItemCollections
import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.osrs.data.ItemID
import com.runemate.game.api.osrs.data.NpcID
import com.runemate.game.api.osrs.data.NullObjectID
import com.runemate.game.api.osrs.data.ObjectID

class FairyTaleI(private val bot: Bot) : BasicQuestHelper() {
    private val log = getLogger("FairyTaleI")

    // Items Required
    lateinit var secateurs: ItemRequirement
    lateinit var draynorSkull: ItemRequirement
    lateinit var spade: ItemRequirement
    lateinit var ghostspeak: ItemRequirement
    lateinit var dramenOrLunarStaff: ItemRequirement
    lateinit var randomItems: ItemRequirement

    // Items Recommended
    lateinit var varrockTeleport: ItemRequirement
    lateinit var faladorTeleport: ItemRequirement
    lateinit var lumbridgeTeleport: ItemRequirement
    lateinit var moryTele: ItemRequirement
    lateinit var food: ItemRequirement

    // Quest Items
    lateinit var symptomsList: ItemRequirement
    lateinit var magicSecateurs: ItemRequirement
    lateinit var magicSecateursEquipped: ItemRequirement
    lateinit var queensSecateurs: ItemRequirement
    lateinit var items3: ItemRequirement
    lateinit var skullOrSpade: ItemRequirement

    // Zones
    lateinit var zanaris: Zone
    lateinit var towerF1: Zone
    lateinit var towerF2: Zone
    lateinit var grotto: Zone
    lateinit var tanglefootRoom: Zone

    // Requirements
    lateinit var inZanaris: Requirement
    lateinit var inTowerF1: Requirement
    lateinit var inTowerF2: Requirement
    lateinit var inGrotto: Requirement
    lateinit var inTanglefootRoom: Requirement
    lateinit var talkedToFarmers: Requirement
    lateinit var secateursNearby: Requirement

    // Steps
    lateinit var talkToMartin: NpcStep
    lateinit var talkToFarmers: NpcStep
    lateinit var talkToMartinAgain: NpcStep
    lateinit var enterZanaris: ObjectStep
    lateinit var talkToGodfather: NpcStep
    lateinit var talkToNuff: NpcStep
    lateinit var climbTowerF0ToF1: ObjectStep
    lateinit var climbTowerF1ToF2: ObjectStep
    lateinit var talkToZandar: NpcStep
    lateinit var talkToMortifer: NpcStep
    lateinit var getSkull: DetailedQuestStep
    lateinit var giveMortiferItems: NpcStep
    lateinit var enterGrotto: ObjectStep
    lateinit var talkToSpirit: NpcStep
    lateinit var enterZanarisForFight: ObjectStep
    lateinit var enterTanglefootRoom: ObjectStep
    lateinit var killTanglefoot: NpcStep
    lateinit var pickUpSecateurs: DetailedQuestStep
    lateinit var enterZanarisForEnd: ObjectStep
    lateinit var talkToGodfatherToFinish: NpcStep

    override fun setupRequirements() {
        // Required Items
        secateurs = ItemRequirement("Secateurs", ItemID.SECATEURS)

        draynorSkull = ItemRequirement("Draynor skull", ItemID.DRAYNOR_SKULL).apply {
            canBeObtainedDuringQuest()
        }

        spade = ItemRequirement("Spade", ItemID.SPADE).apply {
            isNotConsumed()
        }

        ghostspeak = ItemRequirement("Ghostspeak amulet", ItemCollections.GHOSTSPEAK, 1, true).apply {
            isNotConsumed()
            setTooltip("You can get another from Father Urhney in the Lumbridge Swamp")
        }

        dramenOrLunarStaff = ItemRequirement("Dramen or lunar staff", ItemID.DRAMEN_STAFF, 1, true).apply {
            isNotConsumed()
            addAlternates(ItemID.LUNAR_STAFF)
            setDisplayMatchedItemName(true)
        }

        randomItems = ItemRequirement("3 random items requested by Malignius", -1)

        // Recommended Items
        varrockTeleport = ItemRequirement("Varrock teleport", ItemID.VARROCK_TELEPORT)
        faladorTeleport = ItemRequirement("Falador teleport", ItemID.FALADOR_TELEPORT)
        lumbridgeTeleport = ItemRequirement("Lumbridge teleport", ItemID.LUMBRIDGE_TELEPORT)

        moryTele = ItemRequirement("Teleport to Morytania", ItemID.MORTTON_TELEPORT).apply {
            addAlternates(ItemID.BARROWS_TELEPORT, ItemID.ECTOPHIAL)
        }

        food = ItemRequirement("Food", ItemCollections.GOOD_EATING_FOOD, -1)

        // Quest Items
        symptomsList = ItemRequirement("Symptoms list", ItemID.SYMPTOMS_LIST).apply {
            setTooltip("You can get another from Fairy Nuff")
        }

        magicSecateurs = ItemRequirement("Magic secateurs", ItemID.MAGIC_SECATEURS).apply {
            setTooltip("If you lost these you'll need to have the Nature Spirit enchant another secateur")
        }

        magicSecateursEquipped = ItemRequirement("Magic secateurs", ItemID.MAGIC_SECATEURS, 1, true).apply {
            setTooltip("If you lost these you'll need to have the Nature Spirit enchant another secateur")
        }

        queensSecateurs = ItemRequirement("Queen's secateurs", ItemID.QUEENS_SECATEURS).apply {
            setTooltip("You can get another by killing another Tanglefoot")
        }

        items3 = ItemRequirement("3 items Mortifer told you to get", -1, -1)

        skullOrSpade = ItemRequirement("Draynor skull or a spade to get it", ItemID.DRAYNOR_SKULL).apply {
            addAlternates(ItemID.SPADE)
        }

        log.debug("Requirements set up")
    }

    override fun setupZones() {
        zanaris = Zone(Coordinate(2368, 4353, 0), Coordinate(2495, 4479, 0))
        towerF1 = Zone(Coordinate(2900, 3324, 1), Coordinate(2914, 3341, 1))
        towerF2 = Zone(Coordinate(2900, 3324, 2), Coordinate(2914, 3341, 2))
        grotto = Zone(Coordinate(3435, 9733, 1), Coordinate(3448, 9746, 1))
        tanglefootRoom = Zone(Coordinate(2368, 4353, 0), Coordinate(2402, 4399, 0))
    }

    private fun setupConditions() {
        inZanaris = ZoneRequirement(zanaris)
        inTowerF1 = ZoneRequirement(towerF1)
        inTowerF2 = ZoneRequirement(towerF2)
        inGrotto = ZoneRequirement(grotto)
        inTanglefootRoom = Conditions(InInstanceRequirement(), ZoneRequirement(tanglefootRoom))

        talkedToFarmers = Conditions(
            true,
            LogicType.OR,
            WidgetTextRequirement(119, 5, true, "back and talk to <col=800000>Martin"),
            DialogRequirement("Right, well thanks for your input."),
            DialogRequirement("I don't think the crops ARE failing")
        )

        secateursNearby = ItemOnTileRequirement(queensSecateurs)
    }

    private fun setupSteps() {
        talkToMartin = NpcStep.builder()
            .forNpc(NpcID.MARTIN_THE_MASTER_GARDENER)
            .at(Coordinate(3078, 3256, 0))
            .withText("Talk to Martin in the Draynor Market.")
            .withDialogSteps(
                "Ask about the quest.",
                "Anything I can help with?",
                "Now that I think about it, you're right!"
            )
            .build()

        talkToFarmers = NpcStep.builder()
            .forNpc("Elstan")
            .withText("Talk to 5 farmers, then return to Martin in Draynor Village. The recommended 5 are:")
            .withDialogStep("Are you a member of the Group of Advanced Gardeners?")
            .withAlternateNpcs(
                NpcID.FRIZZY_SKERNIP,
                NpcID.HESKEL,
                NpcID.DREVEN,
                NpcID.FAYETH,
                NpcID.TREZNOR_11957
            )
            .build()

        talkToMartinAgain = NpcStep.builder()
            .forNpc(NpcID.MARTIN_THE_MASTER_GARDENER)
            .at(Coordinate(3078, 3256, 0))
            .withText("Return to Martin in the Draynor Market.")
            .withDialogStep("Ask about the quest.")
            .build()

        enterZanaris = ObjectStep.builder()
            .forObject(ObjectID.DOOR_2406)
            .at(Coordinate(3202, 3169, 0))
            .withText("Travel to Zanaris.")
            .requiring(dramenOrLunarStaff)
            .build()

        talkToGodfather = NpcStep.builder()
            .forNpc(NpcID.FAIRY_GODFATHER_5837)
            .at(Coordinate(2447, 4430, 0))
            .withText("Talk to the Fairy Godfather in the Throne Room.")
            .withDialogStep("Where's the Fairy Queen?")
            .build()

        talkToNuff = NpcStep.builder()
            .forNpc(NpcID.FAIRY_NUFF)
            .at(Coordinate(2386, 4472, 0))
            .withText("Talk to Fairy Nuff in north west Zanaris.")
            .build()

        climbTowerF0ToF1 = ObjectStep.builder()
            .forObject(ObjectID.STAIRCASE_11888)
            .at(Coordinate(2908, 3335, 0))
            .withText("Talk to Zandar in the Dark Wizards' Tower west of Falador.")
            .requiring(symptomsList)
            .build()

        climbTowerF1ToF2 = ObjectStep.builder()
            .forObject(ObjectID.STAIRCASE_11889)
            .at(Coordinate(2908, 3335, 1))
            .withText("Talk to Zandar in the Dark Wizards' Tower west of Falador.")
            .requiring(symptomsList)
            .withDialogStep("Climb up")
            .build()

        talkToZandar = NpcStep.builder()
            .forNpc(NpcID.ZANDAR_HORFYRE)
            .at(Coordinate(2907, 3335, 2))
            .withText("Talk to Zandar in the Dark Wizards' Tower west of Falador.")
            .requiring(symptomsList)
            .build()

        talkToMortifer = NpcStep.builder()
            .forNpc(NpcID.MALIGNIUS_MORTIFER)
            .at(Coordinate(2991, 3270, 0))
            .withText("Talk to Malignius Mortifer west of Port Sarim.")
            .requiring(draynorSkull)
            .withDialogSteps(
                "I need help with fighting a Tanglefoot.",
                "I was asking you about fighting a Tanglefoot..."
            )
            .build()

        getSkull = DetailedQuestStep.builder()
            .at(Coordinate(3106, 3383, 0))
            .withText("Dig for a skull in the north of Draynor Manor.")
            .build()

        giveMortiferItems = NpcStep.builder()
            .forNpc(NpcID.MALIGNIUS_MORTIFER)
            .at(Coordinate(2991, 3270, 0))
            .withText("Give Malignius Mortifer the items he wanted.")
            .build()

        enterGrotto = ObjectStep.builder()
            .forObject(ObjectID.GROTTO)
            .at(Coordinate(3440, 3337, 0))
            .withText("Get the items Mortifer tells you to get, and enter the Grotto in the south of Mort Myre.")
            .requiring(ghostspeak, secateurs, items3)
            .build()

        talkToSpirit = NpcStep.builder()
            .forNpc(NpcID.NATURE_SPIRIT)
            .at(Coordinate(3441, 9738, 1))
            .withText("Talk to the Nature Spirit.")
            .requiring(ghostspeak, secateurs, items3)
            .build()

        enterZanarisForFight = ObjectStep.builder()
            .forObject(ObjectID.DOOR_2406)
            .at(Coordinate(3202, 3169, 0))
            .withText("Travel to Zanaris, ready to fight the Tanglefoot.")
            .requiring(dramenOrLunarStaff, magicSecateurs)
            .build()

        enterTanglefootRoom = ObjectStep.builder()
            .forObject(NullObjectID.NULL_11999)
            .at(Coordinate(2399, 4379, 0))
            .withText("Enter the tanglefoot lair in the south of Zanaris, near the cosmic altar.")
            .requiring(magicSecateursEquipped, food)
            .build()

        killTanglefoot = NpcStep.builder()
            .forNpc(NpcID.TANGLEFOOT)
            .at(Coordinate(2375, 4385, 0))
            .withText("Kill the large Tanglefoot with the Magic Secateurs. You can flinch it on a corner.")
            .requiring(magicSecateursEquipped)
            .build()

        pickUpSecateurs = DetailedQuestStep.builder()
            .withText("Pick up the queen's secateurs.")
            .requiring(queensSecateurs)
            .build()

        enterZanarisForEnd = ObjectStep.builder()
            .forObject(ObjectID.DOOR_2406)
            .at(Coordinate(3202, 3169, 0))
            .withText("Talk to the Fairy Godfather in Zanaris to finish the quest.")
            .requiring(dramenOrLunarStaff, queensSecateurs)
            .build()

        talkToGodfatherToFinish = NpcStep.builder()
            .forNpc(NpcID.FAIRY_GODFATHER_5837)
            .at(Coordinate(2447, 4430, 0))
            .withText("Talk to the Fairy Godfather to finish the quest.")
            .requiring(queensSecateurs)
            .build()

        // Add substeps
        talkToFarmers.addSubSteps(talkToMartinAgain)
        talkToZandar.addSubSteps(climbTowerF1ToF2, climbTowerF0ToF1)
        talkToGodfatherToFinish.addSubSteps(enterZanarisForEnd)

        log.debug("Steps set up")
    }

    override fun loadSteps(): Map<Int, QuestStep> {
        setupRequirements()
        setupZones()
        setupConditions()
        setupSteps()

        val steps = mutableMapOf<Int, QuestStep>()

        val startQuest = ConditionalStep.builder().withDefaultStep(talkToMartin)
        steps[0] = startQuest.build()

        val goTalkToFarmers = ConditionalStep.builder()
            .withDefaultStep(talkToFarmers)
            .apply {
                addStep(talkedToFarmers, talkToMartinAgain)
            }
        steps[10] = goTalkToFarmers.build()

        val goTalkToGodfather = ConditionalStep.builder()
            .withDefaultStep(enterZanaris)
            .apply {
                addStep(inZanaris, talkToGodfather)
            }
        steps[20] = goTalkToGodfather.build()

        val goTalkToNuff = ConditionalStep.builder()
            .withDefaultStep(enterZanaris)
            .apply {
                addStep(inZanaris, talkToNuff)
            }
        steps[30] = goTalkToNuff.build()

        val goTalkToZandar = ConditionalStep.builder()
            .withDefaultStep(climbTowerF0ToF1)
            .apply {
                addStep(inTowerF2, talkToZandar)
                addStep(inTowerF1, climbTowerF1ToF2)
            }
        steps[40] = goTalkToZandar.build()

        val goTalkToMortifer = ConditionalStep.builder()
            .withDefaultStep(getSkull)
            .apply {
                addStep(draynorSkull, talkToMortifer)
            }
        steps[50] = goTalkToMortifer.build()

        val goEnchantSecateurs = ConditionalStep.builder()
            .withDefaultStep(enterGrotto)
            .apply {
                addStep(inGrotto, talkToSpirit)
            }
        steps[60] = goEnchantSecateurs.build()

        val goKillTanglefoot = ConditionalStep.builder()
            .withDefaultStep(enterZanarisForFight)
            .apply {
                addStep(secateursNearby, pickUpSecateurs)
                addStep(inTanglefootRoom, killTanglefoot)
                addStep(inZanaris, enterTanglefootRoom)
            }
        steps[70] = goKillTanglefoot.build()

        val finishQuest = ConditionalStep.builder()
            .withDefaultStep(enterZanarisForEnd)
            .apply {
                addStep(inZanaris, talkToGodfatherToFinish)
            }
        steps[80] = finishQuest.build()

        return steps
    }

    override fun getVar(): Int {
        return 0
    }

    override fun getItemRequirements(): List<ItemRequirement> = listOf(
        secateurs,
        ghostspeak,
        dramenOrLunarStaff,
    )

    override fun getItemRecommended(): List<ItemRequirement> = listOf(
        varrockTeleport,
        faladorTeleport,
        lumbridgeTeleport,
        moryTele,
        food
    )

    override fun getGeneralRequirements(): List<Requirement> = listOf(
        QuestRequirement.withStatus(Quest.OSRS.LOST_CITY, Quest.Status.COMPLETE),
        QuestRequirement.withStatus(Quest.OSRS.NATURE_SPIRIT, Quest.Status.COMPLETE)
    )

    override fun getCombatRequirements(): List<String> = listOf(
        "Tanglefoot (level 111)"
    )

    override fun getQuestPointReward(): QuestPointReward = QuestPointReward(2)

    override fun getExperienceRewards(): List<ExperienceReward> = listOf(
        ExperienceReward(Skill.FARMING, 3500),
        ExperienceReward(Skill.ATTACK, 2000),
        ExperienceReward(Skill.MAGIC, 1000)
    )

    override fun getItemRewards(): List<ItemReward> = listOf(
        ItemReward("Magic Secateurs", ItemID.MAGIC_SECATEURS, 1)
    )

    override fun getPanels(): List<PanelDetails> = listOf(
        PanelDetails(
            header = "Investigating",
            steps = listOf(talkToMartin, talkToFarmers, talkToMartinAgain)
        ),
        PanelDetails(
            header = "Unnatural events",
            steps = listOf(enterZanaris, talkToGodfather, talkToNuff, talkToZandar),
            requirements = listOf(dramenOrLunarStaff)
        ),
        PanelDetails(
            header = "Finding a cure",
            steps = listOf(getSkull, talkToMortifer),
            requirements = listOf(skullOrSpade)
        ),
        PanelDetails(
            header = "Enchanting secateurs",
            steps = listOf(enterGrotto, talkToSpirit),
            requirements = listOf(ghostspeak, secateurs, items3)
        ),
        PanelDetails(
            header = "Defeat the Tanglefoot",
            steps = listOf(enterZanarisForFight, enterTanglefootRoom, killTanglefoot, talkToGodfatherToFinish),
            requirements = listOf(dramenOrLunarStaff, magicSecateurs, food)
        )
    )

    private fun talkToFiveFarmers() {
        // I've already told you
        // Heskel 2679 Coordinate(3000, 3375, 0) wanders around
        // Treznor 11957 Coordinate(3226, 3457, 0) wanders around
        // Dreven 2674 Coordinate(3182, 3361, 0) wanders around

    }
}