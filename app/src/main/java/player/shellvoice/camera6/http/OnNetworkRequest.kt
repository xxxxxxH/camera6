package player.shellvoice.camera6.http

interface OnNetworkRequest {
    fun onSuccess(response: String?)
    fun onFailure(responseCode: Int, responseMessage: String, errorStream: String)
}