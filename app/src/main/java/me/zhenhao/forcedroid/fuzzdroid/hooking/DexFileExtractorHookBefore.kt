package me.zhenhao.forcedroid.fuzzdroid.hooking

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.tracing.BytecodeLogger
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by tom on 6/8/17.
 */
class DexFileExtractorHookBefore(private val methodSignature: String, val argumentPosition: Int) : AbstractMethodHookBefore() {


    fun sendDexFileToServer(dexFilePath: String) {
        val dexFile = File(dexFilePath)
        val dexFileBytes = convertFileToByteArray(dexFile)
        if (dexFileBytes != null) {
            BytecodeLogger.sendDexFileToServer(dexFilePath, dexFileBytes)
            Log.i(SharedClassesSettings.TAG, "dex file sent to client")
        }
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>> {
        // there is no need to replace any parameters
        return HashSet()
    }

    override fun isValueReplacementNecessary(): Boolean {
        //there is no need to replace anything
        return false
    }

    private fun convertFileToByteArray(dexFile: File): ByteArray? {
        var fin: FileInputStream? = null
        var fileContent: ByteArray? = null
        try {
            fin = FileInputStream(dexFile)
            fileContent = ByteArray(dexFile.length() as Int)
            fin.read(fileContent)
        } catch (e: FileNotFoundException) {
            Log.e(SharedClassesSettings.TAG, "File not found" + e)
        } catch (ioe: IOException) {
            Log.e(SharedClassesSettings.TAG, "Exception while reading file " + ioe)
        } finally {
            try {
                if (fin != null) {
                    fin.close()
                }
            } catch (ioe: IOException) {
                Log.e(SharedClassesSettings.TAG, "Error while closing stream: " + ioe)
            }

        }

        return fileContent
    }
}