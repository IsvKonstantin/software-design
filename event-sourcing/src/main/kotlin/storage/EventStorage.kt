package storage

import exceptions.UnknownAccountException
import services.ReportService
import java.time.Instant

class EventStorage {
    private val listeners = ArrayList<ReportService>()
    private val events = ArrayList<Event>()

    fun subscribe(listener: ReportService) {
        listeners.add(listener)
    }

    fun submitEvent(event: Event) {
        events.add(event)

        listeners.forEach { it.handle(event) }
    }

    fun getAllEvents(): List<Event> {
        return events
    }

    fun getEvents(login: String): List<Event> {
        return events.filter { it.login == login }
    }

    fun getAccountExpirationDate(login: String): Instant {
        val history = getEvents(login)

        if (history.isEmpty()) {
            throw UnknownAccountException()
        }

        return history.fold(Instant.MIN) { result, event ->
            when (event) {
                is AccountCreated -> event.date.plus(event.duration)
                is AccountExtended -> result.plus(event.duration)
                else -> result
            }
        }
    }
}