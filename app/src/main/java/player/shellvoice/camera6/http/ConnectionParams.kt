package player.shellvoice.camera6.http

import org.json.JSONObject




class ConnectionParams {
    private var connection: Connection? = null

    fun setConnectionInstance(connectionInstance: Connection) {
        connection = connectionInstance
    }

    fun ofTypeGet(): Connection? {
        connection!!.setRequestType("GET")
        return connection
    }
}