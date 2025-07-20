package com.recursive.quester.framework.requirements.player

import com.recursive.quester.framework.requirements.AbstractRequirement
import com.runemate.game.api.hybrid.local.Skill

class SkillRequirement(
    private val skill: Skill,
    private val requiredLevel: Int,
    private val operation: Operation = Operation.GREATER_EQUAL,
    private var canBeBoosted: Boolean = false
) : AbstractRequirement() {

    private var displayText: String? = null

    // Enum to handle different types of level comparisons
    enum class Operation {
        EQUAL,
        NOT_EQUAL,
        LESS,
        GREATER,
        LESS_EQUAL,
        GREATER_EQUAL
    }

    // Secondary constructor for display text
    constructor(
        skill: Skill,
        requiredLevel: Int,
        canBeBoosted: Boolean,
        displayText: String
    ) : this(skill, requiredLevel, Operation.GREATER_EQUAL, canBeBoosted) {
        this.displayText = displayText
    }

    override fun check(): Boolean {
        val skillLevel = if (canBeBoosted) {
            maxOf(skill.currentLevel, skill.baseLevel)
        } else {
            skill.currentLevel
        }

        return when (operation) {
            Operation.EQUAL -> skillLevel == requiredLevel
            Operation.NOT_EQUAL -> skillLevel != requiredLevel
            Operation.LESS -> skillLevel < requiredLevel
            Operation.GREATER -> skillLevel > requiredLevel
            Operation.LESS_EQUAL -> skillLevel <= requiredLevel
            Operation.GREATER_EQUAL -> skillLevel >= requiredLevel
        }
    }

    fun checkRange(config: QuestHelperConfig): Boolean {
        val currentLevel = maxOf(skill.currentLevel, skill.baseLevel)
        val highestBoost = when (skill) {
            Skill.THIEVING -> {
                when {
                    skill.baseLevel >= 65 -> 5  // Summer sq'irk
                    skill.baseLevel >= 45 -> 2  // Autumn sq'irk
                    else -> 1  // Spring sq'irk
                }
            }

            else -> if (config.stewBoosts) 5 else getDefaultBoost(skill)
        }

        return requiredLevel - highestBoost <= currentLevel
    }

    private fun getDefaultBoost(skill: Skill): Int {
        return when (skill) {
            Skill.CONSTRUCTION -> 3  // Crystal saw
            Skill.FARMING -> 5       // Garden pie
            Skill.MINING -> 3        // Dragon pickaxe
            Skill.WOODCUTTING -> 3   // Dragon axe
            else -> 0
        }
    }

    fun checkBoosted(config: QuestHelperConfig): Int {
        val skillLevel = if (canBeBoosted) {
            maxOf(skill.currentLevel, skill.baseLevel)
        } else {
            skill.baseLevel
        }

        return when {
            skillLevel >= requiredLevel -> 1  // Meets requirement
            canBeBoosted && checkRange(config) -> 2  // Can be boosted
            else -> 3  // Does not meet requirement
        }
    }

    override fun getDisplayText(): String {
        return displayText ?: buildString {
            append(requiredLevel)
            append(" ")
            append(skill.name)
            if (canBeBoosted) {
                append(" (boostable)")
            }
        }
    }

    // Helper class for config options
    data class QuestHelperConfig(
        val stewBoosts: Boolean = false,
        val passColor: Int = 0x00FF00,   // Green
        val boostColor: Int = 0xFFFF00,  // Yellow
        val failColor: Int = 0xFF0000    // Red
    )
}


