package me.zhenhao.forcedroid.hook

import android.content.res.XResources
import android.util.Log
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.hooking.Hooker

/**
 * Created by tom on 6/13/17.
 */
class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        Log.d(SharedClassesSettings.TAG_XPD, "Ready to hook Webview...")
        XResources.setSystemWideReplacement("android", "string", "config_webViewPackageName", webviewPackageName)

        XposedHelpers.findAndHookMethod("android.webkit.WebViewFactory", null, "getWebViewPackageName", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                XposedBridge.log("getWebViewPackageName")
                param.result = webviewPackageName
            }
        })
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        Log.d(SharedClassesSettings.TAG_XPD, "<Package> ${loadPackageParam.packageName} <Process> ${loadPackageParam.processName}")

//        SelfHook(loadPackageParam).initAndHook()
        Hooker.initAndHook(loadPackageParam)
    }

    companion object {
        val webviewPackageName = "me.zhenhao.webview"
    }

}
