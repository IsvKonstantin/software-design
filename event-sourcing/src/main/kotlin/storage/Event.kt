package storage

import java.time.Duration
import java.time.LocalDateTime

sealed class Event(val login: String, val date: LocalDateTime)

class Enter(login: String, date: LocalDateTime) : Event(login, date)

class Exit(login: String, date: LocalDateTime) : Event(login, date)

class AccountCreated(login: String, date: LocalDateTime, val duration: Duration) : Event(login, date)

class AccountExtended(login: String, date: LocalDateTime, val duration: Duration) : Event(login, date)
