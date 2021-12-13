import aspect.ProfilingExecutionTimeAspect
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import passenger.model.implementations.PassengerBusiness
import passenger.model.implementations.PassengerEconomy
import plane.PlaneManager
import kotlin.random.Random

fun main() {
    val ctx: ApplicationContext = AnnotationConfigApplicationContext(ContextConfiguration::class.java)
    planeManagerActions(ctx)
    passengerActions(ctx)

    ctx.getBean(ProfilingExecutionTimeAspect::class.java).printStatistics()
}

private fun planeManagerActions(ctx: ApplicationContext) {
    val planeManager = ctx.getBean(PlaneManager::class.java)
    val planes = listOf("A321", "A330", "A340", "A350").onEach { planeManager.addPlane(it, 100) }

    repeat(10) {
        if (Random.nextBoolean()) {
            planeManager.findPlane(planes.random())
        } else {
            planeManager.updatePlane(planes.random())
        }
    }
}

private fun passengerActions(ctx: ApplicationContext) {
    val passengerEconomy = ctx.getBean(PassengerEconomy::class.java)
    val passengerBusiness = ctx.getBean(PassengerBusiness::class.java)

    repeat(10) {
        if (Random.nextBoolean()) {
            passengerEconomy.callFlightAttendant()
        } else {
            passengerBusiness.callFlightAttendant()
        }
    }
}
