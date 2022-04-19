package services

import exceptions.UnknownEventException
import storage.Enter
import storage.Event
import storage.EventStorage
import storage.Exit
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class ReportService(eventStorage: EventStorage) {
    private val visits = mutableMapOf<LocalDate, MutableMap<String, Long>>()
    private val lastVisits = mutableMapOf<String, LocalDateTime>()
    private var totalDuration = Duration.ZERO

    init {
        eventStorage.getAllEvents().forEach { innerHandle(it) }
        eventStorage.subscribe(this)
    }

    fun handle(event: Event) {
        innerHandle(event)
    }

    private fun innerHandle(event: Event) = when (event) {
        is Enter -> {
            lastVisits[event.login] = event.date

            visits.computeIfAbsent(event.date.toLocalDate()) { mutableMapOf<String, Long>().withDefault { 0 } }
            visits[event.date.toLocalDate()]!![event.login] =
                visits[event.date.toLocalDate()]!!.getValue(event.login) + 1
        }
        is Exit -> {
            val duration = Duration.between(event.date, lastVisits[event.login])
            totalDuration += duration
        }
        else -> {
            throw UnknownEventException()
        }
    }

    fun getTotalDuration(): Duration {
        return totalDuration
    }

    fun getDailyVisits(): Map<LocalDate, Long> {
        return visits.mapValues { it.value.values.sum() }
    }

    fun getAverageFrequency(): Double {
        val dailyVisits = getDailyVisits()
        return dailyVisits.values.sum().toDouble() / dailyVisits.size.toDouble()
    }
}