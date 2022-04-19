package services

import exceptions.AccountExpiredException
import exceptions.TurnstileException
import storage.Enter
import storage.EventStorage
import storage.Exit
import java.time.Clock

class TurnstileService(private val eventStorage: EventStorage, private val clock: Clock) {

    private fun enterIsPossible(login: String) {
        val expirationDate = eventStorage.getAccountExpirationDate(login)

        when {
            clock.instant().isAfter(expirationDate) -> throw AccountExpiredException()
            eventStorage.getEvents(login).last() is Enter -> throw TurnstileException("'$login' already entered")
        }
    }

    private fun exitIsPossible(login: String) {
        if (eventStorage.getEvents(login).lastOrNull() !is Enter) {
            throw TurnstileException("'$login' already exited / did not enter")
        }
    }

    fun enter(login: String) {
        enterIsPossible(login)
        eventStorage.submitEvent(Enter(login, clock.instant()))
    }

    fun exit(login: String) {
        exitIsPossible(login)
        eventStorage.submitEvent(Exit(login, clock.instant()))
    }
}