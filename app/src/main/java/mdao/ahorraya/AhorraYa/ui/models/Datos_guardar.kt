package mdao.ahorraya.AhorraYa.ui.models

data class Datos_guardar(
    var id: String = "",
    val type: String = "",
    val amount: Float = 0f,
    val account: String = "",
    val category: String = "",
    val date: String = "",
    var userId: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to type,
            "amount" to amount,
            "account" to account,
            "category" to category,
            "date" to date,
            "userId" to userId
        )
    }
}

