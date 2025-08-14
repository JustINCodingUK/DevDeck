package dev.justincodinguk.devdeck.core.probe.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DeferredSearchController(
    val searchDelayMillis: Long
) {

    private var isLocked: Boolean = false
    private val mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val searchQueryFlow = MutableStateFlow("")

    val searchQuery = searchQueryFlow.asStateFlow()

    fun queueSearch(searchText: String) {
        coroutineScope.launch {
            if(isLocked) { coroutineScope.cancel() }
            mutex.withLock { isLocked = true }
            delay(searchDelayMillis)
            searchQueryFlow.emit(searchText)
            mutex.withLock { isLocked = false }
        }
    }
}