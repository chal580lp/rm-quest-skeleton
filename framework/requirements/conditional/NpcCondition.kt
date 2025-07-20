package com.recursive.quester.framework.requirements.conditional

import com.recursive.quester.framework.requirements.Requirement
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.Npcs
import com.runemate.game.api.script.framework.listeners.NpcListener
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent
import com.runemate.game.api.script.framework.listeners.events.NpcDespawnedEvent
import com.runemate.game.api.script.framework.listeners.events.NpcSpawnedEvent

class NpcCondition : Requirement, NpcListener {
    private val log = getLogger("NpcCondition")
    private val npcs = mutableListOf<Npc>()
    private var npcInScene = false
    private val npcId: Int?
    private val npcName: String?
    private val area: Area.Rectangular?
    var requiredAnimationId: Int? = null

    // Primary constructor with NPC ID
    constructor(npcId: Int) {
        this.npcId = npcId
        this.npcName = null
        this.area = null
    }

    // Primary constructor with NPC name
    constructor(npcName: String) {
        this.npcId = null
        this.npcName = npcName
        this.area = null
    }

    // Constructor with specific coordinate and ID
    constructor(npcId: Int, coordinate: Coordinate) {
        this.npcId = npcId
        this.npcName = null
        this.area = coordinate.area.grow(1, 1)
    }

    // Constructor with specific coordinate and name
    constructor(npcName: String, coordinate: Coordinate) {
        this.npcId = null
        this.npcName = npcName
        this.area = coordinate.area.grow(1, 1)
    }

    // Constructor with area and ID
    constructor(npcId: Int, area: Area.Rectangular) {
        this.npcId = npcId
        this.npcName = null
        this.area = area
    }

    // Constructor with area and name
    constructor(npcName: String, area: Area.Rectangular) {
        this.npcId = null
        this.npcName = npcName
        this.area = area
    }

    override fun check(): Boolean {
        initialize() // Ensure we have current NPCs
        return if (area != null) {
            npcs.any { npc ->
                isInArea(npc) && hasCorrectAnimation(npc)
            }
        } else {
            npcInScene
        }
    }

    private fun initialize() {
        // Clear existing NPCs and scan for new ones
        npcs.clear()
        npcInScene = false

        val query = Npcs.newQuery()

        // Apply either ID or name filter
        when {
            npcId != null -> query.ids(npcId)
            npcName != null -> query.names(npcName)
        }

        query.results().forEach { npc ->
            npcs.add(npc)
            npcInScene = true
        }
    }

    private fun isInArea(npc: Npc): Boolean {
        if (area == null) return true
        return npc.position?.let { pos ->
            area.contains(pos)
        } ?: false
    }

    private fun hasCorrectAnimation(npc: Npc): Boolean {
        return requiredAnimationId == null || npc.animationId == requiredAnimationId
    }

    private fun matchesNpc(npc: Npc): Boolean {
        return when {
            npcId != null -> npc.id == npcId
            npcName != null -> npc.name == npcName
            else -> false
        }
    }

    // NpcListener implementation
    override fun onNpcSpawned(event: NpcSpawnedEvent) {
        val npc = event.npc ?: return
        if (matchesNpc(npc)) {
            npcs.add(npc)
            npcInScene = true
        }
    }

    override fun onNpcDespawned(event: NpcDespawnedEvent?) {
        if (npcs.removeAll { it.index == event?.index }) {
            log.info("NPC despawned: ${event?.lastDefinition?.name}")
            npcInScene = npcs.isNotEmpty()
        }
    }

    override fun onNpcAnimationChanged(event: AnimationEvent?) {
        val npc = event?.source as? Npc ?: return
        if (!matchesNpc(npc) && npcs.remove(npc)) {
            log.info("NPC animation changed to non-matching: ${npc.name ?: npc.id}")
            npcInScene = npcs.isNotEmpty()
        } else if (matchesNpc(npc) && npc !in npcs) {
            log.info("Adding new NPC to list: ${npc.name ?: npc.id} from animation")
            npcs.add(npc)
            npcInScene = true
        }
    }

    fun reset() {
        log.info("Resetting NPC condition")
        npcs.clear()
        npcInScene = false
    }

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var npcId: Int? = null
        private var npcName: String? = null
        private var area: Area.Rectangular? = null
        private var animationId: Int? = null

        fun forNpc(id: Int) = apply {
            this.npcId = id
            this.npcName = null
        }

        fun forNpc(name: String) = apply {
            this.npcName = name
            this.npcId = null
        }

        fun at(coordinate: Coordinate) = apply {
            this.area = coordinate.area.grow(1, 1)
        }

        fun inArea(area: Area.Rectangular) = apply {
            this.area = area
        }

        fun withAnimation(animationId: Int) = apply {
            this.animationId = animationId
        }

        fun build(): NpcCondition {
            require(npcId != null || npcName != null) { "Either NPC ID or name must be specified" }

            return when {
                area != null && npcId != null -> NpcCondition(npcId!!, area!!)
                area != null && npcName != null -> NpcCondition(npcName!!, area!!)
                npcId != null -> NpcCondition(npcId!!)
                npcName != null -> NpcCondition(npcName!!)
                else -> throw IllegalStateException("Invalid builder state")
            }.apply {
                requiredAnimationId = animationId
            }
        }
    }
}