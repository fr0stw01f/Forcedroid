package me.zhenhao.forcedroid.fuzzdroid.hookdefinitions

import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo

/**
 * Created by tom on 6/8/17.
 */
interface Hook {
    fun initializeHooks(): Set<HookInfo>
}