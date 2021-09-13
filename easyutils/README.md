# 工具类说明

## ActivityExt
```
cls.startActivity 启动Activity
Context.startActivity 启动Activity
Intent.startActivity 启动Activity

cls.startActivityForResult 启动Activity有返回结果
Context.startActivityForResult 启动Activity有返回结果
Intent.startActivityForResult 启动Activity有返回结果

Activity.isKeyboardShowing 判断是否开启软键盘
createOptionsBundle 带跳转动画Bundle创建
```

## ActivityResultExt
```
internal class ActivityResultFragment 生成随机数并且直接监听结果的透明Fragment
class ActivityResultHelper 实现功能的帮助类

FragmentActivity.simpleActivityResult 打开Activity并且直接执行结果
Fragment.simpleActivityResult 打开Activity并且直接执行结果
```

## AnnotationsExt
```
Any.containAnnotation 判断类是否被注解
Field.containAnnotation 判断Field是否被注解
Any.getAnnotation 获取注解的对象
Any.getField 获取注解的成员变量
```

## AppExt
```
String.toPackageNameUri 包名转换为uri
Context.packageNameUri 获取包名
Context.installApp 安装app
Context.uninstallApp 卸载app
Context.isInstalled 判断app是否安装
Context.getAppLauncher 获取app启动页字符串
Context.isIntentAvailable 判断Intent是否可用
Context.openApp 打开app
Context.openAppDetailsSettings 打开app设置页
Context.openUnknownAppSettings 打开app未知来源设置页
Context.getAppPackageInfo 获取app包信息
Context.getAppIcon 获取app图标
Context.getAppIconId 获取app图标id
Context.getApplicationName 获取app应用名称
Context.getAppPath 获取app安装路径
Context.getAppVersionName 获取app版本名称
Context.getAppVersionCode 获取app版本代码

Context.getAppSignaturesSHA1 获取app SHA1签名
Context.getAppSignaturesMD5 获取app MD5签名
Context.getAppSignaturesSHA256 获取app SHA256签名
Context.getAppSignaturesHash 获取app签名哈希
Context.getAppSignatures 获取app签名
```

## AppManager App管理工具
```
addActivity 添加Activity
removeActivity 移除Activity
finishActivity 关闭Activity
isActivityExists 是否存在Activity
clearActivity 清空Activity
killApp 杀死app进程
```

## AutoScreenExt 今日头条适配方案
```
AutoScreenUtil 主类
    init 初始化
    setCustomDensity 设置视频
IAutoCancel 取消适配
IAutoChange 修改适配
```

## BarExt
```
Context.statusBarHeight 获取状态栏高度
Activity.setStatusBarVisibility 设置状态栏可见
Activity.isStatusBarVisible 状态栏是否可见
Activity.setStatusBarLightMode 设置状态栏模式
Activity.isStatusBarLightMode 状态栏是否开启light模式
Activity.setStatusBarColor 设置状态栏颜色
Activity.setStatusBarColor4Drawer 设置DrawerLayout下状态栏颜色
Activity.transparentStatusBar 状态栏透明
View.addMarginTopEqualStatusBarHeight 为view增加状态栏高度MarginTop
View.subtractMarginTopEqualStatusBarHeight 为view减少状态栏高度MarginTop

Application.actionBarHeight 获取动作栏高度

Application.setNotificationBarVisibility 设置通知栏是否可见

Context.navigationBarHeight 获取导航栏高度
Activity.setNavBarVisibility 设置导航栏可见
Activity.isNavBarVisible 导航栏是否可见
Application.isSupportNavBar 是否支持导航栏
Activity.setNavBarColor 设置导航栏颜色
Activity.navigationBarColor 获取导航栏颜色
Activity.setNavBarLightMode 设置导航栏模式
Activity.isNavBarLightMode 获取导航栏是否开启light模式
```

## BitmapExt
```
Bitmap.compress 压缩bitmap
*.toByte Bitmap、File、Int（Resource）、InputStream转字节
*.toBitmap 字节、File、Int（Resource）、InputStream转Bitmap
Int.vectorToBitmap 向量图转Bitmap

Bitmap.scale 缩放bitmap
Bitmap?.drawBitmap 在bitmap上面绘制一个bitmap
Bitmap?.drawMiniBitmap 在bitmap上面指定的9个位置绘制一个miniBitmap
Bitmap.blurBitmap 高斯模糊bitmap
```

## BrightnessExt
```
Context.isAutoBrightnessEnabled 是否开启自动亮度
Context.setAutoBrightnessEnabled 设置自动亮度
Context.getBrightness 获取屏幕亮度
Context.getMaxBrightness 获取屏幕最大亮度
Context.setBrightness 设置屏幕亮度

Window.getWindowBrightness 获取窗口屏幕亮度
Window.setWindowBrightness 设置窗口屏幕亮度（0-255）
Window.setWindowBrightnessRatio 设置窗口屏幕亮度（0.0-1.0）
```

## CancelExt
```
clearAppExternalCache 清除外部缓存 /mnt/sdcard/android/data/packageName/cache
clearAppInternalCache 清除内部缓存 /data/data/packageName/cache
clearAppFile 清除File文件 /data/data/packageName/files
clearAppSharedPrefs 清除SharedPreference /data/data/packageName/shared_prefs
clearAppDatabases 清除数据库 /data/data/packageName/databases
appExternalCacheSize 获取外部缓存文件大小
appInternalCacheSize 获取内部缓存文件大小
appFileSize 获取File文件文件大小
CancelUtils 缓存工具类
    clearAppAllData 清除全部缓存数据
    clearAppCancel 清除app内部和外部缓存数据
    getAppCancelSize 获取app内部和外部缓存文件大小
```

## ClickExt
```
View.click 单击点击
View.clickLong 长按点击
View.debouncingClick 防抖点击

setOnClickListeners 批量设置单击点击
setOnLongClickListeners 批量设置长按点击
setOnDebouncingClickListeners 批量设置防抖点击
```

## ClipboardExt
```
Context.getClipboardManager 获取剪切板管理器
Context.copyClipboardText 剪切Text
Context.clearClipboard 清空剪切板
Context.getClipboardLabel 获取剪切板标签
Context.getClipboardText 获取剪切板Text
Context.addClipboardChangedListener 添加剪切板改变监听
Context.removeClipboardChangedListener 移除剪切板改变监听
```

## ColorExt
```
Context.getColorRes 通过resource获取color
Int.changeColorAlpha 改变color的透明度
Int.changeRedColorAlpha 改变color的红色透明度
Int.changeGreenColorAlpha 改变color的绿色透明度
Int.changeBlueColorAlpha 改变color的蓝色透明度

Int.parseRgbColor color转字符串（无透明度）
Int.parseArgbColor color转字符串（有透明度）

randomColor 随即颜色
Int.isLightColor 是否为浅色
```

## CoroutineExt
```
launch 协程launch
launchMain 主线程协程launch
launchIO io线程协程launch

async 协程async
asyncMain 主线程协程async
asyncIO io线程协程async
```

## DeviceExt
```
isDeviceRooted 设备是否root
Context.isAdbEnabled 是否开启adb
sdkVersionName 获取设备版本名称(Build.VERSION.RELEASE)
sdkVersionCode 获取设备版本code(Build.VERSION.SDK_INT)
Context.androidID 获取设备AndroidID

Context.getMacAddress 获取设备Mac地址
Context.setWifiEnabled 修改wifi状态
Context.getWifiEnabled 获取wifi状态
```

## EncodeExt
```
String.urlEncode
String.urlDecode

base64Encode
base64Encode2String
base64Decode

String.binaryEncode
String.binaryDecode
```

## EncryptExt
```
encryptMD5ToString  MD5哈希加密
encryptHash2String 哈希加密

encryptHmacMD5ToString HmacMD5加密
encryptHmac2String Hmac加密

encryptDES DES加密
decryptDES DES解密

encrypt3DES 3DES加密
decrypt3DES 3DES解密

encryptAES AES加密
decryptAES AES解密

encryptRSA RSA加密
decryptRSA RSA解密

hashTemplate 哈希模板
hmacTemplate Hmac模板
symmetricTemplate 对称模板
rsaTemplate 非对称模板
rc4 rc4加密

ByteArray.splice 拼接Bytes
```
