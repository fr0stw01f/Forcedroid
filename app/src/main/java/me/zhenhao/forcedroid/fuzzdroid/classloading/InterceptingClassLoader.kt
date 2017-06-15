package me.zhenhao.forcedroid.fuzzdroid.classloading

import android.util.Log
import dalvik.system.DexFile
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings

/**
 * Created by tom on 6/8/17.
 */
object InterceptingClassLoader {

    @Throws(ClassNotFoundException::class)
    fun loadClass(dexFile: DexFile, className: String, classLoader: ClassLoader): Class<*> {
        try {
            Log.i(SharedClassesSettings.TAG, "Loading class " + className)
            // Try the default class loader
            return Class.forName(className)
        } catch (ex: ClassNotFoundException) {
            try {
                // Try the given class loader
                return classLoader.loadClass(className)
            } catch (ex2: ClassNotFoundException) {
                // We have no other choice than using the original class loading
                Log.w(SharedClassesSettings.TAG, "Could not intercept class loading")
                return dexFile.loadClass(className, classLoader)
            }

        }

    }

}
