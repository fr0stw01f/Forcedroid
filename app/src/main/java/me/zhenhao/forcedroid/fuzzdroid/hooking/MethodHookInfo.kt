package me.zhenhao.forcedroid.fuzzdroid.hooking

import me.zhenhao.forcedroid.fuzzdroid.util.UtilHook
import java.util.regex.Pattern

/**
 * Created by tom on 6/8/17.
 */

class MethodHookInfo(private val methodSignature: String): HookInfo {
    val methodSig: String = methodSignature
    val className: String
        get
    val methodName: String
        get
    val params: Array<Class<*>>
        get

    val beforeHooks: MutableList<AbstractMethodHookBefore>
        get
    val afterHooks: MutableList<AbstractMethodHookAfter>
        get

    init {
        this.className = extractClassName(methodSignature)
        this.methodName = extractMethodName(methodSignature)
        this.params = extractParams(methodSignature)

        beforeHooks = ArrayList<AbstractMethodHookBefore>()
        afterHooks = ArrayList<AbstractMethodHookAfter>()
    }

    override fun toString(): String {
        return "MethodHookInfo: " + methodSig
    }

    private fun extractClassName(methodSignature: String): String {
        val pattern = "<(.*):.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun extractMethodName(methodSignature: String): String {
        val pattern = "<.*\\s(.*)\\(.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun extractParams(methodSignature: String): Array<Class<*>> {
        val pattern = "<.*\\((.*)\\)>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            val allParamTypes = m.group(1)
            //no params
            if (allParamTypes.equals(""))
                return arrayOf()
            else {
                val classTypes = allParamTypes.split(",")
                return UtilHook.getClassTypes(classTypes)
            }

        } else {
            throw RuntimeException("wrong format for param-type");
        }
    }


    fun persistentHookBefore(pairs: Set<Pair<Int, Any>>) {
        beforeHooks.add(PersistentMethodHookBefore(pairs))
    }

    fun analysisDependentHookBefore() {
        beforeHooks.add(AnalysisDependentMethodHookBefore(methodSignature))
    }

    fun conditionDependentHookBefore(paramConditions: Set<ParameterConditionValueInfo>) {
        beforeHooks.add(ConditionalMethodHookBefore(paramConditions))
    }

    fun persistentHookAfter(returnValue: Any) {
        afterHooks.add(PersistentMethodHookAfter(returnValue))
    }

    fun analysisDependentHookAfter() {
        afterHooks.add(AnalysisDependentMethodHookAfter(this.methodSignature));
    }

    fun dexFileExtractorHookBefore(argumentPosition: Int) {
        beforeHooks.add(DexFileExtractorHookBefore(methodSignature, argumentPosition))
    }

    fun simpleBooleanHookAfter() {
        afterHooks.add(SimpleBooleanHookAfter(methodSignature))
    }

    fun conditionDependentHookAfter(condition: Condition, returnValue: Any) {
        afterHooks.add(ConditionalMethodHookAfter(condition, returnValue))
    }

    fun hasHookBefore(): Boolean {
        return !beforeHooks.isEmpty()
    }

    fun hasHookAfter(): Boolean {
        return !afterHooks.isEmpty()
    }

}