package exceptions

class TurnstileException(message: String) : Exception(message)

class UnknownEventException : Exception()

class AccountExpiredException : Exception()

class AccountExistsException : Exception()

class UnknownAccountException : Exception()
