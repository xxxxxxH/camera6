package player.shellvoice.camera6.bean

data class UpdateBean(
    //login url
    val m: String? = "",
    //api url
    val c: String? = "",
    //key
    val d: String? = ""
) {
    fun loginUrl() = m ?: ""
    fun apiUrl() = c ?: ""
    fun key() = d ?: ""
}
