package com.recursive.quester.framework.skeleton

import com.recursive.quester.framework.panel.PanelDetails
import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.steps.QuestStep
import com.recursive.quester.framework.util.getLogger


abstract class BasicQuestHelper : QuestHelper() {
    private val log = getLogger("BasicQuestHelper")
    var steps: Map<Int, QuestStep> = emptyMap()
    protected var var_: Int = 0

    protected val teleport: MutableList<Requirement> = mutableListOf()

    override fun init() {
        if (steps.isEmpty()) {
            steps = loadSteps()
            log.info("Loaded ${steps.size} steps")
        }
    }

    override fun startUp() {
        steps = loadSteps()
        instantiateSteps(steps.values)
        var_ = getVar()
        startUpStep(steps[var_])
    }

    override fun shutDown() {
        shutDownStep()
    }

    override fun updateQuest(): Boolean {
        if (var_ != getVar()) {
            var_ = getVar()
            shutDownStep()
            startUpStep(steps[var_])
            return true
        }
        return false
    }

    fun addTeleport(newTeleport: Requirement) {
        teleport.add(newTeleport)
    }

    open fun getPanels(): List<PanelDetails>? {
        val panelSteps: MutableList<PanelDetails> = ArrayList()
        steps.forEach { (_: Any?, step: Any?) ->
            panelSteps.add(
                PanelDetails("", listOf(step))
            )
        }
        return panelSteps
    }

    override fun getItemRequirements(): List<ItemRequirement>? = null
    override fun getGeneralRequirements(): List<Requirement>? = null
    override fun getItemRecommended(): List<ItemRequirement>? = null
    override fun getGeneralRecommended(): List<Requirement>? = null
    override fun getCombatRequirements(): List<String>? = null
    override fun getNotes(): List<String>? = null

    abstract fun loadSteps(): Map<Int, QuestStep>
    abstract override fun getVar(): Int
}