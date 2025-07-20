package com.recursive.quester.framework.steps

interface EntityStep<T> {
    val entities: MutableList<T>
    var _closestEntity: T?
    var lastInteractionTime: Long
    val maxDistance: Int
    val identifier: Identifier

    fun entityPassesChecks(entity: T): Boolean
    fun updateEntities()
    fun getClosestEntity(): T? = _closestEntity
    fun getAllEntities(): List<T> = entities.toList()
    fun hasEntity(): Boolean = entities.isNotEmpty()
}