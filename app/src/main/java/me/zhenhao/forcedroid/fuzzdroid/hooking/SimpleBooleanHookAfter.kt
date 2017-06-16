package me.zhenhao.forcedroid.fuzzdroid.hooking

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.DecisionRequest
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.NetworkConnectionInitiator

/**
 * Created by tom on 6/8/17.
 */
class SimpleBooleanHookAfter(private val methodSignature: String) : AbstractMethodHookAfter() {

    private var runtimeValueOfReturnAfterHooking: Any? = null

    private var runtimeValueOfReturnAvailable: Boolean = false

    fun retrieveBooleanValueFromServer() {
        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, methodSignature, true)

        val response = sc.getResultForRequest(cRequest)

        if (response == null) {
            Log.e(SharedClassesSettings.TAG, "NULL response received from server")
            runtimeValueOfReturnAvailable = false
            runtimeValueOfReturnAfterHooking = null
            return
        }

        Log.i(SharedClassesSettings.TAG, "Retrieved boolean decision from server")
        runtimeValueOfReturnAvailable = response.doesResponseExist()

        if (runtimeValueOfReturnAvailable) {
            runtimeValueOfReturnAfterHooking = response.returnValue
            Log.d(SharedClassesSettings.TAG, "Return value from server: " + runtimeValueOfReturnAfterHooking!!)
        } else
            Log.d(SharedClassesSettings.TAG, "Server had no response value for us")
    }

    override fun getReturnValue(): Any? {
        return runtimeValueOfReturnAfterHooking
    }

    override fun isValueReplacementNecessary(): Boolean {
        return runtimeValueOfReturnAvailable
    }

}