package me.zhenhao.forcedroid.fuzzdroid.util

import me.zhenhao.forcedroid.fuzzdroid.hooking.DummyValue
import me.zhenhao.forcedroid.fuzzdroid.hooking.HookInfo
import me.zhenhao.forcedroid.hookdefinitions.KrepHookDefinitions


/**
 * Created by tom on 6/8/17.
 */
object UtilHook {
    fun prepareValueForExchange(obj: Any): Any {
        if (obj is String ||
                //primitive types
                obj is Byte ||
                obj is Short ||
                obj is Int ||
                obj is Long ||
                obj is Float ||
                obj is Double ||
                obj is Boolean ||
                obj is Char)
            return obj
        else
            return DummyValue()
    }

    fun getClassTypes(paramTypes: List<String>): Array<Class<*>> {
        val amountOfParams = paramTypes.size
        val classParamTypes = arrayOfNulls<Class<*>>(amountOfParams)
        for (i in 0..amountOfParams - 1) {
            classParamTypes[i] = getClassType(paramTypes[i].trim())
        }
        return classParamTypes as Array<Class<*>>
    }

    private fun getClassType(paramType: String): Class<*> {
        if (paramType == "byte")
            return Byte.javaClass
        else if (paramType == "short")
            return Short.javaClass
        else if (paramType == "int")
            return Int.javaClass
        else if (paramType == "long")
            return Long.javaClass
        else if (paramType == "float")
            return Float.javaClass
        else if (paramType == "double")
            return Double.javaClass
        else if (paramType == "boolean")
            return Boolean::class.java
        else if (paramType == "char")
            return Char.javaClass
        else if (paramType == "byte[]")
            return ByteArray::class.java
        else if (paramType == "short[]")
            return ShortArray::class.java
        else if (paramType == "int[]")
            return IntArray::class.java
        else if (paramType == "long[]")
            return LongArray::class.java
        else if (paramType == "float[]")
            return FloatArray::class.java
        else if (paramType == "double[]")
            return DoubleArray::class.java
        else if (paramType == "boolean[]")
            return BooleanArray::class.java
        else if (paramType == "char[]")
            return CharArray::class.java
        else {
            try {
                if (paramType.endsWith("[]")) {
                    val tmp = paramType.substring(0, paramType.indexOf("[]"))
                    val size = countMatch(paramType, "[]") - 1
                    val tmpClass = Class.forName(tmp)
                    return arrayOf(tmpClass, size).javaClass
                }
                return Class.forName(paramType)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
        throw RuntimeException("incorrect param-type")
    }

    private fun countMatch(string: String, findStr: String): Int {
        var lastIndex = 0
        var count = 0

        lastIndex = string.indexOf(findStr, lastIndex)
        while (lastIndex != -1) {
            count++
            lastIndex += findStr.length - 1
            lastIndex = string.indexOf(findStr, lastIndex)
        }
        return count
    }

    fun initAllHookers(): Set<HookInfo> {
        val allHookInfo = HashSet<HookInfo>()

//        allHookInfo.addAll(AnalysisDependentHookDefinitions().initializeHooks())
//        allHookInfo.addAll(DexFileExtractorHookDefinitions().initializeHooks())
//        allHookInfo.addAll(SimpleBooleanReturnDefinitions().initializeHooks())
//        allHookInfo.addAll(ConditionalHookDefinitions().initializeHooks())
//        allHookInfo.addAll(PersistentHookDefinitions().initializeHooks())

        allHookInfo.addAll(KrepHookDefinitions().initializeHooks())

        return allHookInfo
    }
}