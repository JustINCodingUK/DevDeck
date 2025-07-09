package dev.justincodinguk.devdeck.nav

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import devdeck.composeapp.generated.resources.Res
import devdeck.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun Dashboard(modifier: Modifier = Modifier) {

    var currentScreen by remember { mutableStateOf(NavRailDestination.Feed) }

    NavigationRail(
        header = {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "DevDeck",
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp)
            )
        },
        modifier = modifier
    ) {

        NavRailDestination.entries.forEach { entry ->
            NavigationRailItem(
                selected = currentScreen == entry,
                onClick = { currentScreen = entry },
                icon = {
                    Icon(
                        imageVector = entry.icon,
                        contentDescription = entry.label
                    )
                },
                label = { Text(entry.label) },
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // TODO: Every screen ig
        when(currentScreen) {
            NavRailDestination.Feed -> Box(Modifier)
            NavRailDestination.MyDeck -> Box(Modifier)
            NavRailDestination.MyProjects -> Box(Modifier)
            NavRailDestination.NewProject -> Box(Modifier)
            NavRailDestination.Settings -> Box(Modifier)
            NavRailDestination.Downloads -> Box(Modifier)
            NavRailDestination.QuickCode -> Box(Modifier)
        }
    }
}

@Composable
@Preview
fun SideNavRailPreview() {

}