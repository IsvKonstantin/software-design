package services

import exceptions.AccountExistsException
import exceptions.UnknownAccountException
import storage.AccountCreated
import storage.AccountExtended
import storage.EventStorage
import java.time.Duration
import java.time.LocalDateTime

data class Account(val login: String, val expirationDate: LocalDateTime)

class ManagerService(private val eventStorage: EventStorage) {

    private fun isAccountCreated(login: String): Boolean {
        return eventStorage.getEvents(login).any { it is AccountCreated }
    }

    fun getAccount(login: String): Account {
        val expirationDate = eventStorage.getAccountExpirationDate(login)
        return Account(login, expirationDate)
    }

    fun createAccount(login: String, duration: Duration) {
        if (isAccountCreated(login)) {
            throw AccountExistsException()
        }
        eventStorage.submitEvent(AccountCreated(login, LocalDateTime.now(), duration))
    }

    fun extendAccount(login: String, duration: Duration) {
        if (!isAccountCreated(login)) {
            throw UnknownAccountException()
        }
        eventStorage.submitEvent(AccountExtended(login, LocalDateTime.now(), duration))
    }
}