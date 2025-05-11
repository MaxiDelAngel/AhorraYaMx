package mdao.ahorraya.AhorraYa.ui.views

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.google.firebase.auth.FirebaseAuth
import mdao.ahorraya.AhorraYa.ui.models.Utils
import mdao.ahorraya.AhorraYa.ui.viewmodels.ServiceViewModel
import mdao.ahorraya.R
import mdao.ahorraya.ui.theme.Coral

@Composable
fun HomeScreen(auth: FirebaseAuth) {
    val viewModel: ServiceViewModel = viewModel()
    val servicios by viewModel.listaServices.collectAsState()

    val balance = remember(servicios) {
        viewModel.calcularBalance()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CardCarteras(stringResource(R.string.efectivo), balance.efectivo)
            CardCarteras(stringResource(R.string.tarjeta), balance.tarjeta)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            stringResource(R.string.distribuci_n_de_gastos),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GraficaPastel(viewModel = viewModel)

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.saldo_total),
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3ED))
            ) {
                Text(
                    text = "$${"%.2f".format(balance.total)}",
                    color = if (balance.total < 0) Color.Red else Coral,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 42.sp,
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CardCarteras(title: String, dinero: Float) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Coral)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$${"%.2f".format(dinero)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun GraficaPastel(viewModel: ServiceViewModel) {
    val servicios by viewModel.listaServices.collectAsState()
    val datos = remember(servicios) {
        viewModel.obtenerDatosGraficos()
    }

    if (datos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_hay_gastos_todav_a),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
            )
            CircularProgressIndicator(color = Coral)
        }
    } else {
        val colores = remember(datos) {
            Utils().coloresSinRepetir(datos.size)
        }

        val slices = remember(datos) {
            datos.mapIndexed { index, it ->
                PieChartData.Slice(
                    value = it.value,
                    color = colores[index]
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieChart(
                modifier = Modifier
                    .size(240.dp)
                    .padding(top = 12.dp),
                sliceDrawer = SimpleSliceDrawer(sliceThickness = 80f),
                pieChartData = PieChartData(slices),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                datos.forEachIndexed { index, it ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = slices[index].color, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = it.label, color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}


