package com.lhz.common

fun String.greet(): String = "Hello, $this!"

fun formatEmail(email: String): String =
    email.lowercase().trim()
