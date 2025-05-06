package mdao.ahorraya.AhorraYa.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mdao.ahorraya.R
import mdao.ahorraya.ui.theme.*

@Preview(showBackground = true)
@Composable
fun InitialScreen(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), 
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ahorrar),
                contentDescription = "Logo",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(400.dp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = stringResource(R.string.que_no_te_duela),
                color = Coral,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.ahorra_ya),
                color = Coral,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = { navigateToSignUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(30.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coral,
                )
            ) {
                Text(
                    text = stringResource(R.string.reg_strate_gratis),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                modifier = Modifier.clickable { },
                painter = painterResource(id = R.drawable.google),
                title = stringResource(R.string.continuar_con_google)
            )

            Text(
                stringResource(R.string.iniciar_sesi_n),
                color = Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(24.dp)
                    .clickable { navigateToLogin() }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CustomButton(modifier: Modifier, painter: Painter, title: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 32.dp)
            .background(Color.White)
            .border(1.dp, Coral, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painter,
            contentDescription = "Icon",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(20.dp)
        )
        Text(
            text = title,
            color = Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
