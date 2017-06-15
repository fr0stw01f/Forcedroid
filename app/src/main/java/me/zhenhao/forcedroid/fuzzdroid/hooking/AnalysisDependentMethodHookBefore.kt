package me.zhenhao.forcedroid.fuzzdroid.hooking

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.DecisionRequest
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.NetworkConnectionInitiator
import me.zhenhao.forcedroid.fuzzdroid.util.UtilHook

/**
 * Created by tom on 6/8/17.
 */
class AnalysisDependentMethodHookBefore(private val methodSignature: String) : AbstractMethodHookBefore() {

    private var paramValuesToReplace: Set<Pair<Int, Any>> = HashSet()
    private var needToChangeValues: Boolean = false

    override fun isValueReplacementNecessary(): Boolean {
        return needToChangeValues
    }

    fun retrieveValueFromServer(runtimeValues: Array<Any>) {
        // Make sure to always flush the trace before we ask for a decision
        //		BytecodeLogger.dumpTracingDataSynchronous();
        //		Log.i(SharedClassesSettings.TAG, "Flushed tracing queue to server");

        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, methodSignature, false)
        val preparedParameter = prepareParameterForExchange(runtimeValues)
        cRequest.runtimeValuesOfParams = preparedParameter

        val response = sc.getResultForRequest(cRequest)
        Log.i(SharedClassesSettings.TAG, "Retrieved decision from server")

        if (response != null) {
            needToChangeValues = response.doesResponseExist()
            if (needToChangeValues)
                paramValuesToReplace = response.paramValues
        }
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>> {
        return paramValuesToReplace
    }


    private fun prepareParameterForExchange(params: Array<Any>): Array<Any> {
        val preparedParams = arrayOfNulls<Any>(params.size)
        for (i in params.indices)
            preparedParams[i] = UtilHook.prepareValueForExchange(params[i])
        return preparedParams as Array<Any>
    }
}