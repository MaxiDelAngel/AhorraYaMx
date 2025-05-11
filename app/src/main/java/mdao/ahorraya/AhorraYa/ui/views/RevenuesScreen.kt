package mdao.ahorraya.AhorraYa.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import mdao.ahorraya.AhorraYa.ui.models.Datos_guardar
import mdao.ahorraya.AhorraYa.ui.viewmodels.ServiceViewModel
import mdao.ahorraya.R
import mdao.ahorraya.ui.theme.BackgroundTextfield
import mdao.ahorraya.ui.theme.Black
import mdao.ahorraya.ui.theme.Coral
import mdao.ahorraya.ui.theme.ErrorForm
import mdao.ahorraya.ui.theme.SelectedNavbar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RevenuesScreen(auth: FirebaseAuth, snackbarHostState: SnackbarHostState) {
    val viewModel: ServiceViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val servicios by viewModel.listaServices.collectAsState()

    val balance = remember(servicios) {
        viewModel.calcularBalance()
    }

    var isIncome by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("") }
    var selectedAccount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = formatter.format(Date(selectedDate))

    val accountOptions = listOf("Efectivo", "Tarjeta")
    val categoryOptions = listOf("Comida", "Ropa", "Gasolina", "Transporte", "En general", "Entretenimiento")
    val categoryOptionsIncome = listOf("Salario", "Intereses", "Otros")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.registrar_movimiento),
                fontSize = 24.sp,
                color = Coral,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    isIncome = true
                    selectedAccount = ""
                    selectedCategory = ""
                    amount = ""
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) SelectedNavbar else BackgroundTextfield
                )
            ) {
                Text(stringResource(R.string.ingreso))
            }
            Button(
                onClick = {
                    isIncome = false
                    selectedAccount = ""
                    selectedCategory = ""
                    amount = ""
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isIncome) SelectedNavbar else BackgroundTextfield
                )
            ) {
                Text(stringResource(R.string.gasto))
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = {
                if (it.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                    amount = it
                }
            },
            label = { Text(text = stringResource(R.string.cantidad)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundTextfield, RoundedCornerShape(8.dp))
        )

        DropdownSelector(stringResource(R.string.cuenta), selectedAccount, accountOptions) {
            selectedAccount = it
        }

        DropdownSelector(stringResource(R.string.categor_a), selectedCategory,if (isIncome) categoryOptionsIncome else categoryOptions) {
            selectedCategory = it
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundTextfield)
                .clickable { showDatePicker = true }
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.fechaH, formattedDate), color = Black, fontSize = 16.sp)
            Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha", tint = Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (amount.isBlank()) {
                    errorMessage = "Por favor, ingrese una cantidad válida"
                    return@Button
                }

                if (!isIncome && selectedAccount === "Tarjeta" && amount.toFloat()>balance.tarjeta){
                    errorMessage = "No tienes suficiente dinero en tu tarjeta"
                    return@Button
                } else if (!isIncome && selectedAccount === "Efectivo" && amount.toFloat()>balance.efectivo){
                    errorMessage = "No tienes suficiente efectivo"
                    return@Button
                }

                val nuevoDato = Datos_guardar(
                    type = if (isIncome) "Ingreso" else "Gasto",
                    amount = amount.toFloat(),
                    account = selectedAccount,
                    category = selectedCategory,
                    date = formattedDate
                )

                viewModel.addService(nuevoDato)
                errorMessage = null

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Guardado exitosamente")
                }

                amount = ""
                selectedAccount = ""
                selectedCategory = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Coral)
        ) {
            Text(text = stringResource(R.string.guardar), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

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

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                onDateSelected = {
                    selectedDate = it
                    showDatePicker = false
                }
            )
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, color = Black)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundTextfield, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp
        ) {
            val datePickerState = rememberDatePickerState()

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatePicker(state = datePickerState)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(R.string.cancelar))
                    }
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            onDateSelected(millis)
                        }
                    }) {
                        Text(stringResource(R.string.aceptar))
                    }
                }
            }
        }
    }
}
