package dev.justincodinguk.devdeck.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devdeck.features.auth.generated.resources.Res
import devdeck.features.auth.generated.resources.bg_light
import devdeck.features.auth.generated.resources.bg_dark
import dev.justincodinguk.devdeck.core.ui.auth.AuthForm
import dev.justincodinguk.devdeck.core.ui.auth.Logo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    darkTheme: Boolean,
    viewModel: AuthViewModel = koinViewModel(),
    navigateToDashboard: () -> Unit,
) {
    val state by viewModel.authState.collectAsState()
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    if(state.authStatus == AuthStatus.LOGGED_IN) navigateToDashboard()
    
    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(if (darkTheme) Res.drawable.bg_dark else Res.drawable.bg_light),
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
                    .graphicsLayer { alpha = 0.8f }
                    .size(768.dp)
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    Tab(
                        text = {
                            Text(
                                "Login",
                                fontSize = 24.sp
                            )
                        },
                        selected = state.authMode == AuthMode.LOGIN,
                        onClick = {
                            viewModel.updateAuthMode(AuthMode.LOGIN)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        modifier = Modifier.height(64.dp)
                    )

                    Tab(
                        text = {
                            Text(
                                "Register",
                                fontSize = 24.sp
                            )
                        },
                        selected = state.authMode == AuthMode.REGISTER,
                        onClick = {
                            viewModel.updateAuthMode(AuthMode.REGISTER)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        modifier = Modifier.height(64.dp)
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    when(pageIndex) {
                        0 -> {
                            AuthForm(
                                headerText = "Welcome",
                                subHeaderText = "Log in to your account to continue",
                                submitButtonText = "LOG IN",
                                emailText = state.emailText,
                                passwordText = state.passwordText,
                                onEmailTextChange = viewModel::updateEmailText,
                                onPasswordTextChange = viewModel::updatePasswordText,
                                onSubmit = viewModel::loginWithEmail,
                                showOAuth = true,
                                onGithubLogin = viewModel::loginWithGithub,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        1-> {
                            AuthForm(
                                headerText = "New Account",
                                subHeaderText = "Create a new account with a valid email address",
                                submitButtonText = "REGISTER",
                                emailText = state.emailText,
                                passwordText = state.passwordText,
                                displayNameText = state.displayNameText,
                                onEmailTextChange = viewModel::updateEmailText,
                                onPasswordTextChange = viewModel::updatePasswordText,
                                onDisplayNameTextChange = viewModel::updateDisplayNameText,
                                onSubmit = viewModel::registerEmailAccount,
                                showOAuth = false,
                                showDisplayNameField = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.width(160.dp))
        }
    }
}


@Composable
@Preview
private fun LoginScreenPreview() {

}