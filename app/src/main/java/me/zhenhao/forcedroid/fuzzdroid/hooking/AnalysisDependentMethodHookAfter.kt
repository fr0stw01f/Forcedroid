package me.zhenhao.forcedroid.fuzzdroid.hooking

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.DecisionRequest
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.NetworkConnectionInitiator
import me.zhenhao.forcedroid.fuzzdroid.util.UtilHook

/**
 * Created by tom on 6/8/17.
 */
class AnalysisDependentMethodHookAfter(private val methodSignature: String) : AbstractMethodHookAfter() {

    private var runtimeValueOfReturnAfterhooking: Any? = null

    private var runtimeValueOfReturnAvailable: Boolean = false


    fun retrieveValueFromServer(runtimeValue: Any) {
        // Make sure to always flush the trace before we ask for a decision
        //		BytecodeLogger.dumpTracingDataSynchronous();
        //		Log.i(SharedClassesSettings.TAG, "Flushed tracing queue to server");

        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, methodSignature, true)
        val cleanObject = UtilHook.prepareValueForExchange(runtimeValue)
        cRequest.runtimeValueOfReturn = cleanObject
        val response = sc.getResultForRequest(cRequest)

        if (response == null) {
            Log.e(SharedClassesSettings.TAG, "NULL response received from server")
            runtimeValueOfReturnAvailable = false
            runtimeValueOfReturnAfterhooking = null
            return
        }

        Log.i(SharedClassesSettings.TAG, "Retrieved decision from server")
        runtimeValueOfReturnAvailable = response.doesResponseExist()

        if (runtimeValueOfReturnAvailable) {
            runtimeValueOfReturnAfterhooking = response.returnValue
            Log.d(SharedClassesSettings.TAG, "Return value from server: " + runtimeValueOfReturnAfterhooking!!)
        } else
            Log.d(SharedClassesSettings.TAG, "Server had no response value for us")
    }


    override fun getReturnValue(): Any? {
        return runtimeValueOfReturnAfterhooking
    }


    override fun isValueReplacementNecessary(): Boolean {
        return runtimeValueOfReturnAvailable
    }
}