package event

interface EventsStatistic {
    fun incEvent(name: String)

    fun getEventStatisticByName(name: String)

    fun getAllEventStatistic()

    fun printStatistic()
}