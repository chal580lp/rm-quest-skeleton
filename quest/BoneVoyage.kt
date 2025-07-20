package com.recursive.quester.quest

import com.recursive.quester.Bot
import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.QuestRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.requirements.VarbitRequirement
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.player.SkillRequirement
import com.recursive.quester.framework.requirements.util.Operation
import com.recursive.quester.framework.requirements.zone.Zone
import com.recursive.quester.framework.requirements.zone.ZoneRequirement
import com.recursive.quester.framework.reward.QuestPointReward
import com.recursive.quester.framework.reward.UnlockReward
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.ConditionalStep
import com.recursive.quester.framework.steps.NpcStep
import com.recursive.quester.framework.steps.ObjectStep
import com.recursive.quester.framework.steps.QuestStep
import com.recursive.quester.framework.steps.interactions.NpcInteraction
import com.recursive.quester.framework.util.getLogger
import com.recursive.quester.runelite.ItemCollections
import com.recursive.quester.runelite.transformed.QuestVarbits
import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.osrs.data.ItemID
import com.runemate.game.api.osrs.data.NpcID
import com.runemate.game.api.osrs.data.ObjectID

class BoneVoyage(private val bot: Bot) : BasicQuestHelper() {
    private val log = getLogger("BoneVoyage")

    // Items Required
    lateinit var vodka2: ItemRequirement
    lateinit var marrentillPotionUnf: ItemRequirement

    // Items Recommended
    lateinit var digsiteTeleport: ItemRequirement
    lateinit var woodcuttingGuildTeleport: ItemRequirement
    lateinit var varrockTeleport: ItemRequirement
    lateinit var sarimTeleport: ItemRequirement
    lateinit var lumberyardTeleport: ItemRequirement
    lateinit var hammer: ItemRequirement
    lateinit var ironBar: ItemRequirement
    lateinit var oakPlanks: ItemRequirement
    lateinit var nails: ItemRequirement

    lateinit var sawmillAgreement: ItemRequirement
    lateinit var boneCharm: ItemRequirement
    lateinit var potionOfSealegs: ItemRequirement
    lateinit var sawmillProposal: ItemRequirement

    // Requirements
    lateinit var canEnterGuild: Requirement
    lateinit var onBoat: Requirement
    lateinit var gottenCharm: Requirement
    lateinit var givenCharm: Requirement
    lateinit var talkedToApoth: Requirement
    lateinit var gottenPotion: Requirement
    lateinit var givenPotion: Requirement

    // Steps
    lateinit var talkToHaig: NpcStep
    lateinit var talkToForeman: NpcStep
    lateinit var talkToSawmillOperator: NpcStep
    lateinit var talkToOperatorInGuild: NpcStep
    lateinit var talkToOperatorInGuildFromGate: ObjectStep
    lateinit var talkToOperatorInGuildGeneric: QuestStep
    lateinit var returnWithAgreement: NpcStep
    lateinit var talkToForemanAgain: NpcStep
    lateinit var boardBarge: NpcStep
    lateinit var talkToNavigator: NpcStep
    lateinit var talkToJack: NpcStep
    lateinit var boardBargeAfterJack: NpcStep
    lateinit var talkToNavigatorAgain: NpcStep
    lateinit var talkToOddOldMan: NpcStep
    lateinit var talkToApoth: NpcStep
    lateinit var talkToApothAgain: NpcStep
    lateinit var boardBargeWithPotionAndCharm: NpcStep
    lateinit var giveLeadPotion: NpcStep
    lateinit var giveJuniorBone: NpcStep
    lateinit var boardBargeWithCharm: NpcStep
    lateinit var boardBargeWithPotion: NpcStep
    lateinit var boardBargeToSail: NpcStep
    lateinit var navigateShip: NpcStep

    // Zones
    lateinit var boat: Zone
    lateinit var boatSailing: Zone

    override fun setupRequirements() {
        canEnterGuild = Conditions(
            SkillRequirement(Skill.WOODCUTTING, 60)
        )

        vodka2 = ItemRequirement("Vodka", ItemID.VODKA, 2)
        marrentillPotionUnf = ItemRequirement("Marrentill potion (unf)", ItemID.MARRENTILL_POTION_UNF)

        digsiteTeleport = ItemRequirement("Teleports to the Digsite", ItemID.DIGSITE_PENDANT_4).apply {
            addAlternates(ItemID.DIGSITE_PENDANT_5, ItemID.DIGSITE_TELEPORT)
        }
        woodcuttingGuildTeleport =
            ItemRequirement("Teleport to the Woodcutting Guild", ItemCollections.SKILLS_NECKLACES).apply {
                addAlternates(ItemID.XERICS_TALISMAN, ItemID.KHAREDSTS_MEMOIRS)
            }
        varrockTeleport = ItemRequirement("Varrock teleport", ItemID.VARROCK_TELEPORT)
        sarimTeleport = ItemRequirement("Port Sarim teleport", ItemCollections.AMULET_OF_GLORIES).apply {
            addAlternates(ItemID.DRAYNOR_MANOR_TELEPORT)
        }
        lumberyardTeleport = ItemRequirement("Lumberyard teleport", ItemID.LUMBERYARD_TELEPORT)

        hammer = ItemRequirement("Hammer", ItemCollections.HAMMER)
        ironBar = ItemRequirement("Iron bar", ItemID.IRON_BAR)
        oakPlanks = ItemRequirement("Oak plank", ItemID.OAK_PLANK, 2)
        nails = ItemRequirement("Nails", ItemCollections.NAILS, 5)

        sawmillProposal = ItemRequirement("Sawmill proposal", ItemID.SAWMILL_PROPOSAL).apply {
            setTooltip("You can get another from the sawmill operator near Varrock")
        }

        sawmillAgreement = ItemRequirement("Sawmill agreement", ItemID.SAWMILL_AGREEMENT).apply {
            setTooltip(
                if (canEnterGuild.check()) {
                    "You can get another from the sawmill operator in the Woodcutting Guild"
                } else {
                    "You can get another by trying to enter the Woodcutting Guild"
                }
            )
        }

        boneCharm = ItemRequirement("Bone charm", ItemID.BONE_CHARM).apply {
            setTooltip("You can get another from the Odd Old Man north of the Dig Site")
        }

        potionOfSealegs = ItemRequirement("Potion of sealegs", ItemID.POTION_OF_SEALEGS).apply {
            setTooltip("You can get another from the Apothecary in Varrock")
        }

        log.debug("Requirements set up")
    }

    override fun setupZones() {
        boat = Zone(Coordinate(3355, 3451, 1), Coordinate(3366, 3455, 1))
        boatSailing = Zone(Coordinate(1812, 4750, 0), Coordinate(1840, 4774, 2))
    }

    private fun setupConditions() {
        onBoat = ZoneRequirement(boat, boatSailing)
        gottenCharm = VarbitRequirement(5796, 1, Operation.GREATER_EQUAL)
        givenCharm = VarbitRequirement(5796, 2, Operation.GREATER_EQUAL)
        talkedToApoth = VarbitRequirement(5797, 1, Operation.GREATER_EQUAL)
        gottenPotion = VarbitRequirement(5797, 2, Operation.GREATER_EQUAL)
        givenPotion = VarbitRequirement(5797, 3, Operation.GREATER_EQUAL)
    }

    private fun setupSteps() {
        setupConditions()

        talkToHaig = NpcStep.builder()
            .forNpc(NpcID.CURATOR_HAIG_HALEN)
            .at(3257, 3448, 0)
            .withText("Talk to Curator Haig in the Varrock Museum.")
            .withDialogSteps("Have you any interesting news?", "Sign me up!", "Yes.")
            .build()

        talkToForeman = NpcStep.builder()
            .forNpc(NpcID.BARGE_FOREMAN)
            .at(3364, 3445, 0)
            .withText("Talk to the Barge Foreman north of the Dig Site.")
            .build()

        talkToSawmillOperator = NpcStep.builder()
            .forNpc(NpcID.SAWMILL_OPERATOR)
            .at(3302, 3492, 0)
            .withText("Talk to the Sawmill Operator north east of Varrock.")
            .withDialogStep("I'm here on behalf of the museum archaeological team.")
            .build()

        talkToOperatorInGuild = NpcStep.builder()
            .forNpc(NpcID.SAWMILL_OPERATOR_9140)
            .at(1620, 3499, 0)
            .withText("Talk to the Sawmill Operator in the Woodcutting Guild on Zeah.")
            .requiring(sawmillProposal)
            .withDialogStep("I'm here on behalf of the museum archaeological team.")
            .build()

        talkToOperatorInGuildFromGate = ObjectStep.builder()
            .forObject(ObjectID.GATE_28852)
            .at(1657, 3504, 0)
            .withText("Attempt to enter the Woodcutting Guild on Zeah.")
            .requiring(sawmillProposal)
            .withAction("Open")
            .withDialogStep("I'm here on behalf of the museum archaeological team.")
            .build()

        talkToOperatorInGuildGeneric = if (canEnterGuild.check()) {
            talkToOperatorInGuild
        } else {
            talkToOperatorInGuildFromGate
        }

        returnWithAgreement = NpcStep.builder()
            .forNpc(NpcID.SAWMILL_OPERATOR)
            .at(3302, 3492, 0)
            .withText("Return to the Sawmill Operator north east of Varrock.")
            .requiring(sawmillAgreement)
            .withDialogStep("I'm here on behalf of the museum archaeological team.")
            .build()

        talkToForemanAgain = NpcStep.builder()
            .forNpc(NpcID.BARGE_FOREMAN)
            .at(3364, 3445, 0)
            .withText("Return to the Barge Foreman north of the Dig Site.")
            .build()

        boardBarge = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Board the barge.")
            .withDialogStep("Can I go onto the barge?")
            .withInteraction(NpcInteraction.talkTo("Board"))
            .build()

        talkToNavigator = NpcStep.builder()
            .forNpc("Lead Navigator")
            .at(3363, 3453, 1)
            .withText("Talk to the Lead Navigator.")
            .withDialogSteps("Yep, that would be me.", "No, what happened?")
            .build()

        talkToJack = NpcStep.builder()
            .forNpc(NpcID.JACK_SEAGULL)
            .at(3050, 3257, 0)
            .withText("Talk to Jack Seagull in the Port Sarim Pub.")
            .withDialogStep("Ever made any cursed voyages?")
            .build()

        boardBargeAfterJack = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Return to the barge.")
            .withDialogStep("Can I go onto the barge?")
            .build()

        talkToNavigatorAgain = NpcStep.builder()
            .forNpc("Lead Navigator")
            .at(3363, 3453, 1)
            .withText("Talk to the Lead Navigator again.")
            .build()

        talkToOddOldMan = NpcStep.builder()
            .forNpc(NpcID.ODD_OLD_MAN)
            .at(3360, 3505, 0)
            .withText("Talk to the Odd Old Man north of the Dig Site.")
            .withDialogSteps("Talk about lucky charms.", "I'm making a cursed voyage.")
            .build()

        talkToApoth = NpcStep.builder()
            .forNpc(NpcID.APOTHECARY)
            .at(3195, 3405, 0)
            .withText("Talk to the Apothecary in south west Varrock.")
            .requiring(marrentillPotionUnf, vodka2)
            .withDialogSteps("Talk about something else.", "Talk about Bone Voyage.")
            .build()

        talkToApothAgain = NpcStep.builder()
            .forNpc(NpcID.APOTHECARY)
            .at(3195, 3405, 0)
            .withText("Talk to the Apothecary again.")
            .requiring(marrentillPotionUnf, vodka2)
            .withDialogSteps("Talk about something else.", "Talk about Bone Voyage.")
            .build()

        boardBargeWithPotionAndCharm = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Board the barge.")
            .requiring(boneCharm, potionOfSealegs)
            .withDialogStep("Can I go onto the barge?")
            .build()

        boardBargeWithPotion = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Board the barge.")
            .requiring(potionOfSealegs)
            .withDialogStep("Can I go onto the barge?")
            .build()

        boardBargeWithCharm = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Board the barge.")
            .requiring(boneCharm)
            .withDialogStep("Can I go onto the barge?")
            .build()

        giveLeadPotion = NpcStep.builder()
            .forNpc("Lead Navigator")
            .at(3363, 3453, 1)
            .withText("Give the Lead Navigator the potion.")
            .requiring(potionOfSealegs)
            .build()

        giveJuniorBone = NpcStep.builder()
            .forNpc("Junior Navigator")
            .at(3363, 3453, 1)
            .withText("Give the Junior Navigator the bone charm.")
            .requiring(boneCharm)
            .build()

        boardBargeToSail = NpcStep.builder()
            .forNpc("Barge guard")
            .at(3362, 3446, 0)
            .withText("Board the barge.")
            .withDialogStep("Can I go onto the barge?")
            .build()

        navigateShip = NpcStep.builder()
            .forNpc("Lead Navigator")
            .at(3363, 3453, 1)
            .withText("Talk to the Lead Navigator to set sail. Navigate the ship by keeping the arrow in the middle. Raise the sails to go faster.")
            .withDialogStep("I'm ready, let's go.")
            .build()

        navigateShip.addSubSteps(boardBargeToSail)
        boardBargeWithPotionAndCharm.addSubSteps(boardBargeWithCharm, boardBargeWithPotion)

        log.debug("Steps set up")
    }

    override fun loadSteps(): Map<Int, QuestStep> {
        setupRequirements()
        setupZones()
        setupConditions()
        setupSteps()

        val steps = mutableMapOf<Int, QuestStep>()

        steps[0] = talkToHaig
        steps[5] = talkToForeman
        steps[10] = talkToSawmillOperator

        val goTalkToOperatorInGuild = ConditionalStep.builder().withDefaultStep(talkToOperatorInGuildFromGate)
        goTalkToOperatorInGuild.addStep(canEnterGuild, talkToOperatorInGuild)
        steps[11] = goTalkToOperatorInGuild.build()

        steps[15] = returnWithAgreement
        steps[20] = talkToForemanAgain

        val goTalkToLead = ConditionalStep.builder().withDefaultStep(boardBarge)
        goTalkToLead.addStep(onBoat, talkToNavigator)
        steps[21] = goTalkToLead.build()

        steps[22] = talkToJack

        val goReturnToLead = ConditionalStep.builder().withDefaultStep(boardBargeAfterJack)
        goReturnToLead.addStep(onBoat, talkToNavigatorAgain)
        steps[23] = goReturnToLead.build()

        val goGetItems = ConditionalStep.builder().withDefaultStep(talkToOddOldMan)
        goGetItems.addStep(Conditions(onBoat, givenCharm, gottenPotion), giveLeadPotion)
        goGetItems.addStep(Conditions(onBoat, gottenCharm, gottenPotion), giveJuniorBone)
        goGetItems.addStep(Conditions(gottenCharm, givenPotion), boardBargeWithCharm)
        goGetItems.addStep(Conditions(givenCharm, gottenPotion), boardBargeWithPotion)
        goGetItems.addStep(Conditions(gottenCharm, gottenPotion), boardBargeWithPotionAndCharm)
        goGetItems.addStep(Conditions(gottenCharm, talkedToApoth), talkToApothAgain)
        goGetItems.addStep(gottenCharm, talkToApoth)
        steps[25] = goGetItems.build()

        val goSail = ConditionalStep.builder().withDefaultStep(boardBargeToSail)
        goSail.addStep(onBoat, navigateShip)
        steps[30] = goSail.build()
        steps[35] = goSail.build()

        return steps
    }

    override fun getItemRequirements(): List<ItemRequirement> = listOf(vodka2, marrentillPotionUnf)

    override fun getItemRecommended(): List<ItemRequirement> = listOf(
        digsiteTeleport.quantity(4),
        woodcuttingGuildTeleport,
        varrockTeleport,
        sarimTeleport,
        lumberyardTeleport.quantity(2),
        hammer,
        ironBar,
        oakPlanks,
        nails
    )

    override fun getGeneralRequirements(): List<Requirement> = listOf(
        VarbitRequirement(3637, 100, Operation.GREATER_EQUAL, "100 Kudos"),
        QuestRequirement.withStatus(Quest.OSRS.THE_DIG_SITE, Quest.Status.COMPLETE)
    )

    override fun getQuestPointReward(): QuestPointReward = QuestPointReward(1)

    override fun getUnlockRewards(): List<UnlockReward> = listOf(
        UnlockReward("Access to Fossil Island")
    )

    override fun getPanels(): List<PanelDetails> = listOf(
        PanelDetails(
            header = "Starting off",
            steps = listOf(talkToHaig)
        ),
        PanelDetails(
            header = "Securing materials",
            steps = listOf(
                talkToForeman,
                talkToSawmillOperator,
                talkToOperatorInGuildGeneric,
                returnWithAgreement,
                talkToForemanAgain
            )
        ),
        PanelDetails(
            header = "Lucky charms",
            steps = listOf(
                boardBarge,
                talkToNavigator,
                talkToJack,
                boardBargeAfterJack,
                talkToNavigatorAgain,
                talkToOddOldMan,
                talkToApoth,
                talkToApothAgain,
                boardBargeWithPotionAndCharm,
                giveJuniorBone,
                giveLeadPotion
            ),
            requirements = listOf(marrentillPotionUnf, vodka2)
        ),
        PanelDetails(
            header = "The voyage",
            steps = listOf(navigateShip)
        )
    )

    override fun getNotes(): List<String> = listOf(
        "After quest completion you can use the iron bar, oak planks, nails and hammer to build a bank chest on " +
                "Fossil Island (requires 21 Construction)."
    )

    override fun getVar(): Int = Varbits.load(QuestVarbits.QUEST_BONE_VOYAGE.id)?.value ?: 0
}