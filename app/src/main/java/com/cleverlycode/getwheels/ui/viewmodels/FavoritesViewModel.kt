package com.cleverlycode.getwheels.ui.viewmodels

import com.cleverlycode.getwheels.data.remote.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    logService: LogService
) : GetWheelsViewModel(logService) {

}