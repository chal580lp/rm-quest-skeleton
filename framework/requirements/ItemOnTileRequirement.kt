package com.recursive.quester.framework.requirements

import com.recursive.quester.framework.requirements.zone.Zone
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GroundItems

class ItemOnTileRequirement : AbstractRequirement {
    private val itemID: List<Int>
    private var coordinate: Coordinate? = null

    constructor(itemID: Int) {
        this.itemID = listOf(itemID)
    }

    constructor(item: ItemRequirement) {
        requireNotNull(item) { "ItemRequirement must not be null" }
        this.itemID = item.getAllIds()
    }

    constructor(itemID: Int, zone: Zone) {
        requireNotNull(zone) { "Zone must not be null" }
        this.itemID = listOf(itemID)
        this.coordinate = zone.getMinCoordinate()
    }

    constructor(itemID: Int, coordinate: Coordinate) {
        requireNotNull(coordinate) { "Coordinate must not be null" }
        this.itemID = listOf(itemID)
        this.coordinate = coordinate
    }

    constructor(item: ItemRequirement, coordinate: Coordinate) {
        requireNotNull(item) { "ItemRequirement must not be null" }
        requireNotNull(coordinate) { "Coordinate must not be null" }
        this.itemID = item.getAllIds()
        this.coordinate = coordinate
    }

    override fun check(): Boolean {
        return checkAllTiles()
    }

    override fun getDisplayText(): String {
        TODO("Not yet implemented")
    }

    private fun checkAllTiles(): Boolean {
        coordinate?.let {
            val groundItemsAtCoordinate = GroundItems.newQuery().on(it).ids(*itemID.toIntArray()).results()
            if (groundItemsAtCoordinate.isNotEmpty()) {
                return true // Found required items at the specified coordinate
            }
        }


        val allGroundItems = GroundItems.newQuery().ids(*itemID.toIntArray()).results()
        if (allGroundItems.isNotEmpty()) {
            return true
        }

        return false
    }
}
