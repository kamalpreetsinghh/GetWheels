package com.cleverlycode.getwheels.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}