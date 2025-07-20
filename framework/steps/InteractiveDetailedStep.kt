package com.recursive.quester.framework.steps

abstract class InteractiveDetailedStep<T, I>(
    config: StepConfiguration,
    detailedConfig: DetailedStepConfiguration,
    override val identifier: Identifier,
    val interaction: I?,
    override val maxDistance: Int
) : DetailedQuestStep(config, detailedConfig), EntityStep<T> {

    override val entities = mutableListOf<T>()
    override var _closestEntity: T? = null
    override var lastInteractionTime: Long = 0

    // Common helper methods
    protected fun checkInteractionCooldown(): Boolean {
        return System.currentTimeMillis() - lastInteractionTime >= 300
    }

    protected fun updateInteractionTime() {
        lastInteractionTime = System.currentTimeMillis()
    }

    override fun startUp() {
        super.startUp()
        updateEntities()
    }

    override fun shutDown() {
        super.shutDown()
        entities.clear()
        _closestEntity = null
    }

    override fun shouldExecute(): Boolean {
        if (isLocked() || isStepComplete()) {
            return false
        }
        return checkRequirements()
    }
}