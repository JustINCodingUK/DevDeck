package dev.justincodinguk.devdeck.features.decks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.justincodinguk.devdeck.core.ui.deck.DeckDescriptionCard
import dev.justincodinguk.devdeck.core.ui.deck.DeckSearchBar
import dev.justincodinguk.devdeck.core.ui.deck.PreApplicationAlertDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeckScreen(
    viewModel: DeckViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Row(Modifier.fillMaxSize()) {
        DeckSearchBar(
            searchText = state.searchBarState.searchBarText,
            onSearchTextChange = viewModel::updateSearchBarText,
            results = state.searchBarState.results,
            isLoading = state.searchBarState.isLoading,
            resultItem = { deck ->
                DeckDescriptionCard(
                    id = deck.id,
                    title = deck.name,
                    description = deck.description,
                    onApply = { viewModel.beginDeckApplication(deck) },
                    onEditAndApply = {},
                    onInfo = {},
                    isSelected = if(state.selectedDeckId != null) {
                        state.selectedDeckId == deck.id
                    } else {
                        false
                    },
                    modifier = Modifier.clickable {
                        viewModel.selectDeck(deck.id)
                    }
                )
            },
            modifier = Modifier.weight(1f)
        )

        // TODO: Add a workflow window
        Box {}
    }

    if(state.preInstallationDeck != null) {
        PreApplicationAlertDialog(
            title = state.preInstallationDeck!!.name,
            id = state.preInstallationDeck!!.id,
            onApply = { viewModel.applyDeck(state.preInstallationDeck!!) },
            onCancel = viewModel::cancelDeckApplication
        )
    }
}