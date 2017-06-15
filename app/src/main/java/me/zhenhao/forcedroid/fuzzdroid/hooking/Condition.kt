package me.zhenhao.forcedroid.fuzzdroid.hooking

import de.robv.android.xposed.XC_MethodHook.MethodHookParam



/**
 * Created by tom on 6/8/17.
 */
interface Condition {
    fun isConditionSatisfied(param: MethodHookParam): Boolean
}