package dev.justincodinguk.devdeck.features.decks

import dev.justincodinguk.devdeck.core.model.DeckFileModel

data class DeckState(
    val searchBarState: SearchBarState<DeckFileModel> = SearchBarState(),
    val preInstallationDeck: DeckFileModel? = null,
    val selectedDeckId: String? = null
)

data class SearchBarState<T>(
    val searchBarText: String = "",
    val isLoading: Boolean = false,
    val results: List<T> = listOf()
)