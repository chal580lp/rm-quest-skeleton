package com.recursive.quester.framework.requirements


import com.recursive.quester.framework.requirements.conditional.Conditions
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.util.getLogger
import com.recursive.quester.runelite.ItemCollections
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults


open class ItemRequirement(
    var name: String,
    var id: Int,
    var quantity: Int = 1,
    var _equip: Boolean = false,
) : AbstractRequirement() {
    private val log = getLogger("ItemRequirement")
    protected val alternateItems: MutableList<Int> = mutableListOf()
    var _displayItemId: Int? = null
    var _exclusiveToOneItemType: Boolean = false
    var isChargedItem: Boolean = false
    var _highlightInInventory = false
    var _displayMatchedItemName: Boolean = false
    var hadItemLastCheck: Boolean = false
    protected var isConsumedItem: Boolean = true
    protected var shouldAggregate: Boolean = true
    protected var additionalOptions: Requirement? = null
    protected var _conditionToHide: Requirement? = null

    //
    protected var _canBeObtainedDuringQuest: Boolean = false

    // Constructors
    constructor(name: String, items: List<Int>) : this(name, items[0], 1) {
        require(items.none { false }) { "Item list cannot contain null values" }
        addAlternates(items.subList(1, items.size))
    }

    constructor(name: String, items: List<Int>, quantity: Int) : this(name, items[0], quantity) {
        require(items.none { false }) { "Item list cannot contain null values" }
        addAlternates(items.subList(1, items.size))
    }

    constructor(name: String, items: List<Int>, quantity: Int, equip: Boolean) : this(name, items[0], quantity, equip) {
        require(items.none { false }) { "Item list cannot contain null values" }
        addAlternates(items.subList(1, items.size))
    }

    constructor(name: String, itemCollection: ItemCollections) : this(
        name,
        itemCollection.items[0],  // In Kotlin we can use the property syntax
        1
    ) {
        itemCollection.wikiTerm?.let { setUrlSuffix(it) }  // Use property syntax
        addAlternates(itemCollection.items.subList(1, itemCollection.items.size))
    }

    constructor(name: String, itemCollection: ItemCollections, quantity: Int) : this(
        name,
        itemCollection.items[0],
        quantity
    ) {
        itemCollection.wikiTerm?.let { setUrlSuffix(it) }
        addAlternates(itemCollection.items.subList(1, itemCollection.items.size))
    }

    constructor(name: String, itemCollection: ItemCollections, quantity: Int, equip: Boolean) : this(
        name,
        itemCollection.items[0],
        quantity,
        equip
    ) {
        itemCollection.wikiTerm?.let { setUrlSuffix(it) }
        addAlternates(itemCollection.items.subList(1, itemCollection.items.size))
    }


    fun canBeObtainedDuringQuest(): ItemRequirement {
        _canBeObtainedDuringQuest = true
        return this
    }

    fun isNotConsumed(): ItemRequirement {
        val newItem = copy()
        newItem.isConsumedItem = false
        return newItem
    }

    fun addAlternates(itemCollection: ItemCollections) {
        addAlternates(itemCollection.items)
    }

    fun addAlternates(alternates: List<Int>) {
        alternateItems.addAll(alternates)
    }

    fun addAlternates(vararg alternates: Int) {
        alternateItems.addAll(alternates.toList())
    }

    fun setDisplayItemId(id: Int?) {
        _displayItemId = id
    }

    fun setExclusiveToOneItemType(exclusive: Boolean) {
        _exclusiveToOneItemType = exclusive
    }

    fun setHighlightInInventory(highlight: Boolean) {
        _highlightInInventory = highlight
    }

    fun setDisplayMatchedItemName(display: Boolean) {
        _displayMatchedItemName = display
    }

    fun setConditionToHide(condition: Requirement?) {
        _conditionToHide = condition
    }

    open fun setEquip(shouldEquip: Boolean) {
        _equip = shouldEquip
    }

    // Builder-style methods
    fun highlighted(): ItemRequirement {
        return copy().apply {
            _highlightInInventory = true
        }
    }

    fun doNotAggregate(): ItemRequirement {
        return copy().apply {
            shouldAggregate = false
        }
    }

    fun quantity(newQuantity: Int): ItemRequirement {
        return copy().apply {
            quantity = newQuantity
        }
    }

    fun hideConditioned(condition: Requirement): ItemRequirement {
        return copy().apply {
            _conditionToHide = condition
        }
    }

    fun showConditioned(condition: Requirement): ItemRequirement {
        return copy().apply {
            _conditionToHide = Conditions(LogicType.NOR, condition)
        }
    }

    fun named(newName: String): ItemRequirement {
        return copy().apply {
            name = newName
        }
    }

    open fun isActualItem(): Boolean {
        return id != -1 && quantity != -1
    }

    open fun getAllIds(): List<Int> {
        return (listOf(id) + alternateItems).distinct()
    }

    open fun equipped(): ItemRequirement {
        return copy().apply {
            _equip = true
        }
    }

    open fun checkBank(): Boolean {
        return false
    }

    fun showQuantity(): Boolean {
        return quantity != -1
    }

    // Check methods
    open fun check(checkConsideringSlotRestrictions: Boolean): Boolean {
        if (shouldSkipRequirementCheck()) return false
        if (checkAdditionalOptions()) return true

        val remainder = getRequiredItemDifference(checkConsideringSlotRestrictions)
        hadItemLastCheck = remainder <= 0
        return hadItemLastCheck
    }

    open fun check(checkConsideringSlotRestrictions: Boolean, items: List<SpriteItem>): Boolean {
        return check(checkConsideringSlotRestrictions)
    }

    private fun shouldSkipRequirementCheck(): Boolean {
        return _conditionToHide?.check() == true
    }

    private fun checkAdditionalOptions(): Boolean {
        return additionalOptions?.check() == true
    }

    protected open fun getRequiredItemDifference(checkConsideringSlotRestrictions: Boolean): Int {
        var tempQuantity = quantity

        if (_equip) {
            tempQuantity -= getNumMatches(Equipment.getItems(), id)
        } else if (!checkConsideringSlotRestrictions) {
            tempQuantity -= getNumMatches(Equipment.getItems(), id)
            tempQuantity -= getNumMatches(Inventory.getItems(), id)
        }

        if (tempQuantity > 0) {
            for (alternateId in alternateItems) {
                if (_exclusiveToOneItemType) {
                    tempQuantity = quantity
                }
                if (_equip) {
                    tempQuantity -= getNumMatches(Equipment.getItems(), alternateId)
                } else if (!checkConsideringSlotRestrictions) {
                    tempQuantity -= getNumMatches(Equipment.getItems(), alternateId)
                    tempQuantity -= getNumMatches(Inventory.getItems(), alternateId)
                }
                if (tempQuantity <= 0) break
            }
        }

        return tempQuantity
    }

    private fun getNumMatches(items: SpriteItemQueryResults, itemId: Int): Int {
        return items.filter { it.id == itemId }
            .sumOf { item ->
                if (isChargedItem) {
                    // Implement charge checking logic here if needed
                    item.quantity
                } else {
                    item.quantity
                }
            }
    }

    fun shouldRenderItemHighlights(): Boolean {
        return _conditionToHide == null || !_conditionToHide!!.check()
    }

    fun shouldHighlightInInventory(): Boolean {
        return _highlightInInventory && shouldRenderItemHighlights()
    }

    override fun check(): Boolean {
        val items =
            if (_equip) Equipment.newQuery().ids(getAllIds()).results() else Inventory.newQuery().ids(getAllIds())
                .results()
        val count = items.sumOf { it.quantity }
        if (count < quantity) {
            log.warn("ItemRequirement: $name (ids:${getAllIds()}) x$quantity not met. Found $count")
            return false
        }
        return true
    }

    override fun getDisplayText(): String {
        val quantityText = if (quantity > 1) "$quantity x " else ""
        return "$quantityText$name"
    }

    open fun copy(): ItemRequirement {
        return ItemRequirement(name, id, quantity, _equip).apply {
            alternateItems.addAll(this@ItemRequirement.alternateItems)
            _displayItemId = this@ItemRequirement._displayItemId
            _exclusiveToOneItemType = this@ItemRequirement._exclusiveToOneItemType
            isChargedItem = this@ItemRequirement.isChargedItem
            _highlightInInventory = this@ItemRequirement._highlightInInventory
            _displayMatchedItemName = this@ItemRequirement._displayMatchedItemName
            _conditionToHide = this@ItemRequirement._conditionToHide
            additionalOptions = this@ItemRequirement.additionalOptions
            hadItemLastCheck = this@ItemRequirement.hadItemLastCheck
            setTooltip(this@ItemRequirement.getTooltip())  // Use superclass tooltip methods
        }
    }

    override fun toString(): String {
        return "$name x$quantity"
    }
}