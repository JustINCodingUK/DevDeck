package dev.justincodinguk.devdeck.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExtendedIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    shape: Shape = RoundedCornerShape(8.dp),
    modifier: Modifier = Modifier
) {

    var hovered by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = modifier
            .clip(shape)
            .onPointerEvent(PointerEventType.Enter) {
                hovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                hovered = false
            }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .animateContentSize(animationSpec = tween(durationMillis = 220))
                .widthIn(min = 40.dp, max = 240.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            AnimatedVisibility(
                visible = hovered,
                enter = fadeIn(animationSpec = tween(180)),
                exit = fadeOut(animationSpec = tween(120))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnimatedButtonPreview() {
    DevDeckTheme {
        ExtendedIconButton({}, Icons.Filled.Download, "Download")
    }
}