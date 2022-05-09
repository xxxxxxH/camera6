package player.shellvoice.camera6.http

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import player.shellvoice.camera6.tools.xLogs
import java.net.HttpURLConnection
import java.net.URL


class Connection(private val context: Context) {
    private var connection: Connection? = null
    private var urlString: String? = ""

    private var requestType: String = RequestType.GET


    fun setSingletonInstance(connection: Connection?) {
        this.connection = connection
    }

    fun fromUrl(url: String): ConnectionParams {
        connection!!.urlString = url
        val connectionParams = ConnectionParams()
        connectionParams.setConnectionInstance(connection!!)
        return connectionParams
    }

    fun connect(requestComplete: OnNetworkRequest) {
        if (connection!!.urlString == "" || connection!!.urlString == null) {
            "URL cannot be empty".xLogs()
        } else {
            (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO) {
                var responseMessage = ""
                var errorStream = ""
                var responseCode = 0
                var result = ""
                val url = URL(connection!!.urlString)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.requestMethod = getRequestType()
                if (getRequestType() == RequestType.GET) {
                    connection.readTimeout = 10000
                    connection.connectTimeout = 10000
                }
                if (connection.responseCode == 200) {
                    responseCode = connection.responseCode
                    responseMessage = String(connection.inputStream.readBytes())
                    result = "success"
                } else {
                    responseCode = connection.responseCode
                    responseMessage = connection.responseMessage
                    errorStream = connection.errorStream.toString()
                    result = "failed"
                }
                withContext(Dispatchers.Main) {
                    if (result == "success") {
                        requestComplete.onSuccess(responseMessage)
                    } else {
                        requestComplete.onFailure(responseCode, responseMessage, errorStream)
                    }
                }
            }
        }
    }

    private fun getRequestType(): String {
        return requestType
    }

    fun setRequestType(requestType: String) {
        this.requestType = requestType
    }
}