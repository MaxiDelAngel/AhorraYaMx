package mdao.ahorraya.AhorraYa.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import mdao.ahorraya.AhorraYa.ui.models.AppScreens
import mdao.ahorraya.AhorraYa.ui.models.NavItems

object NavItemsList {
    val navItemsList = listOf(
        NavItems(
            label = "Inicio",
            icon = Icons.Filled.Home,
        ),
        NavItems(
            label = "Agregar",
            icon = Icons.Filled.AddCircle,
        ),
        NavItems(
            label = "Historial",
            icon = Icons.Filled.Info,
        ),
    )
}