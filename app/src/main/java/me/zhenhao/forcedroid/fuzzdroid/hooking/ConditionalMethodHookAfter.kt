package me.zhenhao.forcedroid.fuzzdroid.hooking

import de.robv.android.xposed.XC_MethodHook.MethodHookParam


/**
 * Created by tom on 6/8/17.
 */
class ConditionalMethodHookAfter(private val condition: Condition, private val returnValue: Any) : AbstractMethodHookAfter() {

    private var valueReplacementNecessary: Boolean = false


    fun testConditionSatisfaction(originalMethodInfo: MethodHookParam) {
        valueReplacementNecessary = condition.isConditionSatisfied(originalMethodInfo)
    }


    override fun getReturnValue(): Any? {
        return returnValue
    }


    override fun isValueReplacementNecessary(): Boolean {
        return valueReplacementNecessary
    }

}