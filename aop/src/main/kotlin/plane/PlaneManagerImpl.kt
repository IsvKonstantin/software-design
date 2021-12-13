package plane

import aspect.Profile
import dao.PlaneDao
import kotlin.random.Random

open class PlaneManagerImpl(private val planeDao: PlaneDao) : PlaneManager {
    @Profile
    override fun addPlane(name: String, maxVelocity: Int) {
        planeDao.addPlane(Plane(name, maxVelocity))
    }

    @Profile
    override fun findPlane(name: String): Plane? {
        return planeDao.findPlane(name)
    }

    @Profile
    override fun updatePlane(name: String) {
        Thread.sleep(100)
        planeDao.updatePlane(name,  Random.nextInt(500, 1000))
    }
}
