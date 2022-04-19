package storage

import exceptions.UnknownAccountException
import services.ReportService
import java.time.LocalDateTime

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

    fun getAccountExpirationDate(login: String): LocalDateTime {
        val history = getEvents(login)

        if (history.isEmpty()) {
            throw UnknownAccountException()
        }

        return history.fold(LocalDateTime.MIN) { result, event ->
            when (event) {
                is AccountCreated -> event.date + event.duration
                is AccountExtended -> result + event.duration
                else -> result
            }
        }
    }
}