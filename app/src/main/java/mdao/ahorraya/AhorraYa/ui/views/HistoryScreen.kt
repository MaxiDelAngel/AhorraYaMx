package mdao.ahorraya.AhorraYa.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import mdao.ahorraya.AhorraYa.ui.models.Datos_guardar
import mdao.ahorraya.AhorraYa.ui.viewmodels.ServiceViewModel
import mdao.ahorraya.R
import mdao.ahorraya.ui.theme.BackgroundTextfield
import mdao.ahorraya.ui.theme.Black
import mdao.ahorraya.ui.theme.Coral

@Composable
fun HistoryScreen(auth: FirebaseAuth, snackbarHostState: SnackbarHostState) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ServiceViewModel = viewModel()
    val services by viewModel.listaServices.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var servicioSeleccionado by remember { mutableStateOf<Datos_guardar?>(null) }

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
                stringResource(R.string.historial_de_movimientos),
                fontSize = 24.sp,
                color = Coral,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(services) { service ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = BackgroundTextfield),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.tipoH, service.type),
                                color = Coral,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = stringResource(R.string.cantidadH, service.amount),
                                color = Black
                            )
                            Text(
                                text = stringResource(R.string.cuentaH, service.account),
                                color = Black
                            )
                            Text(
                                text = stringResource(R.string.categor_h, service.category),
                                color = Black
                            )
                            Text(
                                text = stringResource(R.string.fechaH, service.date),
                                color = Black
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    servicioSeleccionado = service
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Coral)
                                }

                                IconButton(onClick = {
                                    viewModel.deleteService(service.id)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Eliminado exito")
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Coral)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDialog && servicioSeleccionado != null) {
            val selected = servicioSeleccionado!!
            var cantidad by remember { mutableStateOf(selected.amount.toString()) }
            var categoria by remember { mutableStateOf(selected.category) }
            var cuenta by remember { mutableStateOf(selected.account) }
            var tipo by remember { mutableStateOf(selected.type) }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val updatedService = selected.copy(
                            amount = cantidad.toFloatOrNull() ?: selected.amount,
                            category = categoria,
                            account = cuenta,
                            type = tipo
                        )
                        viewModel.updateService(updatedService)
                        showDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Edición exitosa")
                        }
                    }) {
                        Text(stringResource(R.string.guardar))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.cancelar))
                    }
                },
                title = { Text(stringResource(R.string.editar_movimiento)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = cantidad,
                            onValueChange = { cantidad = it },
                            label = { Text(stringResource(R.string.cantidad)) }
                        )
                        OutlinedTextField(
                            value = categoria,
                            onValueChange = { categoria = it },
                            label = { Text(stringResource(R.string.categor_a)) }
                        )
                        OutlinedTextField(
                            value = cuenta,
                            onValueChange = { cuenta = it },
                            label = { Text(stringResource(R.string.cantidad)) }
                        )
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = { tipo = it },
                            label = { Text(stringResource(R.string.tipo)) }
                        )
                    }
                }
            )
        }
    }
}