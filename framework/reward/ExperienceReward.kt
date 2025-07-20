package com.recursive.quester.framework.reward

import com.runemate.game.api.hybrid.local.Skill

class ExperienceReward(
    val skill: Skill,
    val experience: Int,
    private val lamp: Boolean = false
) : Reward {

    override fun rewardType(): RewardType {
        return RewardType.EXPERIENCE
    }

    override fun getDisplayText(): String {
        return if (lamp) {
            "${experience} ${skill.name.replaceFirstChar { it.uppercase() }} Experience Lamp"
        } else {
            "${experience} ${skill.name.replaceFirstChar { it.uppercase() }} Experience"
        }
    }
}