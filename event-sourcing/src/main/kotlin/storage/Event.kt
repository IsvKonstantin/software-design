package storage

import java.time.Duration
import java.time.Instant

sealed class Event(val login: String, val date: Instant)

class Enter(login: String, date: Instant) : Event(login, date)

class Exit(login: String, date: Instant) : Event(login, date)

class AccountCreated(login: String, date: Instant, val duration: Duration) : Event(login, date)

class AccountExtended(login: String, date: Instant, val duration: Duration) : Event(login, date)
