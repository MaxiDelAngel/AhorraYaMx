package mdao.ahorraya.AhorraYa.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import mdao.ahorraya.R
import mdao.ahorraya.ui.theme.BackgroundTextfield
import mdao.ahorraya.ui.theme.Black
import mdao.ahorraya.ui.theme.Coral
import mdao.ahorraya.ui.theme.ErrorForm

@Composable
fun SignUpScreen(auth: FirebaseAuth, navigateToBack: () -> Unit, navigateToLogin: () -> Unit) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1f),
            contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Icono flecha regresar",
                tint = Black,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(24.dp)
                    .clickable { navigateToBack() }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            errorMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ErrorForm)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = message,
                            color = Coral,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Text(
                stringResource(R.string.create_account),
                color = Coral,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Create an account to control\nyour money",
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(58.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isEmailFocused) 2.dp else 0.dp,
                        color = if (isEmailFocused) Coral else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .onFocusChanged {
                        isEmailFocused = it.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundTextfield,
                    unfocusedContainerColor = BackgroundTextfield,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(R.string.email)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isPasswordFocused) 2.dp else 0.dp,
                        color = if (isPasswordFocused) Coral else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .onFocusChanged {
                        isPasswordFocused = it.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundTextfield,
                    unfocusedContainerColor = BackgroundTextfield,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(R.string.password)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                value = confirmpassword,
                onValueChange = { confirmpassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isConfirmPasswordFocused) 2.dp else 0.dp,
                        color = if (isConfirmPasswordFocused) Coral else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .onFocusChanged {
                        isConfirmPasswordFocused = it.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundTextfield,
                    unfocusedContainerColor = BackgroundTextfield,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(R.string.confirm_password)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
                        errorMessage = "Todos los campos son obligatorios"
                        return@Button
                    }

                    if (password != confirmpassword) {
                        errorMessage = "Las contraseñas no coinciden"
                        return@Button
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i("Auth", "Registro correcto")
                                navigateToLogin()
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Error al registrar"
                                Log.e("Auth", "Registro incorrecto: $errorMessage")
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coral,
                )
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(R.string.already_have_an_account),
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.clickable { navigateToLogin() }
            )
        }
    }
}