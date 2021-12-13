package dao

import plane.Plane
import java.util.concurrent.ConcurrentHashMap

class PlaneInMemoryDao : PlaneDao {
    private val planesMap: MutableMap<String, Plane> = ConcurrentHashMap()

    override fun addPlane(plane: Plane) {
        planesMap[plane.name] = plane
    }

    override fun findPlane(name: String): Plane? {
        return planesMap[name]
    }

    override fun updatePlane(name: String, maxVelocity: Int) {
        planesMap[name]!!.maxVelocity = maxVelocity
    }
}