package com.recursive.quester.framework.requirements

import com.runemate.game.api.hybrid.region.Region

class InInstanceRequirement : Requirement {
    override fun check(): Boolean {
        return Region.isInstanced()
    }
}