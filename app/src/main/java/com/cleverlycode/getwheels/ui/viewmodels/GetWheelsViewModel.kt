package com.cleverlycode.getwheels.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cleverlycode.getwheels.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler

open class GetWheelsViewModel(private val logService: LogService) : ViewModel() {
    open val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    open val logErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    open fun onError(error: Throwable) {
        logService.logNonFatalCrash(error)
    }
}