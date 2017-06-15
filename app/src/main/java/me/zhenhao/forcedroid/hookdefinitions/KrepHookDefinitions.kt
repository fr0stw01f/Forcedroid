package me.zhenhao.forcedroid.hookdefinitions

import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import me.zhenhao.forcedroid.fuzzdroid.hookdefinitions.Hook
import me.zhenhao.forcedroid.fuzzdroid.hooking.Condition
import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.MethodHookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.ParameterConditionValueInfo

/**
 * Created by tom on 6/13/17.
 */
class KrepHookDefinitions : Hook {
    override fun initializeHooks(): Set<HookInfo> {
        val krepHooks = HashSet<HookInfo>()
        krepHooks.addAll(initWebviewHooks())

        return krepHooks
    }

    fun initWebviewHooks() : Set<HookInfo> {
        val hooks = HashSet<HookInfo>()

        val loadUrl = MethodHookInfo("<android.webkit.WebView: void loadUrl(java.lang.String)>")
        val parameterInfo0 = HashSet<ParameterConditionValueInfo>()
        val arg1 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: XC_MethodHook.MethodHookParam): Boolean {
                Log.i("KREP", "${param.args[0]}")
                if ((param.args[0] as String).contains("google") || (param.args[0] as String).contains("secure"))
                    return true
                return false
            }
        }, "https://www.duckduckgo.com")
        parameterInfo0.add(arg1)
        loadUrl.conditionDependentHookBefore(parameterInfo0)

        hooks.add(loadUrl)

        return hooks
    }
}