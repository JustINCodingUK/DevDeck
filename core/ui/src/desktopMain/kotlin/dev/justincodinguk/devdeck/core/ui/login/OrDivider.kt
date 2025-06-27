package dev.justincodinguk.devdeck.core.ui.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrDivider() {
    Row(
        Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            Modifier.height(1.dp).width(120.dp))
        Text(
            "OR",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.primary,
        )
        HorizontalDivider(Modifier.height(1.dp).width(120.dp))
    }
}