package com.recursive.quester.quest


import ObjectCondition
import com.recursive.quester.Bot
import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.ItemOnTileRequirement
import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.requirements.VarbitRequirement
import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.player.SkillRequirement
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.requirements.zone.Zone
import com.recursive.quester.framework.requirements.zone.ZoneRequirement
import com.recursive.quester.framework.reward.ExperienceReward
import com.recursive.quester.framework.reward.QuestPointReward
import com.recursive.quester.framework.reward.UnlockReward
import com.recursive.quester.framework.skeleton.BasicQuestHelper
import com.recursive.quester.framework.steps.*
import com.recursive.quester.framework.util.getLogger
import com.recursive.quester.runelite.ItemCollections
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.osrs.data.ItemID
import com.runemate.game.api.osrs.data.NpcID
import com.runemate.game.api.osrs.data.ObjectID
import java.util.*


class EaglesPeak(private val bot: Bot) : BasicQuestHelper() {
    private val log = getLogger("EaglesPeak")


    lateinit var coins: ItemRequirement
    lateinit var yellowDye: ItemRequirement
    lateinit var tar: ItemRequirement
    lateinit var birdBook: ItemRequirement
    lateinit var metalFeather: ItemRequirement
    lateinit var metalFeatherHighlighted: ItemRequirement
    lateinit var tenEagleFeathers: ItemRequirement
    lateinit var fakeBeak: ItemRequirement
    lateinit var eagleCape: ItemRequirement
    lateinit var bronzeFeather: ItemRequirement
    lateinit var silverFeather: ItemRequirement
    lateinit var goldFeather: ItemRequirement
    lateinit var bronzeFeatherHighlighted: ItemRequirement
    lateinit var silverFeatherHighlighted: ItemRequirement
    lateinit var goldFeatherHighlighted: ItemRequirement
    lateinit var birdFeed6: ItemRequirement
    lateinit var birdFeed: ItemRequirement
    lateinit var ferret: ItemRequirement


    lateinit var eaglesPeakTeleport: ItemRequirement
    lateinit var varrockTeleport: ItemRequirement
    lateinit var ardougneTeleport: ItemRequirement


    lateinit var inMainCave: Zone
    lateinit var inSilverRoomZone: Zone
    lateinit var inGoldRoomZone1: Zone
    lateinit var inGoldRoomZone2: Zone
    lateinit var inNest: Zone


    lateinit var speakToCharlie: NpcStep
    lateinit var inspectBooks: ObjectStep
    lateinit var clickBook: DetailedQuestStep
    lateinit var inspectBooksForFeather: ObjectStep
    lateinit var useFeatherOnDoor: ObjectStep
    lateinit var enterPeak: ObjectStep
    lateinit var shoutAtNickolaus: NpcStep
    lateinit var pickupFeathers: ObjectStep
    lateinit var goToFancyStore: NpcStep
    lateinit var speakAsyffAgain: NpcStep
    lateinit var returnToEaglesPeak: ObjectStep
    lateinit var enterEastCave: ObjectStep
    lateinit var enterBronzeRoom: ObjectStep
    lateinit var attemptToTakeBronzeFeather: ObjectStep
    lateinit var winch1: ObjectStep
    lateinit var winch2: ObjectStep
    lateinit var winch3: ObjectStep
    lateinit var winch4: ObjectStep
    lateinit var grabBronzeFeather: ObjectStep
    lateinit var enterMainCavernFromBronze: ObjectStep
    lateinit var enterSilverRoom: ObjectStep
    lateinit var inspectSilverPedestal: ObjectStep
    lateinit var enterMainCavernFromSilver: ObjectStep
    lateinit var enterGoldRoom: ObjectStep
    lateinit var inspectRocks1: ObjectStep
    lateinit var inspectRocks2: ObjectStep
    lateinit var inspectOpening: ObjectStep
    lateinit var threatenKebbit: NpcStep
    lateinit var pickupSilverFeather: ObjectStep
    lateinit var collectFeed: ObjectStep
    lateinit var pullLever1Down: ObjectStep
    lateinit var pushLever1Up: ObjectStep
    lateinit var pullLever2Down: ObjectStep
    lateinit var pullLever3Down: ObjectStep
    lateinit var pullLever4Down: ObjectStep
    lateinit var fillFeeder1: ObjectStep
    lateinit var fillFeeder2: ObjectStep
    lateinit var fillFeeder3: ObjectStep
    lateinit var fillFeeder4: ObjectStep
    lateinit var fillFeeder5: ObjectStep
    lateinit var fillFeeder6: ObjectStep
    lateinit var fillFeeder4Again: ObjectStep
    lateinit var fillFeeder7: ObjectStep
    lateinit var grabGoldFeather: ObjectStep
    lateinit var enterMainCavernFromGold: ObjectStep
    lateinit var useFeathersOnStoneDoor: ObjectStep
    lateinit var useSilverFeathersOnStoneDoor: ObjectStep
    lateinit var useBronzeFeathersOnStoneDoor: ObjectStep
    lateinit var useGoldFeathersOnStoneDoor: ObjectStep
    lateinit var useGoldBronzeFeathersOnStoneDoor: ObjectStep
    lateinit var useGoldSilverFeathersOnStoneDoor: ObjectStep
    lateinit var useBronzeSilverFeathersOnStoneDoor: ObjectStep
    lateinit var sneakPastEagle: NpcStep
    lateinit var speakToNickolaus: NpcStep
    lateinit var speakToNickolausInTheCamp: NpcStep
    lateinit var speakToCharlieAgain: NpcStep
    lateinit var pickUpActualSilverFeather: ObjectStep
    lateinit var leavePeak: ObjectStep
    lateinit var speakToAsyffToComplete: NpcStep


    lateinit var inMainCavern: ZoneRequirement
    lateinit var spokenToNickolaus: VarbitRequirement
    lateinit var spokenOnceToAsyff: VarbitRequirement
    lateinit var spokenTwiceToAsyff: VarbitRequirement
    lateinit var inBronzeRoom: ObjectCondition
    lateinit var bronzeRoomPedestalUp: ObjectCondition
    lateinit var bronzeRoomPedestalLowered: ObjectCondition
    lateinit var winch1NotDone: VarbitRequirement
    lateinit var winch2NotDone: VarbitRequirement
    lateinit var winch3NotDone: VarbitRequirement
    lateinit var winch4NotDone: VarbitRequirement
    lateinit var hasSolvedBronze: VarbitRequirement
    lateinit var hasInsertedGoldFeather: VarbitRequirement
    lateinit var hasInsertedSilverFeather: VarbitRequirement
    lateinit var hasInsertedBronzeFeather: VarbitRequirement
    lateinit var inSilverRoom: ZoneRequirement
    lateinit var inGoldRoom: ZoneRequirement
    lateinit var lever1OriginalPosition: VarbitRequirement
    lateinit var lever1Pulled: VarbitRequirement
    lateinit var lever2Pulled: VarbitRequirement
    lateinit var lever3Pulled: VarbitRequirement
    lateinit var lever4Pulled: VarbitRequirement
    lateinit var bird1Moved: VarbitRequirement
    lateinit var bird2Moved: VarbitRequirement
    lateinit var bird3Moved: VarbitRequirement
    lateinit var bird4Moved: VarbitRequirement
    lateinit var bird5Moved: VarbitRequirement
    lateinit var hasInspectedSilverPedestal: VarbitRequirement
    lateinit var hasInspectedRocks1: VarbitRequirement
    lateinit var hasInspectedRocks2: VarbitRequirement
    lateinit var hasInspectedOpening: VarbitRequirement
    lateinit var threatenedKebbit: VarbitRequirement
    lateinit var silverFeatherNearby: ItemOnTileRequirement


    override fun setupRequirements() {
        yellowDye = ItemRequirement("Yellow dye", ItemID.YELLOW_DYE)
        coins = ItemRequirement("Coins", ItemID.COINS_995, 50)
        tar = ItemRequirement("Swamp tar", ItemID.SWAMP_TAR)

        //Obtainable during quest items
        birdBook = ItemRequirement("Bird book", ItemID.BIRD_BOOK).apply {
            setHighlightInInventory(true)
        }
        metalFeatherHighlighted = ItemRequirement("Metal feather", ItemID.METAL_FEATHER).apply {
            setHighlightInInventory(true)
            setTooltip("You can get another Metal Feather by searching the books in the camp north of Eagles' Peak.")
        }
        metalFeather = ItemRequirement("Metal feather", ItemID.METAL_FEATHER).apply {
            setTooltip("You can get another Metal Feather by searching the books in the camp north of Eagles' Peak.")
        }
        tenEagleFeathers = ItemRequirement("Eagle feather", ItemID.EAGLE_FEATHER, 10)
        fakeBeak = ItemRequirement("Fake beak", ItemID.FAKE_BEAK, 2).apply {
            setTooltip("If you lose one of your beaks you'll need to have Azyff make you a new one.")
        }
        eagleCape = ItemRequirement("Eagle cape", ItemID.EAGLE_CAPE, 2).apply {
            setTooltip("If you lose one of your capes you'll need to have Azyff make you a new one.")
        }
        bronzeFeather = ItemRequirement("Bronze feather", ItemID.BRONZE_FEATHER)
        silverFeather = ItemRequirement("Silver feather", ItemID.SILVER_FEATHER)
        goldFeather = ItemRequirement("Golden feather", ItemID.GOLDEN_FEATHER_10175)

        bronzeFeatherHighlighted = ItemRequirement("Bronze feather", ItemID.BRONZE_FEATHER).apply {
            setHighlightInInventory(true)
        }
        silverFeatherHighlighted = ItemRequirement("Silver feather", ItemID.SILVER_FEATHER).apply {
            setHighlightInInventory(true)
        }
        goldFeatherHighlighted = ItemRequirement("Golden feather", ItemID.GOLDEN_FEATHER_10175).apply {
            setHighlightInInventory(true)
        }

        //Teleport items
        varrockTeleport = ItemRequirement("Varrock teleport", ItemID.VARROCK_TELEPORT)
        ardougneTeleport = ItemRequirement("Ardougne teleport", ItemID.ARDOUGNE_TELEPORT).quantity(2)
        eaglesPeakTeleport = ItemRequirement(
            "Teleport to Eagle's Peak. Fairy ring (AKQ), Necklace of passage (The Outpost [2])",
            ItemCollections.FAIRY_STAFF
        ).apply {
            isNotConsumed()
            addAlternates(ItemCollections.NECKLACE_OF_PASSAGES)
            quantity(2)
        }

        birdFeed6 = ItemRequirement("Odd bird seed", ItemID.ODD_BIRD_SEED, 6)
        birdFeed = ItemRequirement("Odd bird seed", ItemID.ODD_BIRD_SEED).apply {
            setHighlightInInventory(true)
        }
        ferret = ItemRequirement("Ferret", ItemID.FERRET).apply {
            setTooltip("If you lose your ferret you'll need to catch a new one with a box trap north of Eagles' Peak.")
        }
        log.debug("Requirements set up")
    }

    override fun setupZones() {
        inMainCave = Zone(Coordinate(1983, 4940, 3), Coordinate(2035, 4987, 3))
        inSilverRoomZone = Zone(Coordinate(1925, 4863, 2), Coordinate(1976, 4884, 2))
        inGoldRoomZone1 = Zone(Coordinate(1924, 4890, 2), Coordinate(1959, 4921, 2))
        inGoldRoomZone2 = Zone(Coordinate(1959, 4890, 2), Coordinate(1985, 4901, 2))
        inNest = Zone(Coordinate(2002, 4956, 3), Coordinate(2010, 4962, 3))
        log.debug("Zones set up")
    }

    override fun loadSteps(): Map<Int, QuestStep> {
        setupRequirements()
        setupZones()
        setupConditions()
        setupSteps()

        val steps = mutableMapOf<Int, QuestStep>()

        // Starting quest
        steps[0] = speakToCharlie

        // Getting feather key
        val getFeatherKey = ConditionalStep.builder().withDefaultStep(inspectBooks)
        getFeatherKey.addStep(inMainCavern, clickBook)
        steps[5] = getFeatherKey.build()

        // Entering Eagles' Peak
        val enterEaglesPeak = ConditionalStep.builder().withDefaultStep(inspectBooksForFeather)
        enterEaglesPeak.addStep(metalFeather, useFeatherOnDoor)
        steps[10] = enterEaglesPeak.build()

        // Creating disguises
        val createDisguises = ConditionalStep.builder().withDefaultStep(enterPeak)

        val hasGoldFeatherOrUsed = Conditions(LogicType.OR, goldFeather, hasInsertedGoldFeather)
        val hasSilverFeatherOrUsed = Conditions(LogicType.OR, silverFeather, hasInsertedSilverFeather)
        val hasBronzeFeatherOrUsed = Conditions(LogicType.OR, bronzeFeather, hasInsertedBronzeFeather)

        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasInsertedGoldFeather,
                hasInsertedBronzeFeather,
                hasSilverFeatherOrUsed
            ), useSilverFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasInsertedGoldFeather,
                hasBronzeFeatherOrUsed,
                hasInsertedSilverFeather
            ), useBronzeFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasGoldFeatherOrUsed,
                hasInsertedBronzeFeather,
                hasInsertedSilverFeather
            ), useGoldFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasGoldFeatherOrUsed,
                hasBronzeFeatherOrUsed,
                hasInsertedSilverFeather
            ), useGoldBronzeFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasGoldFeatherOrUsed,
                hasInsertedBronzeFeather,
                hasSilverFeatherOrUsed
            ), useGoldSilverFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasInsertedGoldFeather,
                hasBronzeFeatherOrUsed,
                hasSilverFeatherOrUsed
            ), useBronzeSilverFeathersOnStoneDoor
        )
        createDisguises.addStep(
            Conditions(
                inMainCavern,
                hasGoldFeatherOrUsed,
                hasBronzeFeatherOrUsed,
                hasSilverFeatherOrUsed
            ), useFeathersOnStoneDoor
        )
        createDisguises.addStep(Conditions(inGoldRoom, hasGoldFeatherOrUsed), enterMainCavernFromGold)
        createDisguises.addStep(Conditions(inGoldRoom, bird5Moved), grabGoldFeather)
        createDisguises.addStep(Conditions(inGoldRoom, lever3Pulled, lever4Pulled, bird3Moved), fillFeeder6)
        createDisguises.addStep(Conditions(inGoldRoom, lever3Pulled, lever4Pulled), fillFeeder4Again)
        createDisguises.addStep(Conditions(inGoldRoom, lever4Pulled), pullLever3Down)
        createDisguises.addStep(
            Conditions(
                inGoldRoom,
                lever1OriginalPosition,
                lever2Pulled,
                lever3Pulled,
                bird4Moved
            ), pullLever4Down
        )
        createDisguises.addStep(
            Conditions(inGoldRoom, lever1OriginalPosition, lever2Pulled, lever3Pulled),
            fillFeeder5
        )
        createDisguises.addStep(
            Conditions(inGoldRoom, lever1OriginalPosition, lever2Pulled, bird3Moved),
            pullLever3Down
        )
        createDisguises.addStep(Conditions(inGoldRoom, lever1OriginalPosition, lever2Pulled), fillFeeder4)
        createDisguises.addStep(
            Conditions(inGoldRoom, lever1Pulled, lever2Pulled, bird4Moved),
            fillFeeder7
        ) // If you've blocked lever 1
        createDisguises.addStep(Conditions(inGoldRoom, lever1Pulled, lever2Pulled), pushLever1Up)
        createDisguises.addStep(Conditions(inGoldRoom, lever1Pulled, bird1Moved, bird2Moved), pullLever2Down)
        createDisguises.addStep(Conditions(inGoldRoom, lever1Pulled, bird2Moved), fillFeeder3)
        createDisguises.addStep(Conditions(inGoldRoom, lever1Pulled, bird1Moved), fillFeeder2)
        createDisguises.addStep(Conditions(inGoldRoom, lever1Pulled), fillFeeder1)
        createDisguises.addStep(Conditions(inGoldRoom, birdFeed6), pullLever1Down)
        createDisguises.addStep(Conditions(inGoldRoom), collectFeed)
        createDisguises.addStep(
            Conditions(inMainCavern, hasSilverFeatherOrUsed, hasBronzeFeatherOrUsed),
            enterGoldRoom
        )
        createDisguises.addStep(Conditions(inSilverRoom, hasSilverFeatherOrUsed), enterMainCavernFromSilver)
        createDisguises.addStep(Conditions(inSilverRoom, silverFeatherNearby), pickUpActualSilverFeather)
        createDisguises.addStep(Conditions(inSilverRoom, threatenedKebbit), pickupSilverFeather)
        createDisguises.addStep(Conditions(inSilverRoom, hasInspectedOpening), threatenKebbit)
        createDisguises.addStep(Conditions(inSilverRoom, hasInspectedRocks2), inspectOpening)
        createDisguises.addStep(Conditions(inSilverRoom, hasInspectedRocks1), inspectRocks2)
        createDisguises.addStep(Conditions(inSilverRoom, hasInspectedSilverPedestal), inspectRocks1)
        createDisguises.addStep(Conditions(inSilverRoom), inspectSilverPedestal)
        createDisguises.addStep(Conditions(inMainCavern, hasBronzeFeatherOrUsed), enterSilverRoom)
        createDisguises.addStep(
            Conditions(bronzeRoomPedestalLowered, hasBronzeFeatherOrUsed),
            enterMainCavernFromBronze
        )
        createDisguises.addStep(Conditions(hasSolvedBronze, bronzeRoomPedestalLowered), grabBronzeFeather)
        createDisguises.addStep(Conditions(bronzeRoomPedestalUp, winch4NotDone), winch4)
        createDisguises.addStep(Conditions(bronzeRoomPedestalUp, winch3NotDone), winch3)
        createDisguises.addStep(Conditions(bronzeRoomPedestalUp, winch2NotDone), winch2)
        createDisguises.addStep(Conditions(bronzeRoomPedestalUp, winch1NotDone), winch1)
        createDisguises.addStep(Conditions(inBronzeRoom, spokenTwiceToAsyff), attemptToTakeBronzeFeather)
        createDisguises.addStep(Conditions(ZoneRequirement(inMainCave), spokenTwiceToAsyff), enterBronzeRoom)
        createDisguises.addStep(Conditions(spokenTwiceToAsyff), returnToEaglesPeak)
        createDisguises.addStep(Conditions(spokenOnceToAsyff, tenEagleFeathers), speakAsyffAgain)
        createDisguises.addStep(Conditions(spokenToNickolaus, tenEagleFeathers), goToFancyStore)
        createDisguises.addStep(Conditions(spokenToNickolaus, inMainCavern), pickupFeathers)
        createDisguises.addStep(inMainCavern, shoutAtNickolaus)

        steps[15] = createDisguises.build()

        // Freeing Nickolaus
        val freeNickolaus = ConditionalStep.builder().withDefaultStep(enterPeak)
        freeNickolaus.addStep(Conditions(ZoneRequirement(inNest)), speakToNickolaus)
        freeNickolaus.addStep(Conditions(inMainCavern), sneakPastEagle)
        steps[20] = freeNickolaus.build()

        // Talking to Nick in Camp
        val goTalkToNeckInCamp = ConditionalStep.builder().withDefaultStep(speakToNickolausInTheCamp)
        goTalkToNeckInCamp.addStep(inMainCavern, leavePeak)
        steps[25] = goTalkToNeckInCamp.build()

        // Finish quest
        steps[30] = goTalkToNeckInCamp.build()
        steps[35] = speakToCharlieAgain

        return steps
    }

    private fun setupConditions() {
        inBronzeRoom = ObjectCondition(ObjectID.PEDESTAL_19980)
        bronzeRoomPedestalUp = ObjectCondition(ObjectID.PEDESTAL_19981)
        bronzeRoomPedestalLowered = ObjectCondition(ObjectID.STONE_PEDESTAL_19984)
        inMainCavern = ZoneRequirement(inMainCave)
        spokenToNickolaus = VarbitRequirement(3110, 3)
        spokenOnceToAsyff = VarbitRequirement(3110, 4)
        spokenTwiceToAsyff = VarbitRequirement(3110, 5)
        winch1NotDone = VarbitRequirement(3101, 0)
        winch2NotDone = VarbitRequirement(3102, 0)
        winch3NotDone = VarbitRequirement(3103, 0)
        winch4NotDone = VarbitRequirement(3104, 0)
        hasSolvedBronze = VarbitRequirement(3105, 0)
        hasInspectedSilverPedestal = VarbitRequirement(3099, 1)
        hasInspectedRocks1 = VarbitRequirement(3099, 2)
        hasInspectedRocks2 = VarbitRequirement(3099, 3)
        hasInspectedOpening = VarbitRequirement(3099, 4)
        threatenedKebbit = VarbitRequirement(3099, 5)
        inSilverRoom = ZoneRequirement(inSilverRoomZone)
        inGoldRoom = ZoneRequirement(inGoldRoomZone1, inGoldRoomZone2)
        lever1OriginalPosition = VarbitRequirement(3092, 0)
        lever1Pulled = VarbitRequirement(3092, 1)
        lever2Pulled = VarbitRequirement(3093, 1)
        lever3Pulled = VarbitRequirement(3090, 1)
        lever4Pulled = VarbitRequirement(3091, 1)
        bird1Moved = VarbitRequirement(3098, 1)
        bird2Moved = VarbitRequirement(3097, 1)
        bird3Moved = VarbitRequirement(3095, 1)
        bird4Moved = VarbitRequirement(3094, 1)
        bird5Moved = VarbitRequirement(3096, 1)
        hasInsertedBronzeFeather = VarbitRequirement(3108, 1)
        hasInsertedSilverFeather = VarbitRequirement(3099, 6)
        hasInsertedGoldFeather = VarbitRequirement(3107, 1)

        silverFeatherNearby = ItemOnTileRequirement(silverFeather)
        log.debug("Conditions set up")
    }

    private fun setupSteps() {
        setupConditions()

        speakToCharlie = NpcStep.builder()
            .forNpc(NpcID.CHARLIE_1495)
            .at(2607, 3264, 0)
            .withText("Speak to Charlie in the Ardougne Zoo.")
            .withDialogSteps(
                "Ah, you sound like someone who needs a quest doing!",
                "Sure. Any idea where I should start looking?",
                "Yes."
            )
            .withTeleport(ardougneTeleport)
            .build()

        inspectBooks = ObjectStep.builder()
            .forObject(NULL_19787)
            .at(2319, 3506, 0)
            .withText(
                "Go to the camp north of Eagles' Peak and search the pile of books for a Bird Book. " +
                        "The closest fairy ring is AKQ or teleport to The Outpost using the Necklace of Passage."
            )
            .withTeleport(eaglesPeakTeleport)
            .withDialogStep("The Outpost")
            .build()

        clickBook = DetailedQuestStep.builder()
            .withText("Click the Bird Book for a Metal Feather.")
            .requiring(birdBook)
            .build()

        inspectBooksForFeather = ObjectStep.builder()
            .forObject(BOOKS_19886)
            .at(2319, 3506, 0)
            .withText("Go to the camp north of Eagles' Peak and search the pile of books to get the Metal Feather back.")
            .build()

        useFeatherOnDoor = ObjectStep.builder()
            .forObject(NULL_19790)
            .at(2329, 3495, 0)
            .withText("Use the Metal Feather on the Rocky Outcrop on Eagles' Peak.")
            .requiring(metalFeatherHighlighted)
            .build()

        enterPeak = ObjectStep.builder()
            .forObject(NULL_19790)
            .at(2329, 3495, 0)
            .withText("Enter Eagles' Peak through the Rocky Outcrop.")
            .build()

        shoutAtNickolaus = NpcStep.builder()
            .forNpc(NpcID.NICKOLAUS_1484)
            .at(2006, 4960, 3)
            .withText("Shout to Nickolaus from across the chasm.")
            .withDialogSteps(
                "The Ardougne zookeeper sent me to find you.",
                "Well if you gave me a ferret I could take it back for you.",
                "Could I help at all?"
            )
            .build()

        pickupFeathers = ObjectStep.builder()
            .forObject(GIANT_FEATHERS)
            .at(2005, 4972, 3)
            .withText("Pick up 10 Eagle feathers from the piles in the main cavern.")
            .requiring(tenEagleFeathers)
            .build()

        goToFancyStore = NpcStep.builder()
            .forNpc(NpcID.ASYFF)
            .at(3281, 3398, 0)
            .withText("Go speak to Asyff in south-east Varrock to have a disguise made.")
            .requiring(yellowDye, coins, tar, tenEagleFeathers)
            .withDialogStep("Well, specifically I'm after a couple of bird costumes.")
            .withTeleport(varrockTeleport)
            .build()

        speakAsyffAgain = NpcStep.builder()
            .forNpc(NpcID.ASYFF)
            .at(3281, 3398, 0)
            .withText("Speak to Asyff again.")
            .requiring(yellowDye, coins, tar, tenEagleFeathers)
            .withDialogSteps(
                "I've got the feathers and materials you requested.",
                "Okay, here are the materials. Eagle me up."
            )
            .build()

        returnToEaglesPeak = ObjectStep.builder()
            .forObject(NULL_19790)
            .at(2329, 3495, 0)
            .withText("Enter Eagles' Peak through the Rocky Outcrop.")
            .requiring(fakeBeak, eagleCape)
            .withTeleport(eaglesPeakTeleport)
            .build()
        enterEastCave = ObjectStep.builder()
            .forObject(TUNNEL_19897)
            .at(2023, 4982, 3)
            .withText("Enter the eastern cavern of Eagles' Peak.")
            .build()

        enterBronzeRoom = ObjectStep.builder()
            .forObject(TUNNEL_19909)
            .at(1986, 4949, 3)
            .withText("Enter the south-western cavern of Eagles' Peak.")
            .build()

        attemptToTakeBronzeFeather = ObjectStep.builder()
            .forObject(PEDESTAL_19980)
            .at(1974, 4915, 2)
            .withText("Try to take the feather from the pedestal.")
            .build()

        winch1 = ObjectStep.builder()
            .forObject(WINCH_19972)
            .at(1970, 4919, 2)
            .withText("Use the winches in the corners of the room.")
            .build()

        winch2 = ObjectStep.builder()
            .forObject(WINCH_19973)
            .at(1978, 4919, 2)
            .withText("Use the winches in the corners of the room.")
            .build()

        winch3 = ObjectStep.builder()
            .forObject(WINCH_19974)
            .at(1970, 4910, 2)
            .withText("Use the winches in the corners of the room.")
            .build()

        winch4 = ObjectStep.builder()
            .forObject(WINCH_19975)
            .at(1978, 4910, 2)
            .withText("Use the winches in the corners of the room.")
            .build()

        grabBronzeFeather = ObjectStep.builder()
            .forObject(STONE_PEDESTAL_19984)
            .at(1974, 4915, 2)
            .withText("Take the feather from the pedestal.")
            .build()

        enterMainCavernFromBronze = ObjectStep.builder()
            .forObject(TUNNEL_19906)
            .at(1974, 4907, 2)
            .withText("Return to the main cavern.")
            .build()

        enterSilverRoom = ObjectStep.builder()
            .forObject(TUNNEL_19903)
            .at(1986, 4972, 3)
            .withText("Enter the north-western cavern of Eagles' Peak.")
            .build()

        inspectSilverPedestal = ObjectStep.builder()
            .forObject(STONE_PEDESTAL_19974)
            .at(1947, 4873, 2)
            .withText("Inspect the Stone Pedestal here.")
            .build()

        enterMainCavernFromSilver = ObjectStep.builder()
            .forObject(TUNNEL_19900)
            .at(1947, 4867, 2)
            .withText("Return to the main cavern.")
            .build()

        inspectRocks1 = ObjectStep.builder()
            .forObject(ROCKS_19969)
            .at(1961, 4875, 2)
            .withText("Inspect the rocks east of the pedestal.")
            .build()

        inspectRocks2 = ObjectStep.builder()
            .forObject(ROCKS_19970)
            .at(1967, 4879, 2)
            .withText("Inspect the rocks north east of the last rock.")
            .build()

        inspectOpening = ObjectStep.builder()
            .forObject(OPENING)
            .at(1971, 4886, 2)
            .withText("Inspect the opening north of the second rock.")
            .build()

        threatenKebbit = NpcStep.builder()
            .forNpc(NpcID.KEBBIT)
            .at(1971, 4880, 2)
            .withText("Right-click threaten the Kebbit that appears. If the kebbit's gone, re-inspect the opening.")
            .withDialogStep("Taunt the kebbit.")
            .build()

        pickupSilverFeather = ObjectStep.builder()
            .forObject(OPENING)
            .at(1971, 4886, 2)
            .withText("Pick up the silver feather. If it's despawned, inspect the opening to get it.")
            .build()

        pickUpActualSilverFeather = ObjectStep.builder()
            .forObject(SILVER_FEATHER_19972)
            .withText("Pick up the silver feather.")
            .requiring(silverFeather)
            .build()

        enterGoldRoom = ObjectStep.builder()
            .forObject(TUNNEL_19897)
            .at(2023, 4982, 3)
            .withText("Enter the tunnel in the north east of the main cavern.")
            .build()

        collectFeed = ObjectStep.builder()
            .forObject(BIRDSEED_HOLDER)
            .at(1958, 4906, 2)
            .withText("Collect 6 birdseed from the Birdseed holder.")
            .requiring(birdFeed6)
            .build()

        pullLever1Down = ObjectStep.builder()
            .forObject(NULL_19948)
            .at(1943, 4911, 2)
            .withText("Pull the lever west of the entrance down.")
            .build()

        pushLever1Up = ObjectStep.builder()
            .forObject(NULL_19948)
            .at(1943, 4911, 2)
            .withText("Push the lever west of the entrance up.")
            .build()

        pullLever2Down = ObjectStep.builder()
            .forObject(NULL_19949)
            .at(1978, 4891, 2)
            .withText("Pull the lever in the south east corner down.")
            .build()

        pullLever3Down = ObjectStep.builder()
            .forObject(NULL_19946)
            .at(1935, 4902, 2)
            .withText("Pull the lever in the south west corner down.")
            .build()

        pullLever4Down = ObjectStep.builder()
            .forObject(NULL_19947)
            .at(1925, 4915, 2)
            .withText("Pull the lever in the north west corner down.")
            .build()

        fillFeeder1 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19939)
            .at(1966, 4890, 2)
            .withText("Use the odd bird seed on the Bird feeder in the far south eastern corner.")
            .requiring(birdFeed)
            .build()

        fillFeeder2 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19938)
            .at(1962, 4894, 2)
            .withText("Use the odd bird seed on the marked Bird feeder.")
            .requiring(birdFeed)
            .build()

        fillFeeder3 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19943)
            .at(1962, 4901, 2)
            .withText("Use the odd bird seed on the marked Bird feeder, as you've moved the wrong bird.")
            .requiring(birdFeed)
            .build()

        fillFeeder4 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19937)
            .at(1947, 4898, 2)
            .withText("Put odd bird feed into the feeder in the north east of the room.")
            .requiring(birdFeed)
            .build()

        fillFeeder5 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19936)
            .at(1945, 4915, 2)
            .withText("Put odd bird feed into the feeder in the south of the room.")
            .requiring(birdFeed)
            .build()

        fillFeeder6 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19941)
            .at(1935, 4897, 2)
            .withText("Put odd bird feed into the feeder in the south west of the room")
            .requiring(birdFeed)
            .build()

        fillFeeder4Again = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19937)
            .at(1947, 4898, 2)
            .withText("Put odd bird feed into the feeder in the south of the room.")
            .requiring(birdFeed)
            .build()

        fillFeeder7 = ObjectStep.builder()
            .forObject(BIRD_FEEDER_19940)
            .at(1931, 4916, 2)
            .withText("Put odd bird feed in the feeder in the north west of the room.")
            .requiring(birdFeed)
            .build()

        grabGoldFeather = ObjectStep.builder()
            .forObject(STONE_PEDESTAL_19950)
            .at(1928, 4907, 2)
            .withText("Grab the Golden feather from the pedestal.")
            .build()

        enterMainCavernFromGold = ObjectStep.builder()
            .forObject(TUNNEL_19894)
            .at(1957, 4909, 2)
            .withText("Return to the main cavern.")
            .build()

        useFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use all three feathers on the door.")
            .requiring(goldFeatherHighlighted, silverFeatherHighlighted, bronzeFeatherHighlighted)
            .build()

        useBronzeFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the bronze feather on the door.")
            .requiring(bronzeFeatherHighlighted)
            .build()

        useSilverFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the silver feather on the door.")
            .requiring(silverFeatherHighlighted)
            .build()

        useGoldFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the golden feather on the door.")
            .requiring(goldFeatherHighlighted)
            .build()

        useGoldBronzeFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the bronze and golden feathers on the door.")
            .requiring(goldFeatherHighlighted, bronzeFeatherHighlighted)
            .build()

        useGoldSilverFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the silver and golden feathers on the door.")
            .requiring(goldFeatherHighlighted, silverFeatherHighlighted, bronzeFeatherHighlighted)
            .build()

        useBronzeSilverFeathersOnStoneDoor = ObjectStep.builder()
            .forObject(NULL_19991)
            .at(2003, 4948, 3)
            .withText("Use the bronze and silver feathers on the door.")
            .requiring(silverFeatherHighlighted, bronzeFeatherHighlighted)
            .build()

        sneakPastEagle = NpcStep.builder()
            .forNpc(NpcID.EAGLE)
            .at(2008, 4955, 3)
            .withText("Go through the feather door and sneak past the Eagle whilst wearing your eagle disguise.")
            .requiring(fakeBeak.equipped(), eagleCape.equipped())
            .build()

        speakToNickolaus = NpcStep.builder()
            .forNpc(NpcID.NICKOLAUS_1485)
            .at(2006, 4960, 3)
            .withText("Speak to Nickolaus.")
            .requiring(fakeBeak, eagleCape)
            .build()

        speakToNickolausInTheCamp = NpcStep.builder()
            .forNpc(NpcID.NICKOLAUS_1485)
            .at(2317, 3504, 0)
            .withText("Speak to Nickolaus in his camp north of Eagles' Peak.")
            .withDialogSteps(
                "Well I was originally sent to find you because of a ferret.",
                "That sounds good to me."
            )
            .build()

        speakToCharlieAgain = NpcStep.builder()
            .forNpc(NpcID.CHARLIE_1495)
            .at(2607, 3264, 0)
            .withText("Bring the ferret back to Charlie in Ardougne Zoo.")
            .requiring(ferret)
            .withTeleport(ardougneTeleport)
            .build()

        leavePeak = ObjectStep.builder()
            .forObject(CAVE_ENTRANCE_19891)
            .at(1993, 4983, 3)
            .withText("Speak to Nickolaus in his camp north of Eagles' Peak.")
            .build()
        log.debug("Steps set up")
    }

    override fun getVar(): Int {
        return 0
    }

    override fun getItemRequirements(): List<ItemRequirement> {
        val reqs = ArrayList<ItemRequirement>()
        reqs.add(yellowDye)
        reqs.add(coins)
        reqs.add(tar)
        return reqs
    }

    override fun getItemRecommended(): List<ItemRequirement> {
        val reqs = ArrayList<ItemRequirement>()
        reqs.add(eaglesPeakTeleport.quantity(2))
        reqs.add(varrockTeleport)
        reqs.add(ardougneTeleport.quantity(2))
        return reqs
    }

    override fun getQuestPointReward(): QuestPointReward {
        return QuestPointReward(2)
    }

    override fun getExperienceRewards(): List<ExperienceReward> {
        return Collections.singletonList(ExperienceReward(Skill.HUNTER, 2500))
    }

    override fun getUnlockRewards(): List<UnlockReward> {
        return listOf(
            UnlockReward("Ability to use Box Traps"),
            UnlockReward("Ability to use Eagle Transport System"),
            UnlockReward("Ability to hunt Rabbits.")
        )
    }

    override fun getPanels(): List<PanelDetails> = buildList {
        add(
            PanelDetails(
                header = "Start the quest",
                steps = listOf(speakToCharlie),
                recommended = listOf(ardougneTeleport)
            )
        )

        add(
            PanelDetails(
                header = "Go to Eagles' Peak",
                steps = listOf(inspectBooks, clickBook, useFeatherOnDoor),
                recommended = listOf(eaglesPeakTeleport)
            )
        )

        add(
            PanelDetails(
                header = "In Eagles' Peak",
                steps = listOf(enterPeak, shoutAtNickolaus, pickupFeathers)
            )
        )

        add(
            PanelDetails(
                header = "Make a disguise",
                steps = listOf(goToFancyStore, speakAsyffAgain),
                requirements = listOf(yellowDye, coins, tar, tenEagleFeathers),
                recommended = listOf(varrockTeleport, eaglesPeakTeleport)
            )
        )

        add(
            PanelDetails(
                header = "Return to Eagles' Peak",
                steps = listOf(returnToEaglesPeak),
                requirements = listOf(fakeBeak, eagleCape),
                recommended = listOf(eaglesPeakTeleport)
            )
        )

        add(
            PanelDetails(
                header = "Get the bronze feather",
                steps = listOf(
                    enterBronzeRoom,
                    attemptToTakeBronzeFeather,
                    winch1,
                    grabBronzeFeather
                )
            )
        )

        add(
            PanelDetails(
                header = "Get the silver feather",
                steps = listOf(
                    enterSilverRoom,
                    inspectSilverPedestal,
                    inspectRocks1,
                    inspectRocks2,
                    inspectOpening,
                    threatenKebbit,
                    pickupSilverFeather
                )
            )
        )

        add(
            PanelDetails(
                header = "Get the golden feather",
                steps = listOf(
                    enterGoldRoom,
                    collectFeed,
                    pullLever1Down,
                    fillFeeder1,
                    fillFeeder2,
                    pullLever2Down,
                    pushLever1Up,
                    fillFeeder4,
                    pullLever3Down,
                    fillFeeder5,
                    pullLever4Down,
                    fillFeeder6,
                    fillFeeder4Again,
                    grabGoldFeather
                )
            )
        )

        add(
            PanelDetails(
                header = "Free Nickolaus",
                steps = listOf(
                    useFeathersOnStoneDoor,
                    sneakPastEagle,
                    speakToNickolaus
                )
            )
        )

        add(
            PanelDetails(
                header = "Learn how to catch ferrets",
                steps = listOf(speakToNickolausInTheCamp, speakToCharlieAgain),
                recommended = listOf(ardougneTeleport)
            )
        )
    }

    override fun getGeneralRequirements(): List<Requirement> {
        return listOf(SkillRequirement(Skill.HUNTER, 27, canBeBoosted = true))
    }

    companion object {
        const val SILVER_ROOM_19970 = 19970
        const val SILVER_FEATHER_19972 = 19972
        const val GOLD_ROOM_19974 = 19974
        const val GOLD_FEATHER_19975 = 19975

        // Object IDs
        const val TUNNEL_19897 = 19897
        const val TUNNEL_19909 = 19909
        const val PEDESTAL_19980 = 19980
        const val PEDESTAL_19981 = 19981
        const val STONE_PEDESTAL_19984 = 19984
        const val CAVE_DOOR_19967 = 19967
        const val EAST_CAVE_19968 = 19968
        const val BRONZE_ROOM_19969 = 19969
        const val BRONZE_FEATHER_19971 = 19971
        const val WINCH_19972 = 19972
        const val WINCH_19973 = 19973
        const val WINCH_19974 = 19974
        const val WINCH_19975 = 19975
        const val BOOKCASE_19894 = 19894

        // Additional Object IDs
        const val NULL_19787 = 19787  // Book pile at start
        const val BOOKS_19886 = 19886 // Book pile for metal feather
        const val NULL_19790 = 19790  // Rocky outcrop
        const val GIANT_FEATHERS = 19893 // For picking up feathers
        const val TUNNEL_19906 = 19906 // Return from bronze room
        const val TUNNEL_19903 = 19903 // Enter silver room
        const val STONE_PEDESTAL_19974 = 19974 // Silver room pedestal
        const val TUNNEL_19900 = 19900 // Return from silver
        const val ROCKS_19969 = 19969  // First rocks to inspect
        const val ROCKS_19970 = 19970  // Second rocks
        const val OPENING = 19971      // Opening to inspect
        const val BIRDSEED_HOLDER = 19892 // Birdseed holder
        const val NULL_19948 = 19948   // Lever 1
        const val NULL_19949 = 19949   // Lever 2
        const val NULL_19946 = 19946   // Lever 3
        const val NULL_19947 = 19947   // Lever 4
        const val BIRD_FEEDER_19939 = 19939 // Feeder 1
        const val BIRD_FEEDER_19938 = 19938 // Feeder 2
        const val BIRD_FEEDER_19943 = 19943 // Feeder 3
        const val BIRD_FEEDER_19937 = 19937 // Feeder 4
        const val BIRD_FEEDER_19936 = 19936 // Feeder 5
        const val BIRD_FEEDER_19941 = 19941 // Feeder 6
        const val BIRD_FEEDER_19940 = 19940 // Feeder 7
        const val STONE_PEDESTAL_19950 = 19950 // Gold feather pedestal
        const val TUNNEL_19894 = 19894 // Return from gold room
        const val NULL_19991 = 19991   // Stone door for feathers
        const val CAVE_ENTRANCE_19891 = 19891 // Cave entrance/exit
    }
}