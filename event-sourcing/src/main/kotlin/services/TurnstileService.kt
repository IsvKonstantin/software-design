package services

import exceptions.AccountExpiredException
import exceptions.TurnstileException
import storage.Enter
import storage.EventStorage
import storage.Exit
import java.time.LocalDateTime

class TurnstileService(private val eventStorage: EventStorage) {

    private fun enterIsPossible(login: String) {
        val expirationDate = eventStorage.getAccountExpirationDate(login)

        when {
            expirationDate < LocalDateTime.now() -> throw AccountExpiredException()
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
        eventStorage.submitEvent(Enter(login, LocalDateTime.now()))
    }

    fun exit(login: String) {
        exitIsPossible(login)
        eventStorage.submitEvent(Exit(login, LocalDateTime.now()))
    }
}