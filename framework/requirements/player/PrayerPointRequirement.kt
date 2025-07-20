package com.recursive.quester.framework.requirements.player

import com.recursive.quester.framework.requirements.AbstractRequirement
import com.runemate.game.api.hybrid.local.Skill

class PrayerPointRequirement(private val level: Int) : AbstractRequirement() {
    override fun check(): Boolean {
        return Skill.PRAYER.currentLevel >= level
    }

    override fun getDisplayText(): String {
        return "$level prayer points"
    }
}