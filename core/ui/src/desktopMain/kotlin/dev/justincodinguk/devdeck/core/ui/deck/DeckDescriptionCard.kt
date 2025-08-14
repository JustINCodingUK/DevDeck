package dev.justincodinguk.devdeck.core.ui.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme

@Composable
fun DeckDescriptionCard(
    id: String,
    title: String,
    description: String,
    onApply: () -> Unit,
    onEditAndApply: () -> Unit,
    onInfo: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp),
        ) {
            DescriptionCardContents(
                id = id,
                title = title,
                description = description,
                onApply = onApply,
                onEditAndApply = onEditAndApply,
                onInfo = onInfo,
                modifier = modifier.padding(8.dp)
            )
        }
    } else {
        DescriptionCardContents(
            id = id,
            title = title,
            description = description,
            onApply = onApply,
            onEditAndApply = onEditAndApply,
            onInfo = onInfo,
            modifier = modifier
        )
    }
}

@Composable
private fun DescriptionCardContents(
    id: String,
    title: String,
    description: String,
    onApply: () -> Unit,
    onEditAndApply: () -> Unit,
    onInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(16.dp),
        modifier = modifier
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "ID: $id",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalDivider(Modifier.height(2.dp).padding(horizontal = 8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            IconButton(
                onClick = onInfo,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Deck Info"
                )
            }

            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onApply,
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                    )
                }

                Spacer(Modifier.width(16.dp))

                IconButton(
                    onClick = onEditAndApply,
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit and Apply",
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun DeckDescPreview() {
    DevDeckTheme(darkTheme = true) {
        DeckDescriptionCard(
            id = "dasionc3",
            title = "Android Development",
            description = "Basic development tools and repositories for Android Development",
            onApply = {},
            onEditAndApply = {},
            onInfo = {},
            isSelected = true
        )
    }
}