package player.shellvoice.camera6.http

interface OnNetworkRequest {
    fun onStart()
    fun onSuccess(response: String?)
    fun onFailure(responseCode: Int, responseMessage: String, errorStream: String)
}