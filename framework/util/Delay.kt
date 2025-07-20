package com.recursive.quester.framework.util

import com.runemate.game.api.hybrid.RuneScape
import com.runemate.game.api.hybrid.local.House
import com.runemate.game.api.hybrid.local.hud.interfaces.*
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.location.navigation.Traversal
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.region.Region
import com.runemate.game.api.hybrid.util.Validatable
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer
import com.runemate.game.api.script.Execution
typealias DelayCondition = () -> Boolean

object Delay {
	val MOVING: DelayCondition = { Players.getLocal()?.isMoving ?: false }
	val NOT_MOVING: DelayCondition = { Players.getLocal()?.isMoving?.not() ?: false }
	val NOT_IDLE: DelayCondition = { Players.getLocal()?.isIdle?.not() ?: false }
	val TARGETING: DelayCondition = { Players.getLocal()?.target != null }
	val ANIMATING: DelayCondition = { Players.getLocal()?.animationId != -1 }
	val NOT_ANIMATING: DelayCondition = { Players.getLocal()?.animationId == -1 }
	val PRAYER_RESTORED: DelayCondition = { Prayer.getPoints() >= Prayer.getMaximumPoints() }
	val HEALTH_RESTORED: DelayCondition = { Health.getCurrent() >= Health.getMaximum() }
    val ENERGY_RESTORED: DelayCondition = { Traversal.getRunEnergy() >= 99 }
	val MOVING_OR_ACTIVE: DelayCondition = { MOVING() || NOT_IDLE() }
	val BANK_IS_OPEN: DelayCondition = { Bank.isOpen() }
    val CUTSCENE_IS_PLAYING: DelayCondition = { RuneScape.isCutscenePlaying() }

	fun healed(initialHealth: Int): DelayCondition = { Health.getCurrent() > initialHealth }

	fun moved(initialCoordinate: Coordinate, currentCoordinate: () -> Coordinate): DelayCondition = {
		initialCoordinate != currentCoordinate()
	}

	inline fun until(
		crossinline condition: DelayCondition,
		timeout: Int = 1200,
		noinline failCondition: DelayCondition = MOVING_OR_ACTIVE,
	): Boolean {
		return Execution.delayUntil({ condition() }, { failCondition() }, timeout)
	}


	inline fun whilst(
		crossinline condition: DelayCondition,
		timeout: Int = 1200,
		noinline failCondition: DelayCondition = MOVING_OR_ACTIVE,
	): Boolean {
		return Execution.delayWhile({ condition() }, { failCondition() }, timeout)
	}

	fun untilRegionEntered(vararg regions: Int): Boolean {
        return until({ Region.getCurrentRegionId() in regions }, 6000)
	}

	fun untilInArea(area: Area): Boolean {
		return until({ area.contains(Players.getLocal()) })
	}

	fun untilInHouse(): Boolean {
		return until({ House.isInside() }, timeout = 3600)
	}

	fun untilChatOptionsAvailable(): Boolean {
		return until({ ChatDialog.getOptions().isNotEmpty() })
	}

    fun untilChatContains(text: String): Boolean {
		return until({ Chatbox.newQuery().textContains(text).results().isNotEmpty() })
	}

	fun untilInventoryContains(itemName: String): Boolean {
		return until({ Inventory.contains(itemName) })
	}

	fun untilInventoryContains(id: Int): Boolean {
		return until({ Inventory.contains(id) })
	}

	fun whileValid(validatable: Validatable?, timeout: Int = 1200): Boolean {
		return whilst({ validatable?.isValid ?: false }, timeout)
	}

	fun delaySeconds(seconds: Int) {
		Execution.delay((seconds * 1000).toLong())
	}

	fun delayTicks(ticks: Int) {
		Execution.delay((ticks * 600).toLong())
	}
}