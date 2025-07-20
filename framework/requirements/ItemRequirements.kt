package com.recursive.quester.framework.requirements

import com.recursive.quester.framework.requirements.util.LogicType
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem

class ItemRequirements : ItemRequirement {
    val itemRequirements = ArrayList<ItemRequirement>()
    val logicType: LogicType

    constructor(vararg requirements: ItemRequirement) : this("", *requirements)

    constructor(name: String, vararg itemRequirements: ItemRequirement) : super(name, itemRequirements[0].id, -1) {
        assert(varargsNotNull(*itemRequirements))
        this.itemRequirements.addAll(itemRequirements)
        this.logicType = LogicType.AND
    }

    constructor(logicType: LogicType, name: String, vararg itemRequirements: ItemRequirement) : super(
        name,
        itemRequirements[0].id,
        -1
    ) {
        assert(varargsNotNull(*itemRequirements))
        this.itemRequirements.addAll(itemRequirements)
        this.logicType = logicType
    }

    constructor(logicType: LogicType, name: String, itemRequirements: List<ItemRequirement>) : super(
        name,
        itemRequirements[0].id,
        -1
    ) {
        assert(itemRequirements.none { false })
        this.itemRequirements.addAll(itemRequirements)
        this.logicType = logicType
    }

    constructor(logicType: LogicType, vararg requirements: ItemRequirement) : this(logicType, "", *requirements)

    override fun isActualItem(): Boolean {
        return LogicType.OR.test(itemRequirements.asSequence()) { item ->
            !item.getAllIds().contains(-1) && item.quantity >= 0
        }
    }

    override fun check(): Boolean {
        return check(false)
    }

    override fun check(checkConsideringSlotRestrictions: Boolean): Boolean {
        val successes = itemRequirements
            .filterNotNull()
            .count { it.check(checkConsideringSlotRestrictions) }
        hadItemLastCheck = logicType.compare(successes, itemRequirements.size)
        return hadItemLastCheck
    }

    override fun check(checkConsideringSlotRestrictions: Boolean, items: List<SpriteItem>): Boolean {
        val successes = itemRequirements
            .filterNotNull()
            .count { it.check(checkConsideringSlotRestrictions, items) }
        hadItemLastCheck = logicType.compare(successes, itemRequirements.size)
        return hadItemLastCheck
    }

    override fun getAllIds(): List<Int> {
        return itemRequirements
            .map { it.getAllIds() }
            .flatten()
    }

    override fun equipped(): ItemRequirement {
        val newItem = copy() as ItemRequirements
        newItem.itemRequirements.forEach { itemRequirement -> itemRequirement.setEquip(true) }
        _equip = true
        return newItem
    }

    override fun checkBank(): Boolean {
        return logicType.test(itemRequirements.asSequence()) { item ->
            item.checkBank() || item.check(false)
        }
    }

    override fun copy(): ItemRequirement {
        val newItem = ItemRequirements(logicType, name, itemRequirements)
        newItem.addAlternates(alternateItems)
        newItem.setDisplayItemId(_displayItemId)
        newItem.setExclusiveToOneItemType(_exclusiveToOneItemType)
        newItem.setHighlightInInventory(_highlightInInventory)
        newItem.setConditionToHide(_conditionToHide)
        newItem.setTooltip(_tooltip)
        newItem.additionalOptions = additionalOptions
        return newItem
    }

    override fun setEquip(shouldEquip: Boolean) {
        itemRequirements.forEach { itemRequirement -> itemRequirement.setEquip(true) }
        _equip = shouldEquip
    }

    companion object {
        fun and(vararg requirements: ItemRequirement): ItemRequirements {
            return ItemRequirements(LogicType.AND, *requirements)
        }

        fun or(vararg requirements: ItemRequirement): ItemRequirements {
            return ItemRequirements(LogicType.OR, *requirements)
        }
    }

    fun <T> varargsNotNull(vararg elements: T): Boolean {
        for (el in elements) {
            if (el == null) {
                return false
            }
        }
        return true
    }
}