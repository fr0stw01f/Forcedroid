package me.zhenhao.forcedroid.fuzzdroid.hookdefinitions

import android.telephony.PhoneNumberUtils
import android.util.Log
import dalvik.system.DexClassLoader
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import me.zhenhao.forcedroid.fuzzdroid.hooking.Condition
import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.MethodHookInfo
import me.zhenhao.forcedroid.fuzzdroid.hooking.ParameterConditionValueInfo


/**
 * Created by tom on 6/8/17.
 */
class ConditionalHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val allConditionalHooks = HashSet<HookInfo>()
        //		allConditionalHooks.addAll(fileSpecificEmulatorChecks());
        //		allConditionalHooks.addAll(systemPropEmulatorChecks());
        //		allConditionalHooks.addAll(appSpecificEmulatorChecks());
        allConditionalHooks.addAll(textMessageCrashPrevention())
        //		allConditionalHooks.addAll(reflectionHooks());
        return allConditionalHooks
    }


    private fun textMessageCrashPrevention(): Set<HookInfo> {
        val textMessageHooks = HashSet<HookInfo>()

        val smsManagerSendTextMessage = MethodHookInfo("<android.telephony.SmsManager: void sendTextMessage(java.lang.String, java.lang.String, java.lang.String, android.app.PendingIntent, android.app.PendingIntent)>")
        val parameterInfos1 = HashSet<ParameterConditionValueInfo>()
        val arg1 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                    return true
                return false
            }
        }, "555555")
        parameterInfos1.add(arg1)
        smsManagerSendTextMessage.conditionDependentHookBefore(parameterInfos1)
        textMessageHooks.add(smsManagerSendTextMessage)

        val smsManagerSendMultipartTextMessage = MethodHookInfo("<android.telephony.SmsManager: void sendMultipartTextMessage(java.lang.String, java.lang.String, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)>")
        val parameterInfos2 = HashSet<ParameterConditionValueInfo>()
        val arg2 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                    return true
                return false
            }
        }, "555555")
        parameterInfos2.add(arg2)
        smsManagerSendMultipartTextMessage.conditionDependentHookBefore(parameterInfos2)
        textMessageHooks.add(smsManagerSendMultipartTextMessage)

        val gsmSendTextMessage = MethodHookInfo("<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String, java.lang.String, java.lang.String, android.app.PendingIntent, android.app.PendingIntent)>")
        val parameterInfos3 = HashSet<ParameterConditionValueInfo>()
        val arg3 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                    return true
                return false
            }
        }, "555555")
        parameterInfos3.add(arg3)
        gsmSendTextMessage.conditionDependentHookBefore(parameterInfos3)
        textMessageHooks.add(gsmSendTextMessage)

        val gsmSendMultiTextMessage = MethodHookInfo("<android.telephony.gsm.SmsManager: void sendMultipartTextMessage(java.lang.String, java.lang.String, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)>")
        val parameterInfos4 = HashSet<ParameterConditionValueInfo>()
        val arg4 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                    return true
                return false
            }
        }, "555555")
        parameterInfos4.add(arg4)
        gsmSendMultiTextMessage.conditionDependentHookBefore(parameterInfos4)
        textMessageHooks.add(gsmSendMultiTextMessage)

        return textMessageHooks
    }


    private fun fileSpecificEmulatorChecks(): Set<HookInfo> {
        val fileHooks = HashSet<HookInfo>()

        val exists = MethodHookInfo("<java.io.File: boolean exists()>")
        //qemu_pipe check
        exists.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.thisObject.toString() == "/dev/qemu_pipe")
                    return true
                return false
            }
        }, false)
        //qemud check
        exists.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.thisObject.toString() == "/dev/socket/qemud")
                    return true
                return false
            }
        }, false)
        //goldfish check
        exists.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.thisObject.toString() == "/init.goldfish.rc")
                    return true
                return false
            }
        }, false)
        //qemu_trace check
        exists.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.thisObject.toString() == "/sys/qemu_trace")
                    return true
                return false
            }
        }, false)


        fileHooks.add(exists)
        return fileHooks
    }


    private fun systemPropEmulatorChecks(): Set<HookInfo> {
        val systemPropHooks = HashSet<HookInfo>()

        val systemPropGet = MethodHookInfo("<android.os.SystemProperties: java.lang.String get(java.lang.String)>")
        //gsm.sim.operator.alpha check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "gsm.sim.operator.alpha")
                    return true
                return false
            }
        }, "T-mobile D")
        //gsm.operator.numeric check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "gsm.operator.numeric")
                    return true
                return false
            }
        }, "26201")
        //gsm.sim.operator.numeric check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "gsm.sim.operator.numeric")
                    return true
                return false
            }
        }, "8923440000000000003")
        //gsm.version.ril-impl check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "gsm.version.ril-impl")
                    return true
                return false
            }
        }, "")
        //ro.baseband check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.baseband")
                    return true
                return false
            }
        }, "")
        //ro.bootloader check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.bootloader")
                    return true
                return false
            }
        }, "PRIMEMD04")
        //ro.build.description check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.description")
                    return true
                return false
            }
        }, "")
        //ro.build.display.id check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.display.id")
                    return true
                return false
            }
        }, "JWR66Y")
        //ro.build.fingerprint check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.fingerprint")
                    return true
                return false
            }
        }, "google/takju/maguro:4.3/JWR66Y/776638:user/release-keys")
        //ro.build.tags check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.tags")
                    return true
                return false
            }
        }, "release-keys")
        //ro.build.user check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.user")
                    return true
                return false
            }
        }, "android-build")
        //ro.hardware check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.hardware")
                    return true
                return false
            }
        }, "tuna")
        //ro.product.board check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.product.board")
                    return true
                return false
            }
        }, "tuna")
        //ro.product.brand check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.product.brand")
                    return true
                return false
            }
        }, "google")
        //ro.product.device check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.product.device")
                    return true
                return false
            }
        }, "maguro")
        //ro.product.manufacturer check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.product.manufacturer")
                    return true
                return false
            }
        }, "samsung")
        //ro.product.name check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.product.name")
                    return true
                return false
            }
        }, "takju")
        //ro.serialno check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.serialno")
                    return true
                return false
            }
        }, "0149E08209007013")
        //ro.setupwizard.mode check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.setupwizard.mode")
                    return true
                return false
            }
        }, "")
        //ro.build.type check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.build.type")
                    return true
                return false
            }
        }, "user")
        //ARGH check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ARGH")
                    return true
                return false
            }
        }, "")
        //init.svc.goldfish-logcat check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "init.svc.goldfish-logcat")
                    return true
                return false
            }
        }, "")
        //init.svc.goldfish-setup check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "init.svc.goldfish-setup")
                    return true
                return false
            }
        }, "")
        //init.svc.qemud check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "init.svc.qemud")
                    return true
                return false
            }
        }, "")
        //qemu.hw.mainkeys check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "qemu.hw.mainkeys")
                    return true
                return false
            }
        }, "")
        //init.svc.qemu-props check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "init.svc.qemu-props")
                    return true
                return false
            }
        }, "")
        //qemu.sf.fake_camera check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "qemu.sf.fake_camera")
                    return true
                return false
            }
        }, "")
        //qemu.sf.lcd_density check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "qemu.sf.lcd_density")
                    return true
                return false
            }
        }, "")
        //ro.kernel.android.checkjni check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.android.checkjni")
                    return true
                return false
            }
        }, "")
        //ro.kernel.android.qemud check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.android.qemud")
                    return true
                return false
            }
        }, "")
        //ro.kernel.console check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.console")
                    return true
                return false
            }
        }, "")
        //ro.kernel.ndns check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.ndns")
                    return true
                return false
            }
        }, "")
        //ro.kernel.qemu.gles check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.qemu.gles")
                    return true
                return false
            }
        }, "")
        //ro.kernel.qemu check
        systemPropGet.conditionDependentHookAfter(object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "ro.kernel.qemu")
                    return true
                return false
            }
        }, "")

        systemPropHooks.add(systemPropGet)

        return systemPropHooks
    }


    private fun appSpecificEmulatorChecks(): Set<HookInfo> {
        val appSpecificHooks = HashSet<HookInfo>()

        val addCategory = MethodHookInfo("<android.content.Intent: android.content.Intent addCategory(java.lang.String)>")

        val parameterInfos = HashSet<ParameterConditionValueInfo>()
        val arg1 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                if (param.args[0].toString() == "android.intent.category.APP_MARKET")
                    return true
                return false
            }
        }, "android.intent.category.LAUNCHER")
        parameterInfos.add(arg1)
        addCategory.conditionDependentHookBefore(parameterInfos)

        appSpecificHooks.add(addCategory)

        return appSpecificHooks
    }

    private fun reflectionHooks(): Set<HookInfo> {
        val reflectionHooks = HashSet<HookInfo>()

        val dexClassLoaderLoadClass = MethodHookInfo("<dalvik.system.DexClassLoader: java.lang.Class loadClass(java.lang.String)>")
        val parameterInfos = HashSet<ParameterConditionValueInfo>()
        val arg0 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                //we check if there is a class available
                try {
                    Log.i("SSE1", "in loadClass")
                    val dcl = param.thisObject as DexClassLoader
                    dcl.loadClass(param.args[0] as String)
                } catch (ex: Exception) {
                    return true
                }
                return false
            }
        }, "de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass")
        parameterInfos.add(arg0)
        dexClassLoaderLoadClass.conditionDependentHookBefore(parameterInfos)
        reflectionHooks.add(dexClassLoaderLoadClass)

        val classGetMethod = MethodHookInfo("<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>")
        val parameterInfosClassGetMethod = HashSet<ParameterConditionValueInfo>()
        val classGetMethodArg0 = ParameterConditionValueInfo(0, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                //we check if there is a class available
                try {
                    Log.i("SSE1", "in getMethod")
                    val clazz = param.thisObject as Class<*>
                    clazz.getMethod(param.args[0] as String, *param.args[1] as Array<Class<*>>)
                } catch (ex: Exception) {
                    return true
                }
                return false
            }
        }, "dummyReflectionMethod")

        val classGetMethodArg1 = ParameterConditionValueInfo(1, object : Condition {
            override fun isConditionSatisfied(param: MethodHookParam): Boolean {
                //we check if there is a class available
                try {
                    val clazz = param.thisObject as Class<*>
                    clazz.getMethod(param.args[0] as String, *param.args[1] as Array<Class<*>>)
                } catch (ex: Exception) {
                    return true
                }
                return false
            }
        }, null)

        parameterInfosClassGetMethod.add(classGetMethodArg0)
        parameterInfosClassGetMethod.add(classGetMethodArg1)
        classGetMethod.conditionDependentHookBefore(parameterInfosClassGetMethod)
        reflectionHooks.add(classGetMethod)

        return reflectionHooks
    }
}