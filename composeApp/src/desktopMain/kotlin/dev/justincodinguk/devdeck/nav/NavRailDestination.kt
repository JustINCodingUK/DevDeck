package dev.justincodinguk.devdeck.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavRailDestination(
    val icon: ImageVector,
    val label: String
) {
    Feed(
        icon = Icons.Filled.Newspaper,
        label = "Feed"
    ),
    NewProject(
        icon = Icons.Filled.Add,
        label = "New Project"
    ),
    MyProjects(
        icon = Icons.Filled.Code,
        label = "My Projects"
    ),
    MyDeck(
        icon = Icons.Filled.Computer,
        label = "My Deck"
    ),
    Downloads(
        icon = Icons.Filled.Download,
        label = "Downloads"
    ),
    QuickCode(
        icon = Icons.Filled.Bolt,
        label = "Quick Code"
    ),
    Settings(
        icon = Icons.Filled.Settings,
        label = "Settings"
    )
}
