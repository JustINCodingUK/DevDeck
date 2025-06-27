package dev.justincodinguk.devdeck.core.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devdeck.core.ui.generated.resources.Res
import devdeck.core.ui.generated.resources.logo
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "",
                modifier = Modifier.padding(4.dp).size(160.dp)
            )

            Text(
                "DevDeck",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(2.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 100.sp
            )
        }

        Text(
            "Organize. Build. Deploy.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 40.sp
        )

    }
}

@androidx.compose.desktop.ui.tooling.preview.Preview
@Composable
private fun LogoPreview() {
    DevDeckTheme {
        Logo()
    }
}