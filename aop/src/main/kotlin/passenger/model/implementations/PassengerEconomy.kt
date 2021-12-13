package passenger.model.implementations

import aspect.Profile
import passenger.model.Passenger
import kotlin.random.Random

open class PassengerEconomy : Passenger {
    @Profile
    override fun callFlightAttendant() {
        Thread.sleep(Random.nextLong(100, 200))
    }
}