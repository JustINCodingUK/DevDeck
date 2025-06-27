package dev.justincodinguk.devdeck.core.ui.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devdeck.core.ui.generated.resources.Res
import devdeck.core.ui.generated.resources.github
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginUI(
    emailText: String,
    passwordText: String,
    onEmailTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGithubLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(64.dp)
    ) {
        Text(
            "Welcome",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(2.dp),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 60.sp
        )

        Text(
            "Login to your account to continue",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(0.dp),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(64.dp))

        OutlinedTextField(
            value = emailText,
            onValueChange = onEmailTextChange,
            modifier = Modifier
                .padding(4.dp)
                .width(512.dp),
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = passwordText,
            onValueChange = onPasswordTextChange,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(4.dp)
                .width(512.dp),
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            label = { Text("Password") }
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.width(240.dp)) {
            Text(
                "LOG IN",
                fontSize = 20.sp
            )
        }

        OrDivider()

        Button(
            onClick = onGithubLogin,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.width(300.dp)) {
            Image(
                painter = painterResource(Res.drawable.github),
                modifier = Modifier.size(36.dp),
                contentDescription = ""
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Continue with GitHub",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun LoginUIPreview() {
    DevDeckTheme(darkTheme = false) {
        LoginUI("HEYY", "meow", {}, {}, {},{})
    }
}