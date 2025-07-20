package com.recursive.quester.framework.steps

interface OwnerStep {
    fun getSteps(): Collection<QuestStep>
}