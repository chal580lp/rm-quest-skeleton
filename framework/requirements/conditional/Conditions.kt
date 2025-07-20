package com.recursive.quester.framework.requirements.conditional

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.requirements.util.LogicType
import com.recursive.quester.framework.requirements.util.Operation

class Conditions : Requirement {
    private val conditions: MutableList<Requirement>
    private var logicType: LogicType
    private var operation: Operation? = null
    private var quantity: Int = 0
    private var onlyNeedToPassOnce: Boolean = false
    private var hasPassed: Boolean = false

    var text: String? = null

    constructor(vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.logicType = LogicType.AND
    }

    constructor(conditions: List<Requirement>) {
        this.conditions = conditions.toMutableList()
        this.logicType = LogicType.AND
    }

    constructor(logicType: LogicType, vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.logicType = logicType
    }

    constructor(operation: Operation, quantity: Int, vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.logicType = LogicType.AND
        this.operation = operation
        this.quantity = quantity
    }

    constructor(logicType: LogicType, conditions: List<Requirement>) {
        this.conditions = conditions.toMutableList()
        this.logicType = logicType
    }

    constructor(onlyNeedToPassOnce: Boolean, operation: Operation, quantity: Int, vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.onlyNeedToPassOnce = onlyNeedToPassOnce
        this.logicType = LogicType.AND
        this.operation = operation
        this.quantity = quantity
    }

    constructor(onlyNeedToPassOnce: Boolean, logicType: LogicType, vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.onlyNeedToPassOnce = onlyNeedToPassOnce
        this.logicType = logicType
    }

    constructor(onlyNeedToPassOnce: Boolean, vararg conditions: Requirement) {
        this.conditions = conditions.toMutableList()
        this.onlyNeedToPassOnce = onlyNeedToPassOnce
        this.logicType = LogicType.AND
    }

    override fun getDisplayText(): String {
        if (text != null) return text!!

        val conditionsText = conditions.joinToString(", ") {
            "${it.getDisplayText()}=${it.check()}"
        }

        return buildString {
            append("${logicType.name}(")
            append(conditionsText)
            append(")")

            operation?.let { op ->
                append(" ${op.name}($quantity)")
            }

            if (onlyNeedToPassOnce) {
                append(" [OneTime${if (hasPassed) "-Passed" else ""}]")
            }
        }
    }

    override fun check(): Boolean {
        if (onlyNeedToPassOnce && hasPassed) {
            return true
        }

        val conditionsPassed = conditions.count { it.check() }

        if (operation != null) {
            return operation!!.check(conditionsPassed, quantity)
        }

        val result = when (logicType) {
            LogicType.OR -> conditionsPassed > 0
            LogicType.NOR -> conditionsPassed == 0
            LogicType.AND -> conditionsPassed == conditions.size
            LogicType.NAND -> conditionsPassed < conditions.size
            LogicType.XOR -> conditionsPassed == 1
        }

        if (result) {
            hasPassed = true
        }

        return result
    }

}