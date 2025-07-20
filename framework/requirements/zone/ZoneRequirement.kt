package com.recursive.quester.framework.requirements.zone

import com.recursive.quester.framework.requirements.Requirement
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.Players

class ZoneRequirement : Requirement {
    private val zones: List<Zone>
    private val checkInZone: Boolean
    private var displayText: String? = null

    constructor(displayText: String, zone: Zone) : this(displayText, false, zone)

    constructor(displayText: String, checkNotInZone: Boolean, zone: Zone) {
        this.displayText = displayText
        this.checkInZone = !checkNotInZone
        this.zones = listOf(zone)
    }

    constructor(vararg coordinates: Coordinate) {
        this.zones = coordinates.map { Zone(it) }
        this.checkInZone = true
    }

    constructor(vararg zones: Zone) {
        this.zones = zones.toList()
        this.checkInZone = true
    }

    constructor(checkInZone: Boolean, vararg zones: Zone) {
        this.zones = zones.toList()
        this.checkInZone = checkInZone
    }

    constructor(checkInZone: Boolean, vararg coordinates: Coordinate) {
        this.zones = coordinates.map { Zone(it) }
        this.checkInZone = checkInZone
    }

    override fun check(): Boolean {
        val player = Players.getLocal()
        return if (player != null) {
            val location = player.position ?: return false
            val inZone = zones.any { it.contains(location) }
            inZone == checkInZone
        } else {
            false
        }
    }

    override fun getDisplayText(): String {
        return displayText ?: ""
    }
}