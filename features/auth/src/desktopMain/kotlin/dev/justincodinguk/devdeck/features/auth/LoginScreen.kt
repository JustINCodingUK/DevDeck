package dev.justincodinguk.devdeck.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import devdeck.features.auth.generated.resources.Res
import devdeck.features.auth.generated.resources.bg_light
import devdeck.features.auth.generated.resources.bg_dark
import dev.justincodinguk.devdeck.core.ui.login.LoginUI
import dev.justincodinguk.devdeck.core.ui.login.Logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    darkTheme: Boolean
) {
    val state by viewModel.authState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(if(darkTheme) Res.drawable.bg_dark else Res.drawable.bg_light),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(Modifier.align(Alignment.CenterStart)) {
            Spacer(Modifier.width(160.dp))
            Logo()
        }

        Row(Modifier.align(Alignment.CenterEnd)) {
            ElevatedCard(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.elevatedCardElevation(32.dp),
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.8f
                    }
                    .size(768.dp)
            ) {
                LoginUI(
                    emailText = state.emailText,
                    passwordText = state.passwordText,
                    onEmailTextChange = viewModel::updateEmailText,
                    onPasswordTextChange = viewModel::updatePasswordText,
                    onSubmit = {},
                    onGithubLogin = {
                        viewModel.loginWithGithub()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(Modifier.width(160.dp))
        }
    }
}


@Composable
@Preview
private fun LoginScreenPreview() {

}