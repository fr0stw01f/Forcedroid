package me.zhenhao.forcedroid.hook

import android.app.AndroidAppHelper
import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.tracing.BytecodeLogger

/**
 * Created by tom on 6/3/17.
 */
class SelfHook(private val loadPackageParam: XC_LoadPackage.LoadPackageParam) {

    fun initAndHook() {
        if (isSelfPackage(loadPackageParam)) {
            findAndHookMethod(CLASSNAME_MAIN, loadPackageParam.classLoader, METHOD_NAME_IS_MODULE_ACTIVE, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    param.result = true
                }
            })

            val context = AndroidAppHelper.currentApplication()
            Log.d(SharedClassesSettings.TAG_XPD, "Preparing to initialize bytecode logger with $context")
            BytecodeLogger.initialize(context)
        }
    }

    private fun isSelfPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam): Boolean {
        return loadPackageParam.packageName == SELF_PACKAGENAME
    }

    companion object {
        private val METHOD_NAME_IS_MODULE_ACTIVE = "isModuleActive"
        private val SELF_PACKAGENAME = "me.zhenhao.forcedroid"
        private val CLASSNAME_MAIN = "me.zhenhao.forcedroid.view.Main"
    }
}
