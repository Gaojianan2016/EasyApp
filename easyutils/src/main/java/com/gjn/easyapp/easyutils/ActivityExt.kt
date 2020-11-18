package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import java.lang.reflect.Proxy

fun Class<*>.startActivity(activity: Activity, bundle: Bundle? = null) {
    activity.startActivity(Intent(activity, this).apply { bundle?.let { putExtras(it) } })
}

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        cls.startActivity(activity, bundle)
    }
}

class HookActivityUtils{

    companion object{
        const val HOOK_INTENT = "HOOK_INTENT"
    }

    @Throws(Throwable::class)
    fun hookStartActivity(context: Context, cls: Class<*>){

        try {
            //1 获取 ActivityManager 参数 gDefault
            val gDefaultField = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                "android.app.ActivityManager".toClass().getDeclaredField("IActivityManagerSingleton")
            }else{
                "android.app.ActivityManagerNative".toClass().getDeclaredField("gDefault")
            }
            gDefaultField.isAccessible = true
            val gDefault = gDefaultField.get(null)

            //2 获取 Singleton 参数 mInstance
            val singletonClass = "android.util.Singleton".toClass()
            val mInstanceField = singletonClass.getDeclaredField("mInstance")
            mInstanceField.isAccessible = true
            val iamInstance = mInstanceField.get(gDefault)

            //当前线程的classLoader
            val classLoader = Thread.currentThread().contextClassLoader
            //动态代理的接口对象
            val iamClass = "android.app.IActivityManager".toClass()

            val proxy = Proxy.newProxyInstance(classLoader, arrayOf(iamClass)
                //InvocationHandler 执行者
            ) { proxy, method, args ->
                "method -> ${method.name} \t args -> ${args.size}".logE()

                for (arg in args) {
                    "arg -> $arg".logD()
                }

                if (method.name == "startActivity") {
//                //获取原来的
//                var index = 0
//                var intent: Intent? = null
//                for (i in args.indices){
//                    if (args[i] is Intent) {
//                        index = i
//                        intent = args[i] as Intent
//                        break
//                    }
//                }
//                //创建一个安全的Intent
//                val safeIntent = Intent(activity, cls)
//                safeIntent.putExtra(HOOK_INTENT, intent)
//
//                //欺骗AMS
//                args[index] = safeIntent

                    //获取原来的
                    val intent: Intent = args[2] as Intent
                    //创建一个安全的Intent
                    val safeIntent = Intent(context, cls)
                    safeIntent.putExtra(HOOK_INTENT, intent)
                    //欺骗AMS
                    args[2] = safeIntent
                }
                return@newProxyInstance method.invoke(iamInstance, args)
            }
//            mInstanceField.set(gDefault, proxy)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}