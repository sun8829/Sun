# Android开发框架搭建Android-Sun-Framework

## 写在前面
Android-Sun-Framework是一个Android组件化开发框架，可用于大型项目开发。
## 框架结构
遵循高内聚低耦合理念，Module之间没有强依赖，具体结构如下图：

![](http://bmob-cdn-9267.b0.upaiyun.com/2017/05/08/e510ffaa407bc1bf805f6ef00e55c0f8.png)
## 详细说明
下面我会根据代码，详细讲解框架结构及其使用说明。
### 一. 配置中心config.gradle
```
ext {
    android = [
            compileSdkVersion: 25,
            buildToolsVersion: "25.0.2",
            minSdkVersion    : 19,
            targetSdkVersion : 25,
            versionCode      : 1,
            versionName      : "1.0.0",
            isModule         : false, //是否是独立模块开发
            isDebug          : "true",//是否是调试模式
            scheme           : "\"xpai\"" //应用scheme
    ]
    //依赖配置
    dependencies = [
            "supportVersion": "25.2.0"
    ]
}
```

当前配置项还不多，后面会根据实际开发需要，优化配置。
这里我单独说明一下isModule的作用：
1. 当isModule为真时，除了library子模块为library，其他子模块均为application，我们以login模块为例，看一下是如何实现的
```
if (rootProject.ext.android.isModule) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}

android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    defaultConfig {
        if (rootProject.ext.android.isModule) {
            applicationId "com.ody.login"
        }
        minSdkVersion rootProject.ext.android.minSdkVersion
        targetSdkVersion rootProject.ext.android.targetSdkVersion
        versionCode rootProject.ext.android.versionCode
        versionName rootProject.ext.android.versionName
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    sourceSets {
        main {
            if (rootProject.ext.android.isModule) {
                manifest.srcFile 'src/main/debug/AndroidManifest.xml'
            } else {
                manifest.srcFile 'src/main/AndroidManifest.xml'
                //release模式下排除debug文件夹中的所有Java文件
                java {
                    exclude 'debug/**'
                }
            }
        }
    }
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')

    annotationProcessor 'com.github.mzule.activityrouter:compiler:1.1.7'
    compile project(':library')
}

```

我们主要看一下rootProject.ext.android.isModule的if判断，这里根据它来配置当前模块plugin为library还是application。当为application时，下面会增加applicationId的配置。
仔细的同学可能会问，sourceSets的配置是干嘛的？由于application 必须要有默认启动Activity，所以这里我们需要根据isModule使用不同的AndroidManifest.xml,每个模块可能会有测试代码，所以只要把测试代码写在debug包下面，正式编译的时候debug下面的java文件不会参与编译。
同样的我们来看一下主Module的配置：
```
apply plugin: 'com.android.application'

android {
    ......
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    testCompile 'junit:junit:4.12'
    //路由
    annotationProcessor 'com.github.mzule.activityrouter:compiler:1.1.7'
    if (rootProject.ext.android.isModule) {
        compile project(':library')
    }
    if (!rootProject.ext.android.isModule) {
        compile project(':guide')
    }
    if (!rootProject.ext.android.isModule) {
        compile project(':login')
    }
    if (!rootProject.ext.android.isModule) {
        compile project(':trade')
    }
    if (!rootProject.ext.android.isModule) {
        compile project(':usercenter')
    }
}

```

这里我们只关注dependencies，根据isModule来配置是否依赖该Module.
2. 当isModule为真时，Module可以单独运行，这样做的好处是：一：大大缩减编译时间；二：可以跨部门，跨团队协作。
### 二. Module间跳转
对比[ARouter](https://github.com/alibaba/ARouter)与[ActivityRouter](https://github.com/mzule/ActivityRouter),我决定使用ActivityRouter，主要原因有两个：
1. 由于ARouter加入分组概念，和我们公司当前已有设计相违背；
2. ActivityRouter比ARouter精简，后面我会分享一下ActivityRouter和ARouter的实现原理，敬请期待！

ActivityRouter具体如何使用我就不再赘述，详细可以查看[ActivityRouter](https://github.com/mzule/ActivityRouter)，里面有详细的说明和demo。我在这里要强调一下ActivityRouter多模块需要如何实现？
实现步骤：

1. 在子Module中新建一个Module类，比如我这里的login模块：
```
@Module("login")
public class LoginModule {
}

```

2. 同样在主Module中也需要新建一个类
```
@Module("app")
public class AppModule {
}
```

3. 关联Module
```
@Modules({"app", "login"})
public class OdyApplication extends BaseApplication {
}
```