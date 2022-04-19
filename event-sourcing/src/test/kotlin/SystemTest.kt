import exceptions.AccountExistsException
import exceptions.AccountExpiredException
import exceptions.TurnstileException
import exceptions.UnknownAccountException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import services.ManagerService
import services.ReportService
import services.TurnstileService
import storage.EventStorage
import java.time.Clock
import java.time.Duration.ofDays
import java.time.Duration.ofHours
import java.time.Instant
import java.time.ZoneId

class SystemTest {
    private lateinit var clock: SettableClock
    private lateinit var eventStorage: EventStorage
    private lateinit var managerService: ManagerService
    private lateinit var reportService: ReportService
    private lateinit var turnstileService: TurnstileService

    @BeforeEach
    fun init() {
        clock = SettableClock(Instant.parse("2022-04-19T02:56:37.283580300Z"))

        eventStorage = EventStorage()
        turnstileService = TurnstileService(eventStorage, clock)
        managerService = ManagerService(eventStorage, clock)
        reportService = ReportService(eventStorage, clock)
    }

    @Test
    fun `manager service test`() {
        val login1 = "Faker"
        val login2 = "Chovy"
        val login3 = "Invalid"

        managerService.createAccount(login1, ofDays(25))
        managerService.createAccount(login2, ofDays(15))

        assertThat(managerService.getAccount(login1).login).isEqualTo(login1)
        assertThat(managerService.getAccount(login2).login).isEqualTo(login2)

        val expirationDate = managerService.getAccount(login1).expirationDate
        managerService.extendAccount(login1, ofDays(25))
        val newExpirationDate = managerService.getAccount(login1).expirationDate

        assertThat(newExpirationDate).isEqualTo(expirationDate + ofDays(25))

        assertThatThrownBy {
            managerService.extendAccount(
                login3,
                ofDays(25)
            )
        }.isInstanceOf(UnknownAccountException::class.java)

        assertThatThrownBy {
            managerService.createAccount(
                login1,
                ofDays(25)
            )
        }.isInstanceOf(AccountExistsException::class.java)
    }

    @Test
    fun `turnstile service test`() {
        val login1 = "Faker"
        val login2 = "Chovy"
        val login3 = "Invalid"

        managerService.createAccount(login1, ofDays(25))
        managerService.createAccount(login2, ofDays(15))

        assertThatThrownBy {
            turnstileService.exit(login1)
        }.isInstanceOf(TurnstileException::class.java)
            .hasMessage("'$login1' already exited / did not enter")
        assertThatThrownBy {
            turnstileService.exit(login2)
        }.isInstanceOf(TurnstileException::class.java)
            .hasMessage("'$login2' already exited / did not enter")

        turnstileService.enter(login1)

        assertThatThrownBy {
            turnstileService.enter(login1)
        }.isInstanceOf(TurnstileException::class.java)
            .hasMessage("'$login1' already entered")
        assertThatThrownBy {
            turnstileService.exit(login2)
        }.isInstanceOf(TurnstileException::class.java)
            .hasMessage("'$login2' already exited / did not enter")

        turnstileService.exit(login1)

        assertThatThrownBy {
            turnstileService.exit(login3)
        }.isInstanceOf(TurnstileException::class.java)
            .hasMessage("'$login3' already exited / did not enter")

        clock.plusDays(20)
        turnstileService.enter(login1)
        assertThatThrownBy { turnstileService.enter(login2) }
            .isInstanceOf(AccountExpiredException::class.java)
    }

    @Test
    fun `report service test`() {
        val login1 = "Chovy"
        val login2 = "Faker"

        managerService.createAccount(login1, ofDays(25))
        managerService.createAccount(login2, ofDays(25))

        turnstileService.enter(login1)
        turnstileService.enter(login2)
        clock.plusHours(6)
        turnstileService.exit(login1)
        turnstileService.exit(login2)

        clock.plusDays(1)

        turnstileService.enter(login1)
        clock.plusHours(3)
        turnstileService.exit(login1)
        clock.plusHours(1)
        turnstileService.enter(login1)
        clock.plusHours(3)
        turnstileService.exit(login1)
        clock.plusHours(1)
        turnstileService.enter(login1)
        clock.plusHours(3)
        turnstileService.exit(login1)


        clock.plusDays(3)

        turnstileService.enter(login2)
        clock.plusHours(4)
        turnstileService.exit(login2)

        val weekStats = reportService.getWeekStats()
        val duration = reportService.getTotalDuration().toHours()
        val average = reportService.getAverageVisitsDaily()

        assertThat(average).isEqualTo(2.0)
        assertThat(duration).isEqualTo(25)
        assertThat(weekStats.toSortedMap().values.toList()).isEqualTo(listOf(2, 3, 1))

        println(weekStats)
        println(duration)
        println(average)
    }
}

class SettableClock(time: Instant) : Clock() {
    private var now: Instant

    init {
        now = time.atZone(ZoneId.systemDefault()).toInstant()
    }

    override fun instant(): Instant {
        return now
    }

    override fun getZone(): ZoneId {
        return ZoneId.systemDefault()
    }

    override fun withZone(zone: ZoneId): Clock {
        throw UnsupportedOperationException()
    }

    fun plusDays(days: Long) {
        now = now.plus(ofDays(days))
    }

    fun plusHours(hours: Long) {
        now = now.plus(ofHours(hours))
    }
}