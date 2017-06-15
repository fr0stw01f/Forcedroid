package me.zhenhao.forcedroid.fuzzdroid.networkconnection

/**
 * Created by tom on 6/8/17.
 */
object NetworkConnectionInitiator {

    var syncToken = Any()
    lateinit var serverCommunicator: ServerCommunicator
        private set

    fun initNetworkConnection() {
        serverCommunicator = ServerCommunicator(syncToken)
    }
}