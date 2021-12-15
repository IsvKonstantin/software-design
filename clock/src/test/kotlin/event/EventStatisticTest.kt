package event

import clock.SettableClock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Duration
import java.time.Instant


internal class EventStatisticTest {
    private lateinit var clock: SettableClock
    private lateinit var eventsStatistic: EventsStatistic

    @BeforeEach
    fun setUp() {
        clock = SettableClock(Instant.now())
        eventsStatistic = EventStatisticImpl(clock)
    }

    @Test
    fun `event statistic for unknown event should be empty`() {
        assertThat(eventsStatistic.getEventStatisticByName("unknown")).isEqualTo(0.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEmpty()
    }

    @Test
    fun `single event statistic test`() {
        val name = "name"

        eventsStatistic.incEvent(name)
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(1.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 1.0 / 60.0))

        eventsStatistic.incEvent(name)
        eventsStatistic.incEvent(name)
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(3.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 3.0 / 60.0))
    }

    @Test
    fun `multiple events statistic test`() {
        val nameFirst = "nameFirst"
        val nameSecond = "nameSecond"

        eventsStatistic.incEvent(nameFirst)
        eventsStatistic.incEvent(nameSecond)
        eventsStatistic.incEvent(nameSecond)
        assertThat(eventsStatistic.getEventStatisticByName(nameFirst)).isEqualTo(1.0 / 60.0)
        assertThat(eventsStatistic.getEventStatisticByName(nameSecond)).isEqualTo(2.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(
            mapOf(nameFirst to 1.0 / 60.0, nameSecond to 2.0 / 60.0)
        )

        eventsStatistic.incEvent(nameFirst)
        assertThat(eventsStatistic.getEventStatisticByName(nameFirst)).isEqualTo(2.0 / 60.0)
        assertThat(eventsStatistic.getEventStatisticByName(nameSecond)).isEqualTo(2.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(
            mapOf(nameFirst to 2.0 / 60.0, nameSecond to 2.0 / 60.0)
        )
    }

    @Test
    fun `single event statistic with changing time test`() {
        val name = "name"

        eventsStatistic.incEvent(name)
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(1.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 1.0 / 60.0))

        clock.now = clock.now.plus(Duration.ofMinutes(25))
        eventsStatistic.incEvent(name)
        eventsStatistic.incEvent(name)
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(3.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 3.0 / 60.0))

        clock.now = clock.now.plus(Duration.ofMinutes(25))
        eventsStatistic.incEvent(name)
        eventsStatistic.incEvent(name)
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(5.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 5.0 / 60.0))

        clock.now = clock.now.plus(Duration.ofMinutes(25))
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(4.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 4.0 / 60.0))

        clock.now = clock.now.plus(Duration.ofMinutes(60))
        assertThat(eventsStatistic.getEventStatisticByName(name)).isEqualTo(0.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(mapOf(name to 0.0))
    }

    @Test
    fun `multiple events statistic with changing time test`() {
        val nameFirst = "nameFirst"
        val nameSecond = "nameSecond"

        eventsStatistic.incEvent(nameFirst)
        eventsStatistic.incEvent(nameSecond)
        clock.now = clock.now.plus(Duration.ofMinutes(40))
        eventsStatistic.incEvent(nameSecond)
        assertThat(eventsStatistic.getEventStatisticByName(nameFirst)).isEqualTo(1.0 / 60.0)
        assertThat(eventsStatistic.getEventStatisticByName(nameSecond)).isEqualTo(2.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(
            mapOf(nameFirst to 1.0 / 60.0, nameSecond to 2.0 / 60.0)
        )

        clock.now = clock.now.plus(Duration.ofMinutes(40))
        assertThat(eventsStatistic.getEventStatisticByName(nameFirst)).isEqualTo(0.0)
        assertThat(eventsStatistic.getEventStatisticByName(nameSecond)).isEqualTo(1.0 / 60.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(
            mapOf(nameFirst to 0.0, nameSecond to 1.0 / 60.0)
        )

        clock.now = clock.now.plus(Duration.ofMinutes(40))
        assertThat(eventsStatistic.getEventStatisticByName(nameFirst)).isEqualTo(0.0)
        assertThat(eventsStatistic.getEventStatisticByName(nameSecond)).isEqualTo(0.0)
        assertThat(eventsStatistic.getAllEventStatistic()).isEqualTo(
            mapOf(nameFirst to 0.0, nameSecond to 0.0)
        )
    }

    @Test
    fun `printStatistic test`() {
        val nameFirst = "nameFirst"
        val nameSecond = "nameSecond"
        val nameThird = "nameThird"

        eventsStatistic.incEvent(nameFirst)
        eventsStatistic.incEvent(nameSecond)
        eventsStatistic.incEvent(nameSecond)
        eventsStatistic.incEvent(nameThird)
        eventsStatistic.incEvent(nameThird)
        eventsStatistic.incEvent(nameThird)

        val default = System.out
        val captor = ByteArrayOutputStream()

        System.setOut(PrintStream(captor))

        eventsStatistic.printStatistic()
        assertThat(captor.toString()).isEqualToIgnoringNewLines(
            """
            Event: nameFirst, rpm: 0.02
            Event: nameSecond, rpm: 0.03
            Event: nameThird, rpm: 0.05
            """.trimIndent()
        )

        System.setOut(default)
    }
}