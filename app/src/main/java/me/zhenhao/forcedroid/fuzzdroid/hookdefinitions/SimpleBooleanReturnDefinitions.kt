package me.zhenhao.forcedroid.fuzzdroid.hookdefinitions

import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.MethodHookInfo



/**
 * Created by tom on 6/8/17.
 */
class SimpleBooleanReturnDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val booleanHooks = HashSet<HookInfo>()

        val getBooleanSP = MethodHookInfo("<android.app.SharedPreferencesImpl: boolean getBoolean(java.lang.String, boolean)>")
        getBooleanSP.simpleBooleanHookAfter()
        booleanHooks.add(getBooleanSP)

        return booleanHooks
    }

}