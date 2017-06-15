package me.zhenhao.forcedroid.fuzzdroid.wrapper;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by tom on 6/13/17.
 */

public class DummyWrapper {

    public static PackageInfo dummyWrapper_getPackageInfo(PackageManager manager, String packageName, int flags) {
        Log.i("SSE", "Dummy getPackage called");
        try {
            return manager.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String dummyWrapper_getProperty(Properties props, String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }


    public static String dummyWrapper_getProperty(Properties props, String key) {
        return props.getProperty(key);
    }


    public static Class<?> dummyWrapper_loadClass(String className, ClassLoader classLoader) {
        Log.i("SSE", "Dummy loadClass() called for " + className);
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className);
            //in case it does not exist, we use our dummy class
        } catch (ClassNotFoundException e) {
            try {
                clazz = DummyWrapper.class.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e2) {
                try{
                    clazz = classLoader.loadClass("de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass");
                    Log.i("SSE", "Dummy class returned for " + className);
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return clazz;
    }

    public static Method dummyWrapper_getMethod(Class clazz, String methodName, Class[] parameterTypes) {
        Log.i("SSE", String.format("Dummy getMethod() called for %s.%s",
                clazz.getName(), methodName));

        // For some methods, we need to inject our own implementations
        if (clazz.getName().equals("dalvik.system.DexFile") && methodName.equals("loadClass")) {
            try {
                Method m = DummyWrapper.class.getMethod("dummyWrapper_loadClass",
                        String.class, ClassLoader.class);
                Log.i("SSE", "Dummy getMethod() obtained: " + m);
                return m;
            } catch (NoSuchMethodException | SecurityException e) {
                Log.i("SSE", "Could not get dummy implementation for loadClass(), falling "
                        + "back to original one");
            }
        }

        Method method = null;
        try{
            method = clazz.getMethod(methodName, parameterTypes);
        }catch(Exception ex) {
            Log.i("SSE", "Could not find method, falling back to dummy");
            try{
                Class dummyClass = Class.forName("de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass");
                method = dummyClass.getMethod("dummyReflectionMethod", null);
            }catch(Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return method;
    }
}
