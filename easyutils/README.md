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
*.toFile 转为file
File.toByteArray 转为字节

File.suffix 直接最后一个.获取文件后缀
File.extension 通过mimeType获取文件后缀
File.mimeType 通过后缀获取文件类型

File.isAvailableDir 是否存在文件夹
File.isAvailableFile 是否存在文件

File.getStatFsTotalSize 获取StatFs总大小
File.getStatFsAvailableSize 获取StatFs可用大小
File.getFileLength 获取文件大小（包含内部所有文件总和）

File.rename 文件重命名
File.createParentDir 创建父目录
File.createOrExistsDir 创建文件夹
File.createOrExistsFile 创建文件
File.createDir 创建新文件夹
File.createFile 创建新文件
File.copyToPath 复制文件
File.moveToPath 移动文件
File.copyOrMoveDir 复制或者移动文件夹
File.deleteDir 删除文件夹
File.deleteFile 删除文件
File.findListFiles 查找文件

File.notifyMediaFile 更新媒体文件
File.notifyScanMediaFile 通知文件夹扫描媒体文件
File.notifyInsertThumbnail 通知文件夹插入缩略图

Context.openFile 打开文件
Context.getLocalFileUri 获取本地文件uri
Context.getLocalFileFromUri 获取本地文件通过uri
Context.getLocalFileFromContentUri 获取本地文件通过ContentUri

Context.unzipAssetsFile 解压assets文件
Context.unzipFileUseZip4j zip4j解压文件
```

## FileIOExt
```
File.writeString file写入字符串
File.writeInputStream file写入输入流
```

## FragmentExt
```
Fragment.startActivity 打开Activity
Fragment.startActivityForResult 打开Activity等待结果

Fragment.newInstance 实例化Fragment
Fragment.getArgumentsKey 获取Fragment的Argument值

```

## IntentExt
```
Context.dial 拨号
Context.sendSMS 发短信
Context.email 发邮件
Context.browser 浏览网址

Context.shareText 分享文字
Context.shareImage 分享图片
Context.shareTextImage 分享图文

FragmentActivity.quickShoot 快速拍照

Context.intentOf 生成带参Intent
Intent.addNewTaskFlag 添加newTaskFlag
Intent.addSingleTopFlag 添加singleTopFlag

Intent.intentIsAvailable Intent 是否可用
String.getComponentIntent 包名获取组件 Intent
```

## JsonExt
```
String?.formatJson json 格式化
```

## KeyboardExt
```
View.isShowKeyboard 是否显示软键盘
View.keyboardShowHeight 软键盘高度
View.showKeyboard 显示软键盘
View.hideKeyboard 隐藏软键盘
View.toggleKeyboard 切换软键盘

Context.toggleSoftInput 切换软键盘
View.hideSoftInputFromWindow 隐藏软键盘
Activity.isSoftInputVisible 是否显示软键盘
Activity.showSoftInput 隐藏软键盘
Activity.hideSoftInput 隐藏软键盘

Activity.fixAndroidBug5497 修复软键盘bug
```

## ListExt
```
*.isList 列表需要大于1
*.isLimitSize 最多不超过设置数量
String.split2List 分割字符串成列表
```

## LogExt
```
logV
logD
logI
logW
logE
logWTF
```

## MetaDataExt
```
Context.getAppMetaData 获取App meta-data

Activity.getAppMetaData 获取Activity meta-data
Context.getActivityMetaData 获取Activity meta-data

Service.getMetaData 获取Service meta-data
Context.getServiceMetaData 获取Service meta-data

BroadcastReceiver.getMetaData 获取BroadcastReceiver meta-data
Context.getReceiverMetaData 获取BroadcastReceiver meta-data
```

## NetworkExt
```
Context.openWirelessSettings 打开无线设置

Context.isNetworkConnected 是否连接网络
Context.isWifiConnected 是否连接wifi
Context.isMobileConnected 是否连接移动网络
Context.isEthernetConnected 是否连接以太网

Context.registerNetworkCallback 监听网络状态变化
Context.unregisterNetworkCallback 取消监听网络状态变化
```

## NotificationExt
```
Context.isNotificationsEnabled 是否启动通知

Context.sendNotification 发送通知
Service.sendForegroundNotification 发送前台通知

Context.cancelNotification 取消通知
Context.cancelAllNotification 取消全部通知
Context.setNotificationBarExpand 是否展开通知

```

## NumberExt
```

```

## PermissionExt
```

```

## PhoneInfoExt
```

```

## QRCodeExt
```

```

## ReflexExt
```

```

## RegexExt
```

```

## ResourcesExt
```

```

## ScreenExt
```

```

## ServiceExt
```

```

## ShellExt
```

```

## ShellExt
```

```

## SingletonExt
```

```

## StringExt
```

```

## TabLayoutExt
```

```

## TimeExt
```

```

## ToastExt
```

```

## UnitExt
```

```

## UriExt
```

```

## VibrateExt
```

```

## ViewExt
```

```

## VolumeExt
```

```