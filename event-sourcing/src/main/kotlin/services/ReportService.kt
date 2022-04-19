package services

import storage.Enter
import storage.Event
import storage.EventStorage
import storage.Exit
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class ReportService(eventStorage: EventStorage, private val clock: Clock) {
    private val visits = mutableMapOf<LocalDate, MutableMap<String, Int>>()
    private val lastVisits = mutableMapOf<String, Instant>()
    private var totalDuration = Duration.ZERO

    init {
        eventStorage.getAllEvents().forEach { innerHandle(it) }
        eventStorage.subscribe(this)
    }

    fun handle(event: Event) {
        innerHandle(event)
    }

    private fun innerHandle(event: Event) {
        return when (event) {
            is Enter -> {
                lastVisits[event.login] = event.date
                val localDate = LocalDate.ofInstant(event.date, clock.zone)

                visits.computeIfAbsent(localDate) { mutableMapOf<String, Int>().withDefault { 0 } }
                visits[localDate]!![event.login] = visits[localDate]!!.getValue(event.login) + 1
            }
            is Exit -> {
                val duration = Duration.between(lastVisits[event.login], event.date)
                totalDuration += duration
            }
            else -> {}
        }
    }

    fun getTotalDuration(): Duration {
        return totalDuration
    }

    fun getWeekStats(): Map<LocalDate, Int> {
        return visits.mapValues { it.value.values.sum() }
    }

    fun getAverageVisitsDaily(): Double {
        val dailyVisits = getWeekStats()
        return dailyVisits.values.sum().toDouble() / dailyVisits.size.toDouble()
    }
}