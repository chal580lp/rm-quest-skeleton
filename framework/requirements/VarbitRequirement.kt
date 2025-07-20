package com.recursive.quester.framework.requirements

import com.recursive.quester.framework.requirements.util.Operation
import com.runemate.game.api.hybrid.local.Varbits

class VarbitRequirement(
    private val varbitID: Int,
    private val requiredValue: Int,
    private val operation: Operation = Operation.EQUAL,
    private val displayText: String? = null
) : AbstractRequirement() {

    override fun check(): Boolean {
        return Varbits.load(varbitID)?.value?.let { value ->
            operation.check(value, requiredValue)
        } ?: false
    }

    override fun getDisplayText(): String {
        return displayText ?: "Varbit $varbitID must be ${operation.displayText} $requiredValue"
    }

    companion object {
        fun exactly(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value)
        fun greaterThan(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value, Operation.GREATER)
        fun lessThan(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value, Operation.LESS)
        fun greaterOrEqual(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value, Operation.GREATER_EQUAL)
        fun lessOrEqual(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value, Operation.LESS_EQUAL)
        fun notEqual(varbitID: Int, value: Int) = VarbitRequirement(varbitID, value, Operation.NOT_EQUAL)
    }
}