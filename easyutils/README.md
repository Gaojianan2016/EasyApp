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
```
## ActivityResultExt
```
internal class ActivityResultFragment 生成随机数并且直接监听结果的透明Fragment
class ActivityResultHelper 实现功能的帮助类
FragmentActivity.simpleActivityResult 打开意图并且直接执行结果
Fragment.simpleActivityResult 打开意图并且直接执行结果
```
## AnnotationsExt
```
Any.containAnnotation 判断类是否被注解
Field.containAnnotation 判断Field是否被注解
Any.getAnnotation 获取注解的对象
Any.getField 获取注解的成员变量
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
## BitmapExt
```
Bitmap.compress 压缩bitmap
*.toByte Bitmap、File、Int（Resource）、InputStream转字节
*.toBitmap 字节、File、Int（Resource）、InputStream转Bitmap
Bitmap.scale 缩放bitmap
Bitmap?.drawBitmap 在bitmap上面绘制一个bitmap
Bitmap?.drawMiniBitmap 在bitmap上面指定的9个位置绘制一个miniBitmap
Bitmap.blurBitmap 高斯模糊bitmap
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
