package com.recursive.quester.framework.execution

data class QuestState(
    val questName: String,
    val currentStep: String,
    val progress: Int,
    val isComplete: Boolean,
    val failureCount: Int
)