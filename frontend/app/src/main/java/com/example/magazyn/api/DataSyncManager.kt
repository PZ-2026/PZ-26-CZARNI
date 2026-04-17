package com.example.magazyn.api

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DataSyncManager {
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    val refreshTrigger = _refreshTrigger.asSharedFlow()

    suspend fun triggerUpdate() {
        _refreshTrigger.emit(Unit)
    }
}