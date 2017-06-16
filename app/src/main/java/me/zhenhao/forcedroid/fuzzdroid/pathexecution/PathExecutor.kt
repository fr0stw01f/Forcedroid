package me.zhenhao.forcedroid.fuzzdroid.pathexecution

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.tracing.BytecodeLogger
import java.util.*


/**
 * Created by tom on 6/8/17.
 */
object PathExecutor {

    fun logInfoAboutNonApiMethodAccess(methodSignature: String) {
        val codePosition = BytecodeLogger.getLastExecutedStatement().toLong()
        Log.i(SharedClassesSettings.TAG, String.format("%s || CodePos: %d || Method-Sign: %s || method access", SharedClassesSettings.METHOD_CALLEE_LABEL, codePosition, methodSignature))
    }

    @JvmOverloads fun logInfoAboutReturnStatement(methodSignature: String, returnValue: Any) {
        val codePosition = BytecodeLogger.getLastExecutedStatement().toLong()
        Log.i(SharedClassesSettings.TAG, String.format("%s || CodePos: %d || Method-Sign: %s || return %s", SharedClassesSettings.RETURN_LABEL, codePosition, methodSignature, concreteParameterValue(returnValue)))
    }

    fun logInfoAboutNonApiMethodCaller(methodSignature: String, invokeExprMethodSignature: String, vararg parameter: Any) {
        val codePosition = BytecodeLogger.getLastExecutedStatement().toLong()
        var invokeExprInfo = invokeExprMethodSignature + "("

        if (!parameter.isEmpty()) {
            for (i in parameter.indices) {
                if (i < parameter.size - 1) {
                    if (parameter[i] != null)
                        invokeExprInfo += concreteParameterValue(parameter[i]) + ", "
                    else
                        invokeExprInfo += "null, "
                } else {
                    if (parameter[i] != null)
                        invokeExprInfo += concreteParameterValue(parameter[i])
                    else
                        invokeExprInfo += "null"
                }

            }
        }
        invokeExprInfo += ")"

        Log.i(SharedClassesSettings.TAG, String.format("%s || CodePos: %d || Method-Sign: %s || InvokeExpr: %s", SharedClassesSettings.METHOD_CALLER_LABEL, codePosition, methodSignature, invokeExprInfo))
    }


    fun logInfoAboutBranchAccess(methodSignature: String, condition: String?, branchInfo: String?) {
        val codePosition = BytecodeLogger.getLastExecutedStatement().toLong()
        //before branch-condition
        if (branchInfo == null && condition != null)
            Log.i(SharedClassesSettings.TAG, String.format("[branch access condition] || CodePos: %d || Method-Sign: %s || %s", codePosition, methodSignature, condition))
        else if (condition == null && branchInfo != null)
            Log.i(SharedClassesSettings.TAG, String.format("[branch access decision] || CodePos: %d || Method-Sign: %s || %s", codePosition, methodSignature, branchInfo))
        else
            throw RuntimeException("there is a issue with the logInfoAboutBranchAccess method")
    }


    private fun concreteParameterValue(`object`: Any?): String {
        if (`object` == null)
            return "null"
        else if (`object` is Array<*>)
            return Arrays.deepToString(`object` as Array<Any>?)
        else if (`object` is String)
            return `object`.toString()
        else if (`object` is BooleanArray)
            return Arrays.toString(`object` as BooleanArray?)
        else if (`object` is ByteArray) {
            val byteArray = Arrays.toString(`object` as ByteArray?)
            var byteArrayToString: String? = null
            try {
                byteArrayToString = String((`object` as ByteArray?)!!)
            } catch (ex: Exception) {
                //do nothing
            }

            if (byteArrayToString == null)
                return byteArray
            else
                return byteArray + "\n" + byteArrayToString
        } //else if (`object` is Array<*>)
            //return Arrays.toString(`object` as Array<ByteArray>?)
        else if (`object` is ShortArray)
            return Arrays.toString(`object` as ShortArray?)
        else if (`object` is CharArray)
            return Arrays.toString(`object` as CharArray?)
        else if (`object` is IntArray)
            return Arrays.toString(`object` as IntArray?)
        else if (`object` is LongArray)
            return Arrays.toString(`object` as LongArray?)
        else if (`object` is FloatArray)
            return Arrays.toString(`object` as FloatArray?)
        else if (`object` is DoubleArray)
            return Arrays.toString(`object` as DoubleArray?)
        //else if (`object` is Array<*>)
            //return Arrays.deepToString(`object` as Array<Any>?)
        else
            return `object`.toString()
    }
}