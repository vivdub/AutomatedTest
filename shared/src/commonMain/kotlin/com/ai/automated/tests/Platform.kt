package com.ai.automated.tests

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform