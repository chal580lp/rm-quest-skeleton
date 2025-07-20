package com.recursive.quester.quest

import com.recursive.quester.Bot
import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.*
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.conditional.NpcCondition
import com.recursive.quester.framework.requirements.player.PrayerPointRequirement
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.requirements.widget.WidgetTextRequirement
import com.recursive.quester.framework.requirements.zone.Zone
import com.recursive.quester.framework.requirements.zone.ZoneRequirement
import com.recursive.quester.framework.reward.ExperienceReward
import com.recursive.quester.framework.reward.QuestPointReward
import com.recursive.quester.framework.reward.UnlockReward
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.*
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.local.Varps
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.util.Regex
import com.runemate.game.api.osrs.data.ItemID
import com.runemate.game.api.osrs.data.NpcID
import com.runemate.game.api.osrs.data.ObjectID

class NatureSpirit(private val bot: Bot) : BasicQuestHelper() {
    private val log = getLogger("NatureSpirit")

    // Items Required
    lateinit var ghostspeak: ItemRequirement
    lateinit var silverSickle: ItemRequirement
    lateinit var washingBowl: ItemRequirement
    lateinit var mirror: ItemRequirement
    lateinit var journal: ItemRequirement
    lateinit var druidicSpell: ItemRequirement
    lateinit var druidPouch: ItemRequirement
    lateinit var blessedSickle: ItemRequirement
    lateinit var spellCard: ItemRequirement
    lateinit var mirrorHighlighted: ItemRequirement
    lateinit var journalHighlighted: ItemRequirement
    lateinit var mushroom: ItemRequirement
    lateinit var mushroomHighlighted: ItemRequirement
    lateinit var druidPouchFull: ItemRequirement

    // Items Recommended
    lateinit var combatGear: ItemRequirement
    lateinit var salveTele: ItemRequirement

    // Requirements
    lateinit var inUnderground: Requirement
    lateinit var fillimanNearby: Requirement
    lateinit var mirrorNearby: Requirement
    lateinit var usedMushroom: Requirement
    lateinit var onOrange: Requirement
    lateinit var usedCard: Requirement
    lateinit var inGrotto: Requirement
    lateinit var inOutsideGrotto: Requirement
    lateinit var natureSpiritNearby: Requirement
    lateinit var ghastNearby: Requirement
    lateinit var prayerPoints: Requirement

    // Steps
    lateinit var goDownToDrezel: ObjectStep
    lateinit var talkToDrezel: NpcStep
    lateinit var leaveDrezel: ObjectStep
    lateinit var enterSwamp: ObjectStep
    lateinit var jumpBridge: ObjectStep // Custom
    lateinit var tryToEnterGrotto: ObjectStep
    lateinit var talkToFilliman: NpcStep
    lateinit var takeWashingBowl: DetailedQuestStep
    lateinit var takeMirror: DetailedQuestStep
    lateinit var useMirrorOnFilliman: NpcStep
    lateinit var searchGrotto: ObjectStep
    lateinit var useJournalOnFilliman: NpcStep
    lateinit var goBackDownToDrezel: ObjectStep
    lateinit var talkToDrezelForBlessing: NpcStep
    lateinit var castSpellAndGetMushroom: DetailedQuestStep
    lateinit var useMushroom: ObjectStep
    lateinit var useSpellCard: ObjectStep
    lateinit var standOnOrange: DetailedQuestStep
    lateinit var tellFillimanToCast: NpcStep
    lateinit var enterGrotto: ObjectStep
    lateinit var searchAltar: ObjectStep
    lateinit var blessSickle: NpcStep
    lateinit var fillPouches: DetailedQuestStep
    lateinit var killGhasts: NpcStep
    lateinit var killGhast: NpcStep
    lateinit var enterGrottoAgain: ObjectStep
    lateinit var touchAltarAgain: ObjectStep
    lateinit var talkToNatureSpiritToFinish: NpcStep
    lateinit var spawnFillimanForRitual: ObjectStep
    lateinit var talkToFillimanInGrotto: NpcStep

    // Zones
    lateinit var underground: Zone
    lateinit var orangeStone: Zone
    lateinit var grotto: Zone
    lateinit var outsideGrotto: Zone

    override fun setupRequirements() {
        ghostspeak = ItemRequirement("Ghostspeak amulet", ItemID.GHOSTSPEAK_AMULET, 1, true).apply {
            isNotConsumed()
        }
        silverSickle = ItemRequirement("Silver sickle", ItemID.SILVER_SICKLE)
        washingBowl = ItemRequirement("Washing bowl", ItemID.WASHING_BOWL)
        mirror = ItemRequirement("Mirror", ItemID.MIRROR)
        mirrorHighlighted = ItemRequirement("Mirror", ItemID.MIRROR).apply {
            setHighlightInInventory(true)
        }
        journal = ItemRequirement("Journal", ItemID.JOURNAL)
        journalHighlighted = ItemRequirement("Journal", ItemID.JOURNAL).apply {
            setHighlightInInventory(true)
        }
        druidicSpell = ItemRequirement("Druidic spell", ItemID.DRUIDIC_SPELL).apply {
            setTooltip("You can get another from Filliman")
        }
        druidPouch = ItemRequirement("Druidic pouch", ItemID.DRUID_POUCH).apply {
            setTooltip("You can get another from the Grotto")
        }
        druidPouchFull = ItemRequirement("Druidic pouch", ItemID.DRUID_POUCH_2958)
        blessedSickle = ItemRequirement("Silver sickle (b)", ItemID.SILVER_SICKLE_B).apply {
            setTooltip("You can bless another silver sickle with the nature spirit")
        }
        spellCard = ItemRequirement("A used spell or druidic spell", ItemID.A_USED_SPELL).apply {
            addAlternates(ItemID.DRUIDIC_SPELL)
            setTooltip("You can get another spell from Filliman")
        }
        mushroom = ItemRequirement("Mort myre fungus", ItemID.MORT_MYRE_FUNGUS)
        mushroomHighlighted = ItemRequirement("Mort myre fungus", ItemID.MORT_MYRE_FUNGUS).apply {
            setHighlightInInventory(true)
        }
        salveTele = ItemRequirement("Salve Graveyard Teleports", ItemID.SALVE_GRAVEYARD_TELEPORT, 2)
        combatGear = ItemRequirement("Combat gear to kill the ghasts", -1, -1).apply {
            isNotConsumed()
        }

        log.debug("Requirements set up")
    }

    override fun setupZones() {
        underground = Zone(Coordinate(3402, 9880, 0), Coordinate(3443, 9907, 0))
        orangeStone = Zone(Coordinate(3440, 3335, 0), Coordinate(3440, 3335, 0))
        grotto = Zone(Coordinate(3435, 9733, 0), Coordinate(3448, 9746, 0))
        outsideGrotto = Zone(Coordinate(3434, 3342, 0), Coordinate(3447, 3331, 0))
    }

    private fun setupConditions() {
        inUnderground = ZoneRequirement(underground)
        onOrange = ZoneRequirement(orangeStone)
        inGrotto = ZoneRequirement(grotto)
        inOutsideGrotto = ZoneRequirement(outsideGrotto)
        fillimanNearby = NpcCondition("Filliman Tarlock")
        natureSpiritNearby = NpcCondition(NpcID.NATURE_SPIRIT)
        mirrorNearby = ItemOnTileRequirement(mirror)
        usedMushroom = Conditions(
            true,
            LogicType.OR,
            ChatMessageRequirement("The stone seems to absorb the fungus."),
            WidgetTextRequirement(229, 1, "nature symbol<br>scratched into it. This stone seems complete in some way."),
            WidgetTextRequirement(119, 5, true, "Mort Myre Fungi was absorbed")
        )
        usedCard = Conditions(
            true,
            LogicType.OR,
            ChatMessageRequirement("The stone seems to absorb the used spell scroll."),
            ChatMessageRequirement("The stone seems to absorb the spell scroll."),
            WidgetTextRequirement(229, 1, "spirit symbol<br>scratched into it. This stone seems to be complete"),
            WidgetTextRequirement(119, 5, true, "spell scroll was absorbed")
        )
        ghastNearby = NpcCondition(NpcID.GHAST_946)
        prayerPoints = PrayerPointRequirement(1)
    }

    private fun setupSteps() {
        goDownToDrezel = ObjectStep.builder()
            .forObject(ObjectID.TRAPDOOR_1579)
            .at(Coordinate(3405, 3507, 0))
            .withText("Talk to Drezel under the Paterdomus Temple.")
            .withDialogSteps("Well, what is it, I may be able to help?", "Yes.")
            .withAlternateObjects(ObjectID.TRAPDOOR_1580, ObjectID.TRAPDOOR_1581, ObjectID.TRAPDOOR_1582)
            .withAction(Regex.getPatternContainingOneOf("Open", "Climb-down"))
            .build()

        talkToDrezel = NpcStep.builder()
            .forNpc("Drezel")
            .at(Coordinate(3439, 9896, 0))
            .withText("Talk to Drezel under the Paterdomus Temple.")
            .build()
        talkToDrezel.addSubSteps(goDownToDrezel)

        leaveDrezel = ObjectStep.builder()
            .forObject(ObjectID.HOLY_BARRIER)
            .at(Coordinate(3440, 9886, 0))
            .withText("Enter the Mort Myre from the north gate.")
            .withAction("Pass-through")
            .build()

        enterSwamp = ObjectStep.builder()
            .forObject(ObjectID.GATE_3506)
            .at(Coordinate(3444, 3458, 0))
            .withText("Enter the Mort Myre from the north gate.")
            .requiring(ghostspeak)
            .withAction("Open")
            .build()

        jumpBridge = ObjectStep.builder()
            .forObject(3522)
            .at(Coordinate(3440, 3329, 0))
            .withText("Jump the bridge into the Grotto.")
            .requiring(ghostspeak)
            .withAction("Jump")
            .build()

        tryToEnterGrotto = ObjectStep.builder()
            .forObject(ObjectID.GROTTO)
            .at(Coordinate(3440, 3337, 0))
            .withText("Attempt to enter the Grotto in the south of Mort Myre.")
            .requiring(ghostspeak)
            .withAction("Enter")
            .withDialogStep("How long have you been a ghost?")
            .build()

        talkToFilliman = NpcStep.builder()
            .forNpc("Filliman Tarlock")
            .at(Coordinate(3440, 3336, 0))
            .withText("Talk to Filliman Tarlock.")
            .requiring(ghostspeak)
            .withDialogStep("How long have you been a ghost?")
            .build()

        takeWashingBowl = DetailedQuestStep.builder()
            .at(Coordinate(3437, 3337, 0))
            .withText("Pick up the washing bowl.")
            .build()

        takeMirror = DetailedQuestStep.builder()
            .at(Coordinate(3437, 3337, 0))
            .withText("Pick up the mirror.")
            .requiring(washingBowl)
            .build()

        useMirrorOnFilliman = NpcStep.builder()
            .forNpc("Filliman Tarlock")
            .at(Coordinate(3440, 3336, 0))
            .withText("Use the mirror on Filliman Tarlock.")
            .requiring(ghostspeak, mirrorHighlighted, washingBowl)
            .withDialogStep("How long have you been a ghost?")
            .useItemOn(mirror)
            .build()
//		useMirrorOnFilliman.addIcon(ItemID.MIRROR)

        searchGrotto = ObjectStep.builder()
            .forObject(ObjectID.GROTTO_TREE)
            .at(Coordinate(3440, 3339, 0))
            .withText("Right-click search the grotto tree.")
            .withAction("Search")
            .build()

        useJournalOnFilliman = NpcStep.builder()
            .forNpc("Filliman Tarlock")
            .at(Coordinate(3440, 3336, 0))
            .withText("Use the journal on Filliman Tarlock.")
            .requiring(ghostspeak, journalHighlighted)
            .withDialogStep("How can I help?")
            .useItemOn(journal)
            .build()
//		useJournalOnFilliman.addIcon(ItemID.JOURNAL)

        goBackDownToDrezel = ObjectStep.builder()
            .forObject(ObjectID.TRAPDOOR_3432)
            .withAlternateObjects(ObjectID.TRAPDOOR_3433)
            .at(Coordinate(3422, 3485, 0))
            .withText("Talk to Drezel to get blessed.")
            .withAction(Regex.getPatternContainingOneOf("Open", "Climb-down"))
            .build()

        talkToDrezelForBlessing = NpcStep.builder()
            .forNpc("Drezel")
            .at(Coordinate(3439, 9896, 0))
            .withText("Talk to Drezel under the Paterdomus Temple.")
            .build()
        talkToDrezelForBlessing.addSubSteps(goBackDownToDrezel)

        castSpellAndGetMushroom = DetailedQuestStep.builder()
            .withDialogStep("Cast the druidic spell next to a rotten log in Mort Myre to grow a mushroom. Pick it. If you already have, open the quest journal to re-sync your state.")
            .withTeleport(druidicSpell)
            .build()

        useMushroom = ObjectStep.builder()
            .forObject(ObjectID.STONE)
            .at(Coordinate(3439, 3336, 0))
            .withText("Use the mushroom on the brown stone outside the grotto. If you already have, search it instead.")
            .requiring(mushroomHighlighted)
            .build()
//		useMushroom.addIcon(ItemID.MORT_MYRE_FUNGUS)

        useSpellCard = ObjectStep.builder()
            .forObject(ObjectID.STONE_3529)
            .at(Coordinate(3441, 3336, 0))
            .withText("Use the used spell on the gray stone outside the grotto. If you already have, search it instead.")
            .requiring(spellCard)
            .build()
//		useSpellCard.addIcon(ItemID.A_USED_SPELL)

//		standOnOrange = DetailedQuestStep(this, Coordinate(3440, 3335, 0), "Stand on the orange stone outside the grotto.")
        standOnOrange = DetailedQuestStep.builder()
            .at(Coordinate(3440, 3335, 0))
            .withText("Stand on the orange stone outside the grotto.")
            .requiring(onOrange)
            .build()

        tellFillimanToCast = NpcStep.builder()
            .forNpc("Filliman Tarlock")
            .at(Coordinate(3440, 3336, 0))
            .withText("Tell Filliman Tarlock you're ready.")
            .requiring(ghostspeak)
            .withDialogStep("I think I've solved the puzzle!")
            .build()

        spawnFillimanForRitual = ObjectStep.builder()
            .forObject(ObjectID.GROTTO)
            .at(Coordinate(3440, 3337, 0))
            .withText("Attempt to enter the Grotto in the south of Mort Myre to spawn Filliman.")
            .requiring(ghostspeak)
            .withDialogStep("I think I've solved the puzzle!")
            .build()
        tellFillimanToCast.addSubSteps(spawnFillimanForRitual)

        enterGrotto = ObjectStep.builder()
            .forObject(ObjectID.GROTTO)
            .at(Coordinate(3440, 3337, 0))
            .withText("Enter the Grotto in the south of Mort Myre.")
            .requiring(ghostspeak, silverSickle)
            .build()

        searchAltar = ObjectStep.builder()
            .forObject(ObjectID.GROTTO_3520)
            .at(Coordinate(3442, 9741, 0))
            .withText("Search the grotto inside.")
            .requiring(ghostspeak)
            .build()

        talkToFillimanInGrotto = NpcStep.builder()
            .forNpc("Filliman Tarlock")
            .at(Coordinate(3441, 9738, 0))
            .withText("Talk to Filliman in the grotto to bless your sickle.")
            .requiring(ghostspeak, silverSickle)
            .build()

        blessSickle = NpcStep.builder()
            .forNpc("Nature Spirit")
            .at(Coordinate(3441, 9738, 0))
            .withText("Talk to the Nature Spirit in the grotto to bless your sickle.")
            .requiring(ghostspeak, silverSickle)
            .build()

//		fillPouches = DetailedQuestStep(
//			this,
//			"Right-click 'bloom' the blessed sickle next to rotten logs for mort myre fungi. Use these to fill the druid pouch.",
//			blessedSickle,
//			prayerPoints
//		)
        fillPouches = DetailedQuestStep.builder()
            .withDialogStep("Right-click 'bloom' the blessed sickle next to rotten logs for mort myre fungi. Use these to fill the druid pouch.")
            .requiring(blessedSickle, prayerPoints)
            .build()

        killGhasts = NpcStep.builder()
            .forNpc(NpcID.GHAST)
            .withText("Use the filled druid pouch on a ghast to make it attackable and kill it. You'll need to kill 3.")
            .requiring(druidPouchFull)
            .build()

        killGhast = NpcStep.builder()
            .forNpc(NpcID.GHAST_946)
            .withText("Kill the ghast.")
            .requiring(druidPouchFull)
            .build()
        killGhasts.addSubSteps(killGhast)

        enterGrottoAgain = ObjectStep.builder()
            .forObject(ObjectID.GROTTO)
            .at(Coordinate(3440, 3337, 0))
            .withText("Enter the Grotto in the south of Mort Myre.")
            .requiring(ghostspeak)
            .build()

        touchAltarAgain = ObjectStep.builder()
            .forObject(ObjectID.GROTTO_3520)
            .at(Coordinate(3442, 9741, 0))
            .withText("Search the grotto inside.")
            .requiring(ghostspeak)
            .build()

        talkToNatureSpiritToFinish = NpcStep.builder()
            .forNpc(NpcID.NATURE_SPIRIT)
            .at(Coordinate(3441, 9738, 0))
            .withText("Talk to the Nature Spirit in the grotto to finish the quest!")
            .requiring(ghostspeak)
            .build()
        talkToNatureSpiritToFinish.addSubSteps(touchAltarAgain)

        log.debug("Steps set up")
    }

    override fun loadSteps(): Map<Int, QuestStep> {
        setupRequirements()
        setupZones()
        setupConditions()
        setupSteps()

        val steps = mutableMapOf<Int, QuestStep>()

        val startQuest = ConditionalStep.builder().withDefaultStep(goDownToDrezel)
        startQuest.addStep(inUnderground, talkToDrezel)

        steps[0] = startQuest.build()

        val goEnterSwamp = ConditionalStep.builder().withDefaultStep(enterSwamp)
        goEnterSwamp.addStep(inUnderground, leaveDrezel)
        steps[1] = goEnterSwamp.build()
        steps[2] = goEnterSwamp.build()
        steps[3] = goEnterSwamp.build()
        steps[4] = goEnterSwamp.build()
        steps[5] = goEnterSwamp.build()

        val goTalkToFilliman = ConditionalStep.builder().withDefaultStep(jumpBridge)
        goTalkToFilliman.addStep(Conditions(inOutsideGrotto, fillimanNearby), talkToFilliman)
        goTalkToFilliman.addStep(inOutsideGrotto, tryToEnterGrotto)
        steps[10] = goTalkToFilliman.build()
        steps[15] = goTalkToFilliman.build()

        val showFillimanReflection = ConditionalStep.builder().withDefaultStep(takeWashingBowl)
        showFillimanReflection.addStep(Conditions(mirror, fillimanNearby), useMirrorOnFilliman)
        showFillimanReflection.addStep(mirror, tryToEnterGrotto)
        showFillimanReflection.addStep(mirrorNearby, takeMirror)
        steps[20] = showFillimanReflection.build()

        val goGetJournal = ConditionalStep.builder().withDefaultStep(searchGrotto)
        goGetJournal.addStep(Conditions(journal, fillimanNearby), useJournalOnFilliman)
        goGetJournal.addStep(journal, tryToEnterGrotto)
        steps[25] = goGetJournal.build()

        val goOfferHelp = ConditionalStep.builder().withDefaultStep(tryToEnterGrotto)
        goOfferHelp.addStep(fillimanNearby, useJournalOnFilliman)
        steps[30] = goOfferHelp.build()

        val getBlessed = ConditionalStep.builder().withDefaultStep(goBackDownToDrezel)
        getBlessed.addStep(inUnderground, talkToDrezelForBlessing)
        steps[35] = getBlessed.build()

        val performRitual = ConditionalStep.builder().withDefaultStep(castSpellAndGetMushroom)
        performRitual.addStep(Conditions(usedMushroom, usedCard, fillimanNearby, onOrange), tellFillimanToCast)
        performRitual.addStep(Conditions(usedMushroom, usedCard, fillimanNearby), standOnOrange)
        performRitual.addStep(Conditions(usedMushroom, usedCard), spawnFillimanForRitual)
        performRitual.addStep(usedMushroom, useSpellCard)
        performRitual.addStep(mushroom, useMushroom)
        steps[40] = performRitual.build()
        steps[45] = performRitual.build()
        steps[50] = performRitual.build()
        steps[55] = performRitual.build()

        val goTalkInGrotto = ConditionalStep.builder().withDefaultStep(enterGrotto)
        goTalkInGrotto.addStep(Conditions(inGrotto, fillimanNearby), talkToFillimanInGrotto)
        goTalkInGrotto.addStep(inGrotto, searchAltar)
        steps[60] = goTalkInGrotto.build()

        val goBlessSickle = ConditionalStep.builder().withDefaultStep(enterGrotto)
        goBlessSickle.addStep(Conditions(inGrotto, natureSpiritNearby), blessSickle)
        goBlessSickle.addStep(inGrotto, searchAltar)
        steps[65] = goBlessSickle.build()
        steps[70] = goBlessSickle.build()

        val goKillGhasts = ConditionalStep.builder().withDefaultStep(fillPouches)
        goKillGhasts.addStep(ghastNearby, killGhast)
        goKillGhasts.addStep(druidPouchFull, killGhasts)
        steps[75] = goKillGhasts.build()
        steps[80] = goKillGhasts.build()
        steps[85] = goKillGhasts.build()
        steps[90] = goKillGhasts.build()
        steps[95] = goKillGhasts.build()
        steps[100] = goKillGhasts.build()

        val finishOff = ConditionalStep.builder().withDefaultStep(enterGrottoAgain)
        finishOff.addStep(Conditions(inGrotto, natureSpiritNearby), talkToNatureSpiritToFinish)
        finishOff.addStep(inGrotto, touchAltarAgain)
        steps[105] = finishOff.build()

        return steps
    }

    override fun getItemRequirements(): List<ItemRequirement> = listOf(ghostspeak, silverSickle)

    override fun getItemRecommended(): List<ItemRequirement> = listOf(salveTele, combatGear)

    override fun getNotes(): List<String> = listOf(
        "Whilst in Mort Myre, the Ghasts will occasionally rot the food in your inventory."
    )

    override fun getCombatRequirements(): List<String> = listOf(
        "3 Ghasts (level 30)"
    )

    override fun getGeneralRequirements(): List<Requirement> = listOf(
        QuestRequirement.withStatus(Quest.OSRS.THE_RESTLESS_GHOST, Quest.Status.COMPLETE),
        QuestRequirement.withStatus(Quest.OSRS.PRIEST_IN_PERIL, Quest.Status.COMPLETE)
    )

    override fun getQuestPointReward(): QuestPointReward = QuestPointReward(2)

    override fun getExperienceRewards(): List<ExperienceReward> = listOf(
        ExperienceReward(Skill.CRAFTING, 3000),
        ExperienceReward(Skill.DEFENCE, 2000),
        ExperienceReward(Skill.CONSTITUTION, 2000)
    )

    override fun getUnlockRewards(): List<UnlockReward> = listOf(
        UnlockReward("Access to Mort Myre Swamp"),
        UnlockReward("Ability to fight Ghasts.")
    )

    override fun getPanels(): List<PanelDetails> = listOf(
        PanelDetails(
            header = "Start the quest",
            steps = listOf(
                talkToDrezel,
                enterSwamp,
                tryToEnterGrotto,
                talkToFilliman,
                takeWashingBowl,
                takeMirror,
                useMirrorOnFilliman,
                searchGrotto,
                useJournalOnFilliman
            ),
            requirements = listOf(ghostspeak, silverSickle, prayerPoints)
        ),
        PanelDetails(
            header = "Helping Filliman",
            steps = listOf(
                talkToDrezelForBlessing,
                castSpellAndGetMushroom,
                useMushroom,
                useSpellCard,
                standOnOrange,
                tellFillimanToCast,
                enterGrotto,
                searchAltar,
                talkToFillimanInGrotto,
                blessSickle
            ),
            requirements = listOf(ghostspeak, silverSickle)
        ),
        PanelDetails(
            header = "Killing Ghasts",
            steps = listOf(
                fillPouches,
                killGhasts,
                enterGrottoAgain,
                talkToNatureSpiritToFinish
            ),
            requirements = listOf(ghostspeak, blessedSickle, prayerPoints)
        )
    )

    override fun getVar() = Varps.getAt(307).value
}