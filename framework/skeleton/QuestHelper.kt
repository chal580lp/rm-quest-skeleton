package com.recursive.quester.framework.skeleton

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.reward.*
import com.recursive.quester.framework.steps.OwnerStep
import com.recursive.quester.framework.steps.QuestStep
import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory

abstract class QuestHelper {
    var currentStep: QuestStep? = null
    lateinit var quest: Quest

    // New properties for managing the quest configuration
    protected var hasInitialized = false

    abstract fun init()
    abstract fun startUp()
    abstract fun shutDown()
    abstract fun updateQuest(): Boolean

    protected open fun startUpStep(step: QuestStep?) {
        if (step != null) {
            currentStep = step
            currentStep?.startUp()
        } else {
            currentStep = null
        }
    }

    protected open fun shutDownStep() {
        currentStep?.shutDown()
        currentStep = null
    }

    open fun isCompleted(): Boolean {
        return quest.status == Quest.Status.COMPLETE
    }

    open fun clientMeetsRequirements(): Boolean {
        return getGeneralRequirements()?.all { it.check() } ?: true
    }

    abstract fun getVar(): Int

    open fun getItemRequirements(): List<ItemRequirement>? = null
    open fun getGeneralRequirements(): List<Requirement>? = null
    open fun getItemRecommended(): List<ItemRequirement>? = null
    open fun getGeneralRecommended(): List<Requirement>? = null
    open fun getCombatRequirements(): List<String>? = null
    open fun getNotes(): List<String>? = null

    // New method to check if the player has the required items in their inventory
    fun checkRequirements(): Boolean {
        val itemRequirements = getItemRequirements() ?: return true
        return itemRequirements.all { requirement ->
            Inventory.getItems(requirement.id).isNotEmpty()
        }
    }

    // Add new methods to manage quest rewards
    open fun getQuestPointReward(): QuestPointReward? = null
    open fun getItemRewards(): List<ItemReward>? = null
    open fun getExperienceRewards(): List<ExperienceReward>? = null
    open fun getUnlockRewards(): List<UnlockReward>? = null

    // Aggregate rewards for the quest
    fun getQuestRewards(): List<Reward> {
        val rewards = mutableListOf<Reward>()

        getQuestPointReward()?.let { rewards.add(it) }
        getExperienceRewards()?.let { rewards.addAll(it) }
        getItemRewards()?.let { rewards.addAll(it) }
        getUnlockRewards()?.let { rewards.addAll(it) }

        return rewards
    }

    // Optional: If you need to aggregate quest rewards text
    open fun getQuestRewardsText(): List<String> {
        val rewardsText = mutableListOf<String>()

        getQuestPointReward()?.let { rewardsText.add(it.getDisplayText()) }
        getItemRewards()?.forEach { rewardsText.add(it.getDisplayText()) }
        getExperienceRewards()?.forEach { rewardsText.add(it.getDisplayText()) }
        getUnlockRewards()?.forEach { rewardsText.add(it.getDisplayText()) }

        return rewardsText
    }

    // For instantiating steps recursively
    companion object {
        fun instantiateSteps(steps: Collection<QuestStep>) {
            for (step in steps) {
                if (step is OwnerStep) {
                    instantiateSteps(step.getSteps())
                }
            }
        }
    }

    // New methods and variables for zones and requirements initialization
    protected open fun setupZones() {
        // Implement zone setup logic if needed
    }

    protected abstract fun setupRequirements()

    fun initializeRequirements() {
        if (hasInitialized) return
        setupZones()
        setupRequirements()
        hasInitialized = true
    }


}
