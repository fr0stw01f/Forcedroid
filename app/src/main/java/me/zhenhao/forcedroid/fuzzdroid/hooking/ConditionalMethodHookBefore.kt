package me.zhenhao.forcedroid.fuzzdroid.hooking

import android.util.Log
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings


/**
 * Created by tom on 6/8/17.
 */
class ConditionalMethodHookBefore(private val paramConditions: Set<ParameterConditionValueInfo>) : AbstractMethodHookBefore() {

    private var valueReplacementNecessary = false

    private val newParamObjectPairs = HashSet<Pair<Int, Any>>()

    fun testConditionSatisfaction(originalMethodInfo: MethodHookParam) {
        Log.d(SharedClassesSettings.TAG_HOK, "Testing condition satisfaction...")
        for (paramConditionValuePair in paramConditions) {
            val paramCondition = paramConditionValuePair.condition
            if (paramCondition.isConditionSatisfied(originalMethodInfo)) {
                valueReplacementNecessary = true
                val paramIndex = paramConditionValuePair.paramIndex
                val newParamValue = paramConditionValuePair.newValue
                if (newParamValue != null)
                    newParamObjectPairs.add(Pair(paramIndex, newParamValue))
            }
        }
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>> {
        return newParamObjectPairs
    }

    override fun isValueReplacementNecessary(): Boolean {
        return valueReplacementNecessary
    }
}