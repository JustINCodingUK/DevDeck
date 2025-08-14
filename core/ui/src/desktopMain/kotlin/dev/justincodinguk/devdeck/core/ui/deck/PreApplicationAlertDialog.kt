package dev.justincodinguk.devdeck.core.ui.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreApplicationAlertDialog(
    title: String,
    id: String,
    onApply: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(onClick = onApply) {
                Text("Apply")
            }
        },
        dismissButton = {
            ElevatedButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
        title = { Text("Warning") },
        text = { Text("You're about to apply the deck \"$title\" to your system.\nMake sure you trust the publisher and/or have gone through the deckfile yourself.\n\nThe ID of the deckfile is $id.") },
        modifier = modifier
    )
}

@Preview
@Composable
private fun AlertDialogPreview() {
    DevDeckTheme {
        PreApplicationAlertDialog(
            title = "Android Development",
            id = "jfj3d",
            onApply = {},
            onCancel = {}
        )
    }
}