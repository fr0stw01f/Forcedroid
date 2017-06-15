package me.zhenhao.forcedroid.fuzzdroid.hookdefinitions

import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.MethodHookInfo


/**
 * Created by tom on 6/8/17.
 */
class DexFileExtractorHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val dexFileHooks = HashSet<HookInfo>()

        val loadDex = MethodHookInfo("<dalvik.system.DexFile: dalvik.system.DexFile loadDex(java.lang.String, java.lang.String, int)>")
        loadDex.dexFileExtractorHookBefore(0)
        dexFileHooks.add(loadDex)

        return dexFileHooks
    }

}