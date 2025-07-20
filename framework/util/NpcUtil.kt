package com.recursive.quester.framework.util

import com.recursive.quester.framework.extension.isInteractable
import com.runemate.game.api.hybrid.region.Npcs

object NpcUtil {
    private val log = getLogger("NpcUtil")

    fun canInteractWithNpc(name: String): Boolean {
        val npc = Npcs.newQuery().names(name).reachable().results().nearest() ?: return false
        return npc.isInteractable()
    }
}