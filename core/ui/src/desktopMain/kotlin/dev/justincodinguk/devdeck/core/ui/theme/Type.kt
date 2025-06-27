package dev.justincodinguk.devdeck.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import devdeck.core.ui.generated.resources.JetBrainsMono_Bold
import devdeck.core.ui.generated.resources.JetBrainsMono_BoldItalic
import devdeck.core.ui.generated.resources.JetBrainsMono_ExtraBold
import devdeck.core.ui.generated.resources.JetBrainsMono_Italic
import devdeck.core.ui.generated.resources.JetBrainsMono_Regular
import devdeck.core.ui.generated.resources.JetBrainsMono_Thin
import devdeck.core.ui.generated.resources.Monoton_Regular
import devdeck.core.ui.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun bodyFontFamily() = FontFamily(
    Font(Res.font.JetBrainsMono_Regular, weight = FontWeight.Normal),
    Font(Res.font.JetBrainsMono_Bold, weight = FontWeight.Bold),
    Font(Res.font.JetBrainsMono_Italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(Res.font.JetBrainsMono_BoldItalic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(Res.font.JetBrainsMono_ExtraBold, weight = FontWeight.ExtraBold),
    Font(Res.font.JetBrainsMono_Thin, weight = FontWeight.Thin)
)

@Composable
fun displayFontFamily() = FontFamily(
    Font(Res.font.Monoton_Regular, weight = FontWeight.Normal)
)

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun appTypography() = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily()),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily()),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily()),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily()),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily()),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily()),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily()),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily()),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily()),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily()),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily()),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily()),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily()),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily()),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily()),
)

