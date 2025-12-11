package com.example.levelup.util

object ValidadorUtil {

    // CORRECCIÓN: Esta Regex obliga a tener un punto y al menos 2 letras al final (ej: .cl, .com)
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun esEmailValido(email: String): Boolean {
        if (email.isBlank()) return false
        return email.matches(EMAIL_REGEX)
    }
}