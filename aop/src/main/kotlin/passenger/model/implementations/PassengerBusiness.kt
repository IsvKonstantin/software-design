package passenger.model.implementations

import aspect.Profile
import passenger.model.Passenger
import kotlin.random.Random

open class PassengerBusiness : Passenger {
    @Profile
    override fun callFlightAttendant() {
        Thread.sleep(Random.nextLong(50, 100))
    }
}