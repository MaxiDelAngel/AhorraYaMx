package mdao.ahorraya.AhorraYa.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mdao.ahorraya.AhorraYa.repository.AuthManager
import mdao.ahorraya.AhorraYa.ui.models.Datos_Graficar
import mdao.ahorraya.AhorraYa.ui.models.Datos_guardar
import java.util.UUID


class ServiceViewModel: ViewModel() {
    private val db= Firebase.firestore

    private var _services= MutableStateFlow<List<Datos_guardar>>(emptyList())

    var listaServices = _services.asStateFlow()

    init {
        getServices()
    }

    fun getServices() {
        val user = AuthManager.getCurrentUser() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val resultado = db.collection("services")
                    .whereEqualTo("userId", user.uid)
                    .get().await()

                val services = resultado.documents.mapNotNull {
                    it.toObject(Datos_guardar::class.java)
                }

                _services.value = services
            } catch (e: Exception) {
                println("🔥 Error al obtener los datos: ${e.message}")
            }
        }
    }

    fun addService(service: Datos_guardar) {
        val user = AuthManager.getCurrentUser() ?: return
        service.id = UUID.randomUUID().toString()
        service.userId = user.uid

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("services").document(service.id).set(service).await()
                getServices()
            } catch (e: Exception) {
                println("🔥 Error al guardar: ${e.message}")
            }
        }
    }

    fun updateService(service: Datos_guardar) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("services").document(service.id).update(service.toMap()).await()
                getServices()
            } catch (e: Exception) {
                println("🔥 Error al actualizar: ${e.message}")
            }
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("services").document(id).delete().await()
                _services.value = listaServices.value.filter { it.id != id }
            } catch (e: Exception) {
                println("🔥 Error al eliminar servicio: ${e.message}")
            }
        }
    }

    data class BalancePorCuenta(
        val efectivo: Float = 0f,
        val tarjeta: Float = 0f,
        val total: Float = 0f
    )

    fun calcularBalance(): BalancePorCuenta {
        val servicios = listaServices.value
        var totalEfectivo = 0f
        var totalTarjeta = 0f

        try {
            for (servicio in servicios) {
                val monto = if (servicio.type == "Ingreso") servicio.amount else -servicio.amount

                when (servicio.account) {
                    "Efectivo" -> totalEfectivo += monto
                    "Tarjeta" -> totalTarjeta += monto
                }
            }

            return BalancePorCuenta(
                efectivo = totalEfectivo,
                tarjeta = totalTarjeta,
                total = totalEfectivo + totalTarjeta
            )
        } catch (e: Exception) {
            println("🔥 Error al calcular balance: ${e.message}")
            return BalancePorCuenta()
        }
    }

    fun obtenerDatosGraficos(): List<Datos_Graficar> {
        val servicios = listaServices.value

        try {
            return servicios
                .filter { it.type == "Gasto" }
                .groupBy { it.category }
                .map { (categoria, items) ->
                    val total = items.sumOf { it.amount.toDouble() }.toFloat()
                    Datos_Graficar(label = categoria, value = total)
                }
        } catch (e: Exception) {
            println("🔥 Error al obtener datos para gráfico: ${e.message}")
            return emptyList()
        }
    }
}