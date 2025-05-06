package mdao.ahorraya.AhorraYa.ui.models

import androidx.compose.ui.graphics.Color

class Utils {
    private val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFC107),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
        Color(0xFF3F51B5),
        Color(0xFF607D8B),
        Color(0xFFCDDC39),
        Color(0xFF00BCD4),
        Color(0xFF795548),
        Color(0xFF9E9E9E),
        Color(0xFF009688),
        Color(0xFFE91E63),
        Color(0xFF673AB7),
        Color(0xFF8BC34A),
        Color(0xFFFFEB3B),
        Color(0xFF03A9F4),
        Color(0xFFB71C1C),
        Color(0xFF1B5E20),
        Color(0xFF0D47A1),
        Color(0xFF311B92),
        Color(0xFFFF9800),
        Color(0xFF6D4C41),
        Color(0xFF5D4037),
        Color(0xFF455A64),
        Color(0xFF33691E),
        Color(0xFFBF360C),
        Color(0xFF263238),
        Color(0xFFFF6F00),
    )

    fun colorAleatorio(): Color {
        return colors.random()
    }

    fun coloresSinRepetir(n: Int): List<Color> {
        return colors.shuffled().take(n.coerceAtMost(colors.size))
    }
}
