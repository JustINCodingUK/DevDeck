package dev.justincodinguk.devdeck.features.decks

import DevDeck.features.decks.BuildConfig
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.justincodinguk.devdeck.core.data.deck.DeckRepository
import dev.justincodinguk.devdeck.core.model.DeckFileModel
import dev.justincodinguk.devdeck.core.probe.background.BackgroundTaskManager
import dev.justincodinguk.devdeck.core.probe.search.DeferredSearchController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeckViewModel(
    private val backgroundTaskManager: BackgroundTaskManager,
    private val deferredSearchController: DeferredSearchController,
    private val deckRepository: DeckRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            deferredSearchController.searchQuery.collect {
                deckRepository.searchDeck(it).collect { decks ->
                    _state.emit(
                        state.value.copy(
                            searchBarState = state.value.searchBarState.copy(
                                isLoading = false,
                                results = decks.getOrDefault(listOf())
                            )
                        )
                    )
                }
            }
        }
    }

    private val _state = MutableStateFlow(DeckState())
    val state = _state.asStateFlow()

    fun beginDeckApplication(deckFileModel: DeckFileModel) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(preInstallationDeck = deckFileModel)
            )
        }
    }

    fun cancelDeckApplication() {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(preInstallationDeck = null)
            )
        }
    }

    fun applyDeck(deckFileModel: DeckFileModel) {
        viewModelScope.launch {
            deckRepository.runDeckFile(
                deckFileModel,
                BuildConfig.DECK_REFERENCE_DIR,
                3
            ).collect {
                if (it.isSuccess) {
                    backgroundTaskManager.attachTaskHandler(it.getOrNull()!!)
                }
            }
        }
    }

    fun selectDeck(id: String) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(selectedDeckId = id)
            )
        }
    }

    fun updateSearchBarText(newValue: String) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    searchBarState = state.value.searchBarState.copy(
                        searchBarText = newValue,
                        isLoading = true,
                        results = listOf()
                    )
                )
            )
        }
        deferredSearchController.queueSearch(newValue)
    }
}