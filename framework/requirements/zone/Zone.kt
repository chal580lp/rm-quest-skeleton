package com.recursive.quester.framework.requirements.zone

import com.runemate.game.api.hybrid.location.Coordinate

class Zone {
    val minX: Int
    val maxX: Int
    val minY: Int
    val maxY: Int
    private var minPlane: Int = 0
    private var maxPlane: Int = 2

    // The first plane of the "Overworld"
    constructor() {
        minX = 1152
        maxX = 3903
        minY = 2496
        maxY = 4159
        maxPlane = 0
    }

    constructor(p1: Coordinate, p2: Coordinate) {
        minX = minOf(p1.x, p2.x)
        maxX = maxOf(p1.x, p2.x)
        minY = minOf(p1.y, p2.y)
        maxY = maxOf(p1.y, p2.y)
        minPlane = minOf(p1.plane, p2.plane)
        maxPlane = maxOf(p1.plane, p2.plane)
    }

    constructor(p: Coordinate) {
        minX = p.x
        maxX = p.x
        minY = p.y
        maxY = p.y
        minPlane = p.plane
        maxPlane = p.plane
    }

    constructor(regionID: Int) {
        minX = ((regionID shr 8) and 0xFF) shl 6
        maxX = minX + REGION_SIZE
        minY = (regionID and 0xFF) shl 6
        maxY = minY + REGION_SIZE
    }

    constructor(regionID: Int, plane: Int) : this(regionID) {
        minPlane = plane
        maxPlane = plane
    }

    fun contains(coordinate: Coordinate): Boolean {
        return coordinate.x in minX..maxX &&
                minY <= coordinate.y &&
                coordinate.y <= maxY &&
                minPlane <= coordinate.plane &&
                coordinate.plane <= maxPlane
    }

    fun getMinCoordinate(): Coordinate {
        return Coordinate(minX, minY, minPlane)
    }

    companion object {
        private const val REGION_SIZE = 64 // Equivalent to RuneLite's Constants.REGION_SIZE
    }
}