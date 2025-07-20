package com.recursive.quester.framework.extension

import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.local.Camera

fun Npc?.isInteractable(): Boolean {
    return this != null && (this.visibility > 99 || Camera.turnTo(this)) && this.position?.isReachable == true
}