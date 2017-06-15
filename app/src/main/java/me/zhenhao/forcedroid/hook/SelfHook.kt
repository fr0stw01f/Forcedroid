package me.zhenhao.forcedroid.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by tom on 6/3/17.
 */
class SelfHook(private val loadPackageParam: XC_LoadPackage.LoadPackageParam) {

    fun initAndHook() {
        if (isSelfPackage(loadPackageParam)) {
            findAndHookMethod(CLASSNAME_MAIN, loadPackageParam.classLoader, METHOD_NAME_IS_MODULE_ACTIVE, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam?) {
                    param!!.result = true
                }
            })
        }
    }

    private fun isSelfPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam): Boolean {
        return loadPackageParam.packageName == SELF_PACKAGENAME
    }

    companion object {
        private val METHOD_NAME_IS_MODULE_ACTIVE = "isModuleActive"
        private val SELF_PACKAGENAME = "me.zhenhao.forced"
        private val CLASSNAME_MAIN = "zhenhao.forced.view.Main"
    }
}
