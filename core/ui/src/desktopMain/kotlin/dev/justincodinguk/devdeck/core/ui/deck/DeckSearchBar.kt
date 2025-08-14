package dev.justincodinguk.devdeck.core.ui.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DeckSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    results: List<T>,
    resultItem: @Composable (T) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            label = {
                Text(text = "Search")
            },
            shape = RoundedCornerShape(32.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        HorizontalDivider(Modifier.height(1.dp).padding(8.dp))

        if(isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(results) {
                    resultItem(it)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    DevDeckTheme(darkTheme = false) {
        DeckSearchBar(
            searchText = "Search",
            onSearchTextChange = {},
            results = listOf(2,4,3,1,9),
            resultItem = {
                DeckDescriptionCard(
                    title = "Title",
                    id = "131mnidc",
                    description = "This is a description",
                    onApply = {},
                    onInfo = {},
                    onEditAndApply = {},
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            },
            isLoading = false
        )
    }
}