package me.zhenhao.forcedroid.fuzzdroid.hooking

import me.zhenhao.forcedroid.fuzzdroid.networkconnection.DecisionRequest
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.NetworkConnectionInitiator
import me.zhenhao.forcedroid.fuzzdroid.util.UtilHook

/**
 * Created by tom on 6/8/17.
 */
class AnalysisDependentFieldHookAfter(private val fieldSignature: String) : AbstractFieldHookAfter() {

    private var newValueAvailable: Boolean = false

    private var newValue: Any? = null

    fun retrieveValueFromServer(runtimeValue: Any) {
        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, fieldSignature, true)
        val cleanObject = UtilHook.prepareValueForExchange(runtimeValue)
        cRequest.runtimeValueOfReturn = cleanObject
        val response = sc.getResultForRequest(cRequest)
        if (response != null) {
            newValueAvailable = response.doesResponseExist()
            if (newValueAvailable)
                newValue = response.returnValue
        }
    }

    override fun isValueReplacementNecessary(): Boolean {
        return newValueAvailable
    }

    override fun getNewValue(): Any? {
        return newValue
    }

}