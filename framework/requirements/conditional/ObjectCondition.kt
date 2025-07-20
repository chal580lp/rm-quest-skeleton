import com.recursive.quester.framework.requirements.conditional.ConditionForStep
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.framework.listeners.GameObjectListener
import com.runemate.game.api.script.framework.listeners.events.GameObjectDespawnEvent
import com.runemate.game.api.script.framework.listeners.events.GameObjectSpawnEvent

class ObjectCondition : ConditionForStep, GameObjectListener {
    private val log = getLogger("ObjectCondition")
    private val objects = mutableListOf<GameObject>()
    private var objectInScene = false
    private val objectId: Int
    private val area: Area.Rectangular?
    private var maxDistance: Int = -1
    private var checkOnlyGameObjects: Boolean = false

    // Primary constructor with required object ID
    constructor(objectId: Int) {
        this.objectId = objectId
        this.area = null
    }

    // Constructor with specific coordinate
    constructor(objectId: Int, coordinate: Coordinate) {
        this.objectId = objectId
        this.area = coordinate.area.grow(1, 1)
    }

    // Constructor with area
    constructor(objectId: Int, area: Area.Rectangular) {
        this.objectId = objectId
        this.area = area
    }

    override fun check(): Boolean {
        initialize() // Ensure we have current objects

        return when {
            area != null -> {
                objects.any { obj -> isInArea(obj) }
            }

            maxDistance > 0 -> {
                val playerPos = Players.getLocal()?.position ?: return false
                objects.any { obj ->
                    obj.position?.distanceTo(playerPos)?.toInt() ?: Int.MAX_VALUE <= maxDistance
                }
            }

            else -> objectInScene
        }
    }

    private fun initialize() {
        objects.clear()
        objectInScene = false

        val query = GameObjects.newQuery().ids(objectId)

        // Apply area filter if specified
        if (area != null) {
            query.within(area)
        }

        // Apply distance filter if specified
        if (maxDistance > 0) {
            val playerPos = Players.getLocal()?.position ?: return
            query.filter { obj ->
                (obj.position?.distanceTo(playerPos)?.toInt() ?: Int.MAX_VALUE) <= maxDistance
            }
        }

        query.results().forEach { obj ->
            objects.add(obj)
            objectInScene = true
        }
    }

    private fun isInArea(obj: Locatable): Boolean {
        if (area == null) return true
        return obj.position?.let { pos ->
            area.contains(pos)
        } ?: false
    }

    // GameObjectListener implementation
    override fun onGameObjectSpawned(event: GameObjectSpawnEvent) {
        val obj = event.eventObject ?: return
        if (obj.id == objectId && (area == null || isInArea(obj))) {
            log.info("Added Object spawn: $obj")
            objects.add(obj)
            objectInScene = true
        }
    }

    override fun onGameObjectDespawned(event: GameObjectDespawnEvent?) {
        val obj = event?.eventObject ?: return
        if (objects.remove(obj)) {
            objectInScene = objects.isNotEmpty()
        }
    }

    // Reset the condition
    fun reset() {
        objects.clear()
        objectInScene = false
    }

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var objectId: Int? = null
        private var area: Area.Rectangular? = null
        private var maxDistance: Int = -1
        private var checkOnlyGameObjects: Boolean = false

        fun forObject(id: Int) = apply {
            this.objectId = id
        }

        fun at(coordinate: Coordinate) = apply {
            this.area = coordinate.area.grow(1, 1)
        }

        fun inArea(area: Area.Rectangular) = apply {
            this.area = area
        }

        fun withMaxDistance(distance: Int) = apply {
            this.maxDistance = distance
        }

        fun checkingOnlyGameObjects(check: Boolean = true) = apply {
            this.checkOnlyGameObjects = check
        }

        fun build(): ObjectCondition {
            requireNotNull(objectId) { "Object ID must be specified" }

            return when {
                area != null -> ObjectCondition(objectId!!, area!!)
                else -> ObjectCondition(objectId!!)
            }.apply {
                maxDistance = this@Builder.maxDistance
                checkOnlyGameObjects = this@Builder.checkOnlyGameObjects
            }
        }
    }
}