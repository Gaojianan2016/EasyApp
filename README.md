# EasyApp
自定义简单app开发
[![](https://jitpack.io/v/Gaojianan2016/EasyApp.svg)](https://jitpack.io/#Gaojianan2016/EasyApp)

依赖使用
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    //last写最新版本
    def last="1.0.0"
    //easybase 包括Activity Fragment的基础类
    implementation "com.github.Gaojianan2016.EasyApp:easybase:$last"
    //简单快速网络框架（基于Retrofit2+OkHttp3）
    implementation "com.github.Gaojianan2016.EasyApp:easynetworker:$last"
    //简单快速的log打印
    implementation "com.github.Gaojianan2016.EasyApp:easylogger:$last"
    //简单快速的toast工具
    implementation "com.github.Gaojianan2016.EasyApp:easytoaster:$last"
    //简单快速实现对话框（基于DialogFragment）
    implementation "com.github.Gaojianan2016.EasyApp:easydialoger:$last"
    //实现Activity管理，不需要可以在自己项目的AndroidManifest中添加去除操作
    implementation "com.github.Gaojianan2016.EasyApp:easyinitalizer:$last"
    //工具类整合
    implementation "com.github.Gaojianan2016.EasyApp:easyutils:$last"
}
```

easyutils 比较杂的工具类整合（主要用kotlin的扩展实现）
可以查看easyutils的[README.md](https://github.com/Gaojianan2016/EasyApp/tree/master/easyutils)