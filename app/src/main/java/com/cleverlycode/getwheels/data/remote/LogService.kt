package com.cleverlycode.getwheels.data.remote

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}