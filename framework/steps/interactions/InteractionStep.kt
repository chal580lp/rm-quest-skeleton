package com.recursive.quester.framework.steps.interactions

data class InteractionStep(
    val action: String,
    val validate: () -> Boolean,
    val reason: String?,
    val execute: () -> Boolean
)