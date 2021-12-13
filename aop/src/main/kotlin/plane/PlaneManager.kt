package plane

interface PlaneManager {
    fun addPlane(name: String, maxVelocity: Int)

    fun findPlane(name: String): Plane?

    fun updatePlane(name: String)
}