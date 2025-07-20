package com.recursive.quester.framework.requirements.util

enum class Operation(
    val displayText: String,
    private val operation: (Int, Int) -> Boolean
) {
    GREATER(">", { x, y -> x > y }),
    LESS("<", { x, y -> x < y }),
    LESS_EQUAL("<=", { x, y -> x <= y }),
    EQUAL("==", Int::equals),
    GREATER_EQUAL(">=", { x, y -> x >= y }),
    NOT_EQUAL("=/=", { x, y -> x != y });

    fun check(numberToCheck: Int, numberToCheckAgainst: Int): Boolean {
        return operation(numberToCheck, numberToCheckAgainst)
    }
}