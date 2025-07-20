package com.recursive.quester.framework.requirements.util

enum class LogicType(
    private val function: (Sequence<*>, (Any?) -> Boolean) -> Boolean,
    private val comparatorFunction: (Number, Number) -> Boolean
) {
    /** Returns true only if all inputs match the supplied predicate. */
    AND({ sequence, predicate -> sequence.all(predicate) },
        { n1, n2 -> n1.toDouble() == n2.toDouble() }),

    /** Returns true if any inputs match the supplied predicate. */
    OR({ sequence, predicate -> sequence.any(predicate) },
        { n1, _ -> n1.toDouble() > 0.0 }),

    /** The output is false if all inputs match the supplied predicate. Otherwise returns true. */
    NAND({ sequence, predicate -> !sequence.all(predicate) },
        { n1, n2 -> n1.toDouble() < n2.toDouble() }),

    /** Returns true if all elements do not match the supplied predicate. */
    NOR({ sequence, predicate -> sequence.none(predicate) },
        { n1, _ -> n1.toDouble() == 0.0 }),

    /** Returns true if either, but not both, inputs match the given predicate.
     * This only tests the first two elements of the sequence.
     */
    XOR({ sequence, predicate ->
        sequence.filter(predicate).take(2).count() == 1
    },
        { n1, n2 ->
            (n1.toDouble() > 0.0 && n2.toDouble() <= 0.0) || (n2.toDouble() > 0.0 && n1.toDouble() <= 0.0)
        });

    fun <T> test(sequence: Sequence<T>, predicate: (T) -> Boolean): Boolean {
        @Suppress("UNCHECKED_CAST")
        return function(sequence as Sequence<*>, predicate as (Any?) -> Boolean)
    }

    fun compare(numberToCheck: Number, numberToCheckAgainst: Number): Boolean {
        return comparatorFunction(numberToCheck, numberToCheckAgainst)
    }
}