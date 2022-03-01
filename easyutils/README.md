# 工具类说明

## ActivityExt
```
Context.startActivity 启动Activity
Context.startActivityForResult 启动Activity有返回结果

Activity.finishActivity 关闭Activity
Activity.finishWithResult 关闭Activity并返回数据

Activity.contentFrameLayout 获取android.R.id.content帧布局对象
Activity.decorViewGroup 获取decorView布局对象

Activity.rootWindowInsetsCompat 根布局插入兼容
Activity.windowInsetsControllerCompat 窗口插入控制兼容

Activity.getIntentKey 获取Activity Intent对象key值

Activity.contentViewInvisibleHeight 获取android.R.id.content未显示高度
Activity.decorViewInvisibleHeight 获取decorView未显示高度
Activity.getViewInvisibleHeight 获取Activity中子view未显示高度

```

## ActivityResultExt
```
FragmentActivity.quickActivityResult 打开Activity并且直接执行结果
Fragment.quickActivityResult 打开Activity并且直接执行结果
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
Context.toPackageNameUri 包名转换为uri

Context.installApp 安装app
Context.uninstallApp 卸载app
Context.isInstalled 判断app是否安装

Context.getAppLauncherClassName 获取app的启动页className
Context.getAppLauncherIntent 获取app的启动页Intent
Context.isIntentAvailable 判断Intent是否可用

Context.openApp 打开app
Context.openAppDetailsSettings 打开app设置页
Context.openUnknownAppSettings 打开app未知来源设置页

Context.getAppPackageInfo 获取App PackageInfo
Context.getAppIcon 获取app图标
Context.getAppIconId 获取app图标id
Context.getApplicationName 获取App ApplicationName
Context.getAppPath 获取app安装路径
Context.getAppVersionName 获取app版本名称
Context.getAppVersionCode 获取app版本代码

Context.getAppSignaturesSHA1 获取app SHA1签名
Context.getAppSignaturesMD5 获取app MD5签名
Context.getAppSignaturesSHA256 获取app SHA256签名
Context.getAppSignaturesHash 获取app签名哈希
Context.getAppSignatures 获取app签名
```

## AutoScreenExt 今日头条适配方案
```
AutoScreenUtil 主类
    init 初始化
    setCustomDensity 设置适配
IAutoCancel 取消适配
IAutoChange 修改适配
```

## BarExt
```
Context.statusBarHeight 状态栏高度(px)
Activity.isStatusBarVisible 状态栏是否显示
Activity.isStatusBarLightMode 状态栏Light模式
Activity.transparentStatusBar 透明状态栏

//通过透明状态栏 添加一个fakeStatusBar设置背景颜色实现
Activity.setStatusBarColor 设置状态栏颜色

//需要设置一个fakeStatusBar占位
Activity.setStatusBarColor4Drawer DrawerLayout 设置状态栏颜色
View.setStatusBarColor 设置(fakeStatusBar)状态栏颜色
View.addMarginTopEqualStatusBarHeight 给view添加状态栏高度的TopMargin
View.subtractMarginTopEqualStatusBarHeight 给view减去状态栏高度的TopMargin

Activity.actionBarHeight actionBar高度(px)

Context.navigationBarHeight 导航栏高度(px)
Activity.isNavBarVisible 导航栏显示
Activity.isSupportNavBar 是否支持导航栏
Activity.setNavBarColor 设置导航栏颜色 api21以上
Activity.navigationBarColor 获取导航栏颜色 api21以上
Activity.isNavBarLightMode 导航栏LightMode

Context.navigationBarHeight 获取导航栏高度
Activity.setNavBarVisibility 设置导航栏可见
Activity.isNavBarVisible 导航栏是否可见
Application.isSupportNavBar 是否支持导航栏
Activity.setNavBarColor 设置导航栏颜色
Activity.navigationBarColor 获取导航栏颜色
Activity.setNavBarLightMode 设置导航栏模式
Activity.isNavBarLightMode 获取导航栏是否开启light模式

//对FakeStatusBar进行操作
Activity.showFakeStatusBarView
Activity.hideFakeStatusBarView
Activity.addTopOffsetStatusBarHeight
Activity.subtractTopOffsetStatusBarHeight
```

## BitmapExt
```
Bitmap.compress 压缩bitmap
*.toDrawable Bitmap、ByteArray转Drawable
*.toByte Bitmap、File、InputStream转Byte
*.toBitmap 字节、File、InputStream转Bitmap

Context.toByte Int（Resource）转Bitmap
Context.toBitmap Int（Resource）转Bitmap
Context.vectorToBitmap 向量图转Bitmap

File.toRectBitmap 文件转设定矩形Bitmap
View.toBitmap view转Bitmap 可以当成截图view

Bitmap.scale 缩放
Bitmap.clip 修剪
Bitmap.skew 偏斜
Bitmap.rotate 旋转
Bitmap.alpha 透明
Bitmap.gray 灰色

Bitmap.toCircle 切圆
Bitmap.toRoundCorner 切圆角
Bitmap.addTextWatermark 添加文字水印
Bitmap.addImageWatermark 添加图片水印
Bitmap.fastBlur 快速模糊

File.getRotateDegree 获取图片文件旋转角度
```

## BrightnessExt
```
Context.isAutoBrightnessEnabled 是否开启自动亮度
Context.screenBrightness 屏幕亮度 0-255
Context.screenMaxBrightness 获取屏幕最大亮度

Context.windowBrightness 获取window亮度
Context.setWindowBrightness 设置window亮度
Context.setWindowBrightnessRatio 设置window亮度比例

//需要系统权限才可以修改
Context.setAutoBrightnessEnabled 设置屏幕自动亮度开关
Context.setBrightness 设置亮度
```

## BundleExt
```
Bundle?.get 将 Bundle.get(key) 转换为 Bundle[key]
```

## CancelExt
```
//externalCacheDir cacheDir filesDir sharedPreferenceDir databasesDir 这几个路径为官方自带路径
Context.sharedPreferenceDir SharedPreference路径
Context.databasesDir 数据库路径

Context.clearAppExternalCache 清除外部缓存
Context.clearAppInternalCache 清除内部缓存
Context.clearAppFile 清除File文件
Context.clearAppSharedPrefs 清除SharedPreference
Context.clearAppDatabases 清除数据库

Context.clearAppAllData 清除App全部数据
Context.clearAppCancel 清除App缓存

Context.appExternalCacheSize 应用外部缓存文件大小
Context.appInternalCacheSize 应用内部缓存文件大小
Context.appFileSize file文件大小
Context.appSharedPrefsSize SharedPrefs文件大小
Context.appDatabasesSize 数据库文件大小
Context.appCancelSize app缓存文件大小
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

## ContextExt
```
//常用Manager
Context.activityManager
Context.clipboardManager
Context.connectivityManager
Context.notificationManager
Context.telephonyManager
Context.windowManager
Context.keyguardManager
Context.audioManager
Context.inputMethodManager

Context.makeCustomAnimationBundle 制作跳转页面 自定义转场动画
Context.makeSceneTransitionAnimationBundle 制作跳转页面 View过渡动画
Context.checkPermission 判断权限

```

## ConvertExt
```
Int.toHexString 10进制转16进制
String.hexString2Int 16进制转10进制

ByteArray?.toHexString byte转16进制
String?.hexString2Bytes 16进制转byte

ByteArray?.toParcelable Bytes to Parcelable
Parcelable?.toBytes Parcelable to Bytes

ByteArray.splice 拼接Byte

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

## DisplayExt
```
Float.px2Dimension

*.dp
*.sp
*.pt

*.px2dp
*.px2sp
*.px2pt
```

## EncodeExt
```
String.urlEncode url加密
String.urlDecode url解密

*.base64Encode base64加密
*.base64Encode2String base64加密成String
*.base64Decode base64解密

String.binaryEncode 二进制加密
String.binaryDecode 二进制解密
```

## EncryptExt
```
*.encryptMD5ToString  MD5哈希加密
*.encryptHash2String 哈希加密

*.encryptHmacMD5ToString HmacMD5加密
*.encryptHmac2String Hmac加密

*.encryptDES DES加密
*.decryptDES DES解密

*.encrypt3DES 3DES加密
*.decrypt3DES 3DES解密

*.encryptAES AES加密
*.decryptAES AES解密

*.encryptRSA RSA加密
*.decryptRSA RSA解密

*.hashTemplate 哈希模板
*.hmacTemplate Hmac模板
*.symmetricTemplate 对称模板
*.rsaTemplate 非对称模板
*.rc4 rc4加密
```

## FileExt
```

```