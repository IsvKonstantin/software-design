package dao

import plane.Plane

interface PlaneDao {
    fun addPlane(plane: Plane)

    fun findPlane(name: String): Plane?

    fun updatePlane(name: String, maxVelocity: Int)
}