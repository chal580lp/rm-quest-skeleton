package com.recursive.quester.framework.extension


import com.runemate.game.api.hybrid.entities.LocatableEntity
import com.runemate.game.api.hybrid.queries.LocatableEntityQueryBuilder
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.util.calculations.Distance

fun <T : LocatableEntity, QB : LocatableEntityQueryBuilder<T, QB>> LocatableEntityQueryBuilder<T, QB>.maxDistance(
    distance: Int
): QB {
    return filter { entity ->
        val playerPos = Players.getLocal()?.position ?: return@filter false
        entity.position?.let { pos ->
            Distance.between(playerPos, pos).toInt() <= distance
        } ?: false
    }
}