package event

import clock.Clock
import java.util.*
import java.util.concurrent.TimeUnit.HOURS

class EventStatisticImpl(private val clock: Clock) : EventsStatistic {
    private val events: MutableMap<String, MutableList<Long>> = mutableMapOf()

    override fun incEvent(name: String) {
        events
            .getOrPut(name) { mutableListOf() }
            .apply { add(clock.now().epochSecond) }
    }

    override fun getEventStatisticByName(name: String): Double {
        val requests = events[name] ?: return 0.0
        val intervalEnd = clock.now().epochSecond
        val intervalStart = (intervalEnd - HOURS.toSeconds(1L)).coerceAtLeast(0L)
        val filteredRequests = requests.filter { it in (intervalStart..intervalEnd) }

        return filteredRequests.size.toDouble() / HOURS.toMinutes(1L)
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        return events.keys.associateWith { getEventStatisticByName(it) }
    }

    override fun printStatistic() {
        events.forEach { (key, value) ->
            val rpm = value.size.toDouble() / HOURS.toMinutes(1L)
            println("Event: $key, rpm: %.2f".format(Locale("en"), rpm))
        }
    }
}