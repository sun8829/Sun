# Android模块化开发框架Android-Sun-Framework

[![](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/samuelhuahui/Sun)
[![](https://img.shields.io/scrutinizer/g/filp/whoops.svg)](https://github.com/samuelhuahui/Sun)
## 一. 写在前面
Android-Sun-Framework是一个Android组件化开发框架，可用于大型项目开发。
> 欢迎Star or Follow我的[GitHub](https://github.com/samuelhuahui/Sun)

> 欢迎搜索微信公众号**SamuelAndroid**关注我，定期推送原创文章和代码。

![](http://upload-images.jianshu.io/upload_images/4751442-30878689318053ae.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 二. 框架结构
遵循高内聚低耦合理念，Module之间没有强依赖，具体结构如下图：

![](http://bmob-cdn-9267.b0.upaiyun.com/2017/05/08/e510ffaa407bc1bf805f6ef00e55c0f8.png)

## 三. 框架依赖
```groovy
//Rx系列
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle-android:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.0.1'

    //Retrofit
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
    //路由
    compile 'com.github.mzule.activityrouter:activityrouter:1.2.2'

    //图片加载
    compile 'com.github.bumptech.glide:glide:3.7.0'

    //下拉刷新
    compile 'com.lcodecorex:tkrefreshlayout:1.0.7'

    compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.15'
```


## 三. 详细说明
下面我会根据代码，详细讲解框架结构及其使用说明。
### A. 配置中心config.gradle
```groovy
ext {
    android = [
            compileSdkVersion: 25,
            buildToolsVersion: "25.0.2",
            minSdkVersion    : 19,
            targetSdkVersion : 25,
            versionCode      : 1,
            versionName      : "1.0.0",
            isModule         : false, //是否是独立模块开发
            loginIsApp       : false,
            userCenterIsApp  : false,
            tradeIsApp       : false,
            guideIsApp       : false,
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
这里我单独说明一下loginIsApp的作用：(此处从isModule改为各个模块的开关，方便后面开发)
1. 当isModule为真时，除了library子模块为library，其他子模块均为application，我们以login模块为例，看一下是如何实现的
```groovy
if (rootProject.ext.android.loginIsApp) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}

android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    defaultConfig {
        if (rootProject.ext.android.loginIsApp) {
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

 我们主要看一下rootProject.ext.android.loginIsApp的if判断，这里根据它来配置当前模块plugin为library还是application。当为application时，下面会增加applicationId的配置。
仔细的同学可能会问，sourceSets的配置是干嘛的？由于application 必须要有默认启动Activity，所以这里我们需要根据isModule使用不同的AndroidManifest.xml,每个模块可能会有测试代码，所以只要把测试代码写在debug包下面，正式编译的时候debug下面的java文件不会参与编译。
同样的我们来看一下主Module的配置：
```groovy
apply plugin: 'com.android.application'

android {
    ......
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    testCompile 'junit:junit:4.12'
    //路由
    annotationProcessor 'com.github.mzule.activityrouter:compiler:1.1.7'
    if (!rootProject.ext.android.guideIsApp) {
            compile project(':guide')
    }
    if (!rootProject.ext.android.loginIsApp) {
            compile project(':login')
    }
     if (!rootProject.ext.android.tradeIsApp) {
            compile project(':trade')
     }
     if (!rootProject.ext.android.userCenterIsApp) {
            compile project(':usercenter')
     }
}

```

这里我们只关注dependencies，根据xxisApp来配置是否依赖该Module.
2. 当isModule为真时，Module可以单独运行，这样做的好处是：一：大大缩减编译时间；二：可以跨部门，跨团队协作。
### B. Module间跳转
对比[ARouter](https://github.com/alibaba/ARouter)与[ActivityRouter](https://github.com/mzule/ActivityRouter),我决定使用ActivityRouter，主要原因有两个：
1. 由于ARouter加入分组概念，和我们公司当前已有设计相违背；
2. ActivityRouter比ARouter精简，后面我会分享一下ActivityRouter和ARouter的实现原理，**敬请期待！**

ActivityRouter具体如何使用我就不再赘述，详细可以查看[ActivityRouter](https://github.com/mzule/ActivityRouter)，里面有详细的说明和demo。我在这里要强调一下ActivityRouter多模块需要如何实现？
实现步骤：

a. 在子Module中新建一个Module类，比如我这里的login模块：
```java
@Module("login")
public class LoginModule {
}

```

b. 同样在主Module中也需要新建一个类
```java
@Module("app")
public class AppModule {
}
```

c. Module注册
```java
@Modules({"app", "login"})
public class OdyApplication extends BaseApplication {
}
```
d. 界面跳转
为了能统一处理，自己把ActivityRouter的跳转封装了一层：
```java
public class JumpUtils {
    public final static String LOGIN_URL = "login";

    public static void open(Context context, String url) {
        Routers.open(context, BuildConfig.SCHEME + "://" + url);
    }

    public static void open(Context context, String url, RouterCallback callback) {
        Routers.open(context, BuildConfig.SCHEME + "://" + url, callback);
    }
}
```
JumpUtils里的常量就是跳转注解路径
```java
@Router(JumpUtils.LOGIN_URL)
public class LoginActivity extends BaseActivity {
    ......
}
```
以上都是为了能更好的**统一管理，避免后期修改多处**。

### C. 网络库封装
做Android开发的想必大家都知道Retrofit和Rxjava，我们也是使用的他们，为了更好的控制网络请求，这里我同时引入了Rxlifecycle。接下来我们看看具体是如何实现的

1. 添加依赖
```groovy
//Rx系列
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle-android:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.0.1'

    //Retrofit
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
```
2. Retrofit接口定义
```java
public interface BaseNetApi {
    @GET("/api/dolphin/list?&platform=3&platformId=0&pageCode=APP_HOME&adCode=ad_banner&areaCode=310115")
    Observable<AdBean> getAd(@QueryMap Map<String, String> params);
}
```

3. Retrofit初始化和具体实现
```java
public class SingletonNet {
    public static final String BASE_URL = "http://api.laiyifen.com";
    private static final int DEFAULT_TIMEOUT = 30;
    private Retrofit retrofit;
    private volatile static SingletonNet INSTANCE = null;

    private SingletonNet() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
                //添加公共header
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("token", "123");
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit.Builder b = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL);

        retrofit = b.build();
    }

    public static SingletonNet getSingleton() {
        if (INSTANCE == null) {
            synchronized (SingletonNet.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonNet();
                }
            }
        }
        return INSTANCE;
    }

    public <T> T getNetService(Class<T> t) {
        return retrofit.create(t);
    }

}
```
我们使用泛型，为各个模块创建自己的API，这样做能更好的解耦。同时我们也在基础库里实现了BaseNetApi，这里是项目中重复使用的接口，避免多处实现同一个接口。我们再来看一下其他Module里是如何创建的
```java

public class MainHttpClient extends BaseHttpClient {

    private static class SingletonHolder {
        private static final MainApi API = SingletonNet.getSingleton().getNetService(MainApi.class);
    }

    public static Observable<AdBean> get() {
        Map<String, String> params = new HashMap<>();
        return SingletonHolder.API.get(params);
    }
}
```

这里采用静态内部类实现了单例，和SingletonNet的单例有区别。
4. 具体使用
```java
 MainHttpClient.get()
                .compose(RxSchedulers.<AdBean>compose())
                .compose(this.<AdBean>bindToLifecycle())
                .subscribe(new HttpObserver<AdBean>(mContext) {

                    @Override
                    protected void success(AdBean bean) {
                        super.success(bean);
                    }

                });
```
.compose(RxSchedulers.<AdBean>compose())线程调度等重复操作放在这里；
.compose(this.<AdBean>bindToLifecycle()) 网络请求和Activity生命周期想关联。
HttpObserver的实现如下：
```java
public abstract class HttpObserver<T> implements Observer<T> {
    private Context mContext;

    protected HttpObserver(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public final void onSubscribe(Disposable d) {

    }

    @Override
    public final void onNext(T value) {
        success(value);
    }

    @Override
    public final void onError(Throwable e) {
        error(e.toString());
    }

    @Override
    public final void onComplete() {
        complete();
    }


    protected void success(T t) {
    }

    protected void error(String msg) {
    }

    protected void complete() {

    }
}
```

**final是为了避免用户重写，强制重写后面自定义的几个方法。**

### D. 启用StrictMode

        Android平台中(Android 2.3起)，新增加了一个新的类，叫StrictMode(android.os.StrictMode)。这个类可以用来帮助开发者改进他们编写的应用，并且提供了各种的策略，这些策略能随时检查和报告开发者开发应用中存在的问题，比如可以监视那些本不应该在主线程中完成的工作或者其他的一些不规范和不好的代码。
        本框架是在[BaseApplication](https://github.com/samuelhuahui/Sun/blob/master/library/baselibrary/src/main/java/com/ody/library/base/BaseApplication.java)里开启的，具体代码如下：
```java
if (BuildConfig.IS_DEBUG) {
            logBuilder.setLogSwitch(true);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        } else {
            logBuilder.setLogSwitch(true);
        }
```
我们设定只有是debug状态才会开启StrictMode，以免影响线上版本。具体StrictMode的配置自行Google，这里就不多赘述。

### E. 基础库归类
        细心的朋友会发现以前的library更名为baselibrary,并将baselibrary移至library目录下，同时增加了模块PhotoPicker，PhotoPicker我下面会讲解，我们先来说一下目录变更的原因。
        当前我们已有的Module如下：app，login， usercenter，guide，trade，baselibrary和PhotoPicker，根据他们的命名，我们知道app，login， usercenter，guide和trade是业务模块，而baselibrary和PhotoPicker是基础模块，当然基础模块我们还会继续增加，比如推送，客服等。所以如果把基础模块放在和业务模块同目录下，难免会笔记混乱，故我们把基础模块做了归类，都放在library目录下。我们讲一下如何移动Module。
步骤1. 右击工程，新建文件夹library
步骤2. 移动已有Module至library文件夹下
步骤3. 打开setting.gradle修改“:baselibrary”为":library:baselibrary"
步骤4. 打开所以有依赖的gradle，compile project(':library:baselibrary')
调整后目录结构如下图：
![](http://upload-images.jianshu.io/upload_images/4751442-c4a8e2d762ace72e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### F. 图片选择库(PhotoPicker)实现和使用
先看效果
![](http://upload-images.jianshu.io/upload_images/4751442-edf63fb45d4394f9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
##### a. 图片选择参数
        这里我们采用Builder模式，简化调用，主要代码如下：
```java
public class PhotoPicker {

    public static final int REQUEST_CODE = 233;

    public final static int DEFAULT_MAX_COUNT = 9;
    public final static int DEFAULT_COLUMN_NUMBER = 3;

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static String EXTRA_GRID_COLUMN = "column";
    public final static String EXTRA_ORIGINAL_PHOTOS = "ORIGINAL_PHOTOS";
    public final static String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public static ImageLoader mImageLoader;

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Bundle mPickerOptionsBundle;
        private Intent mPickerIntent;


        public PhotoPickerBuilder() {
            mPickerOptionsBundle = new Bundle();
            mPickerIntent = new Intent();
        }

        /**
         * Send the Intent from an Activity with a custom request code
         *
         * @param activity    Activity to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull Activity activity, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                activity.startActivityForResult(getIntent(activity), requestCode);
            }
        }

        /**
         * @param fragment    Fragment to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull Context context,
                          @NonNull android.support.v4.app.Fragment fragment, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), requestCode);
            }
        }

        /**
         * Send the Intent with a custom request code
         *
         * @param fragment Fragment to receive result
         */
        public void start(@NonNull Context context,
                          @NonNull android.support.v4.app.Fragment fragment) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), REQUEST_CODE);
            }
        }

        /**
         * Get Intent to start {@link PhotoPickerActivity}
         *
         * @return Intent for {@link PhotoPickerActivity}
         */
        public Intent getIntent(@NonNull Context context) {
            mPickerIntent.setClass(context, PhotoPickerActivity.class);
            mPickerIntent.putExtras(mPickerOptionsBundle);
            return mPickerIntent;
        }

        /**
         * Send the crop Intent from an Activity
         *
         * @param activity Activity to receive result
         */
        public void start(@NonNull Activity activity) {
            start(activity, REQUEST_CODE);
        }

        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean showGif) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<String> imagesUri) {
            mPickerOptionsBundle.putStringArrayList(EXTRA_ORIGINAL_PHOTOS, imagesUri);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPickerOptionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }

        public PhotoPickerBuilder setImageLoader(ImageLoader loader) {
            mImageLoader = loader;
            return this;
        }
    }
}
```
##### b. 图片加载器

1. 目的：PhotoPicker作为一个基础库且需要大量加载图片，简单的方式就是库里添加第三方框架的依赖，但是如果项目中和库里的依赖不一致，势必会导致冗余增加包大小。如过用的库相同，但版本不一致的话，将导致包冲突，所以我们这里通过实现图片加载器来解耦。

2. 具体实现:通过定义一个接口[ImageLoader](https://github.com/samuelhuahui/Sun/blob/master/library/PhotoPicker/src/main/java/com/ody/photopicker/loader/ImageLoader.java),代码如下：

```java
public interface ImageLoader {
//加载列表时调用
    void displayImage(ImageView imageView, String path);

  //预览图片时调用
   void displayImage(ImageView imageView, Uri uri) ;

    void clear(View imageView);
}
```
所以PhotoPicker的图片加载都使用displayImage方法，

##### c. 使用：详细使用请查看[MainActivity](https://github.com/samuelhuahui/Sun/blob/master/app/src/main/java/com/ody/ody/main/MainActivity.java)
```java
PhotoPicker.builder()
                        .setGridColumnCount(2)
                        .setPhotoCount(9)
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(ImageView imageView, String path) {
                                Glide.with(imageView.getContext()).load(path).thumbnail(0.3f).into(imageView);
                            }

                            @Override
                            public void displayImage(ImageView imageView, Uri uri) {
                                Glide
                                        .with(imageView.getContext()).load(uri)
                                        .thumbnail(0.1f)
                                        .dontAnimate()
                                        .dontTransform()
                                        .override(800, 800)
                                        .into(imageView);
                            }

                            @Override
                            public void clear(View view) {
                                GlideUtil.clear(view);
                            }
                        })
                        .start(MainActivity.this);
```
## G 分享库封装
分享功能几乎是每个APP必备功能，所以本开发框架必然不会少了他的封装，提高开发效率。分享库的实现思路和图片选择相似。具体实现步骤如下：
#### a. 新建Module Share
由于分享平台众多，为了能简化开发，这里使用ShareSdk来实现我们的分享功能，当前框架仅引入了新浪、QQ、微信三个平台的分享，如有其它需求，可以到ShareSdk下载更多。
#### b. 新建ShareHelper类,详细代码如下：
```java

/**
 * Created by Samuel on 2017/5/12.
 */

public class ShareHelper {
    public final static String QQ_NAME = QQ.NAME;
    public final static String QZONE_NAME = QZone.NAME;
    public final static String WECHAT_NAME = Wechat.NAME;
    public final static String WECHATMOMENTS_NAME = WechatMoments.NAME;

    public static void init(Context context, String APP_KEY) {
        ShareSDK.initSDK(context, APP_KEY);
        ShareSDK.setPlatformDevInfo(QQ.NAME, BuildConfig.QQ);
        ShareSDK.setPlatformDevInfo(QZone.NAME, BuildConfig.QZone);
        ShareSDK.setPlatformDevInfo(Wechat.NAME, BuildConfig.Wechat);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, BuildConfig.WechatMoments);
    }

    public static Builder builder(String platformName) {
        return new Builder().setPlatformName(platformName);
    }

    public static class Builder {
        private String title;
        private String titleUrl;
        private String text;
        private String url;
        private String imageUrl;
        private String comment;
        private String site;
        private String siteUrl;
        private String platformName;
        private ShareListener shareListener;

        public Builder() {

        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        public Builder setTitleUrl(String titleUrl) {
            this.titleUrl = titleUrl;
            return this;
        }

        // text是分享文本，所有平台都需要这个字段
        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        // url仅在微信（包括好友和朋友圈）中使用
        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        // site是分享此内容的网站名称，仅在QQ空间使用
        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setPlatformName(String platformName) {
            this.platformName = platformName;
            return this;
        }

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        public Builder setSiteUrl(String siteUrl) {
            this.siteUrl = siteUrl;
            return this;
        }

        public Builder setShareListener(ShareListener listener) {
            shareListener = listener;
            return this;
        }

        public void share() {
            Platform.ShareParams sp = null;
            Platform platform;
            if (platformName == null || platformName.trim().length() == 0)
                throw new RuntimeException("还未设置分享平台");

            if (platformName.equals(QQ_NAME)) {
                sp = new QQ.ShareParams();

            } else if (platformName.equals(QZONE_NAME)) {
                sp = new QZone.ShareParams();
            } else if (platformName.equals(WECHAT_NAME)) {
                sp = new Wechat.ShareParams();
            } else if (platformName.equals(WECHATMOMENTS_NAME)) {
                sp = new WechatMoments.ShareParams();
            }
            platform = ShareSDK.getPlatform(platformName);
            sp.setTitle(title);
            sp.setTitleUrl(titleUrl); // 标题的超链接
            sp.setText(text);
            sp.setImageUrl(imageUrl);
            sp.setSite(site);
            sp.setSiteUrl(siteUrl);
            // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
            platform.setPlatformActionListener(new PlatformActionListener() {
                public void onError(Platform arg0, int arg1, Throwable arg2) {
                    //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    if (shareListener != null) {
                        shareListener.onError();
                    }
                }

                public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                    //分享成功的回调
                    if (shareListener != null) {
                        shareListener.onComplete();
                    }
                }

                public void onCancel(Platform arg0, int arg1) {
                    //取消分享的回调
                    if (shareListener != null) {
                        shareListener.onCancel();
                    }
                }
            });
            // 执行图文分享
            platform.share(sp);
        }
    }

    public interface ShareListener {
        void onError();

        void onComplete();

        void onCancel();
    }
}

```
这里主要讲一下init方法中，ShareSDK动态参数（BuildConfig.QQ、BuildConfig.Wechat等）的配置
首先打开config.gradle:
```
 //分享配置
    QQ = [
            AppId : "100371282",
            AppKey: "aed9b0303e3ed1e27bae87c33761161d"
    ]
    QZone = [
            AppId : "100371282",
            AppKey: "aed9b0303e3ed1e27bae87c33761161d"
    ]
    Wechat = [
            AppId    : "wx4868b35061f87885",
            AppSecret: "64020361b8ec4c99936c0e3999a9f249"
    ]
    WechatMoments = [
            AppId    : "wx4868b35061f87885",
            AppSecret: "64020361b8ec4c99936c0e3999a9f249"
    ]
```
这里是各个分享平台的配置参数，使用分享功能前只需要修改这里的配置参数即可，如果需要支持更多平台，可以提issue给我，我会增加配置入口。大家都知道这里只是配置入口，这些输入如何才能让代码使用，这里我们就要看一下share模块下的build.gradle

```
defaultConfig {
       ......
        //推送配置
        manifestPlaceholders = [
                QQ_SCHEME: "tencent${rootProject.ext.QQ.AppId}"
        ]

        buildConfigField "java.util.HashMap<String, Object>", "QQ",
                "new java.util.HashMap<String, Object>() {" +
                        "{ put(\"AppId\", \"${rootProject.ext.QQ.AppId}\"); put(\"AppKey\",  \"${rootProject.ext.QQ.AppKey}\"); }}"

        buildConfigField "java.util.HashMap<String, Object>", "QZone",
                "new java.util.HashMap<String, Object>() {" +
                        "{ put(\"AppId\", \"${rootProject.ext.QZone.AppId}\"); put(\"AppKey\",  \"${rootProject.ext.QZone.AppKey}\"); }}"

        buildConfigField "java.util.HashMap<String, Object>", "Wechat",
                "new java.util.HashMap<String, Object>() {" +
                        "{ put(\"AppId\", \"${rootProject.ext.Wechat.AppId}\"); put(\"AppSecret\",  \"${rootProject.ext.Wechat.AppSecret}\"); }}"

        buildConfigField "java.util.HashMap<String, Object>", "WechatMoments",
                "new java.util.HashMap<String, Object>() {" +
                        "{ put(\"AppId\", \"${rootProject.ext.WechatMoments.AppId}\"); put(\"AppSecret\",  \"${rootProject.ext.WechatMoments.AppSecret}\"); }}"
    }
```
到这里你应该明白我们在初始化方法中使用的BuildConfig.QQ是从哪里来的吧！！！
我们在看一下上面的manifestPlaceholders 配置，做过分享的童鞋都知道，腾讯的分享需要通过设定scheme，我们打开share模块清单文件查看一下：
```
<activity
            android:name="com.mob.tools.MobUIShell"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:windowSoftInputMode="stateHidden|adjustResize">

            <intent-filter>
                <data android:scheme="${QQ_SCHEME}" />
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.BROWSABLE" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
```

#### b. 如何使用
```
ShareHelper.builder(ShareHelper.QQ_NAME)
                        .setTitle("测试分享的标题")
                        .setTitleUrl("http://sharesdk.cn")
                        .setText("测试分享的文本")
                        .setImageUrl("http://www.someserver.com/测试图片网络地址.jpg")
                        .setSite("发布分享的网站名称")
                        .setSiteUrl("发布分享网站的地址")
                        .setShareListener(new ShareHelper.ShareListener() {
                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(mContext, "cancel", Toast.LENGTH_LONG).show();
                            }
                        })
                        .share();
``` 
## Hybrid框架介绍
![](http://upload-images.jianshu.io/upload_images/4751442-13baf1fbfafd82a0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

该框架支持两种方式接入：SuperWebView, SuperWebFragment
接触过APICloud的童鞋都知道，他们只支持以Activity的形式，所以使用受限比较明显。我们这次的封装规避了他们的问题，是接入更加灵活，同时又参照了他们，做了一些相似的实现（当前只是把基础的且分钟重要的内容实现了，** 后面会参照APICloud的官方文档陆续增加，敬请期待**）

## 如何使用
我这里就不再说明底层是如何实现的，想了解这块的可以查看源码，或者关注我的微信公众号（搜索**SamuelAndroid**添加关注），给我留言！
1. 导入
当前还没有发布到jcenter，需要手工导入，这里不再赘述，可以参照[这篇文章](http://blog.csdn.net/Bingtang_blog/article/details/52557056)

2. native封装的接口:
详细看下面的注释
```java
    // 发送数据
    void sendDataToHtml5(String data);
    //发送数据 
    void sendDataToHtml5(String data, CallBackFunction responseCallback);

    //发送事件
    void sendEventToHtml5(String handlerName);
   //发送事件
    void sendEventToHtml5(String handlerName, CallBackFunction callBack);
    //发送事件
    void sendEventToHtml5(String handlerName, String data, CallBackFunction callBack);

    // 添加默认事件处理，即数据，对应于js的mobileAPI.send
   setDefaultHtml5EventListener(new Html5EventListener() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (function != null) {
                    function.onCallBack("孙华辉");
                }
            }
        });

  //监听H5发送的事件，对应H5的accessNative
   addHtml5EventListener("eventname", new Html5EventListener() {

	@Override
	public void handler(String data, CallBackFunction function) {
				
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
	}
});
```
3.  H5的封装--注入完成
本框架会在HTML加载完成后动态注入一段js代码，框架里的一些方法只有等到注入完成后才能使用，为了让前段知道调用时机，我们把这个状态发送给了前端，有两种方式可以拿到注入完成的回调：
 a. 通过事件：
```javascript
document.addEventListener(
       'onReady'
       , function() {
              //TODO 
       },
       false
);
```

 b. 通过回调:这里我多说两句，由于本人前端知识薄弱，这种方式的实现着实花了我一段时间，主要也是为了和APICloud具有同样的使用方式，所以这篇文章相隔上一篇时间较久。
```javascript
appReady = function(){
      // TODO
}
```

4. H5的封装--生命周期（resume、pause）
做安卓开发的童鞋对于onResume，onPause这两个生命周期再熟悉不过，开发过程中我们会在onResume回调中做一些处理，所以本框架也将这两个生命周期传给了H5，具体使用方法如下：
```javascript
appReady = function(){
      ...
      resume = function(){
              // TODO 可以这里加载数据
      }

      pause = function(){
              // TODO  
      }
}
```
5. H5的封装--事件

```javascript
// 接收native发送过来的数据,对应native的sendDataToHtml5的处理
mobileAPI.init(function(message, responseCallback) {
        responseCallback(data);
});
//监听native发送的事件
mobileAPI.addEventListener("functionInJs", function(data, responseCallback) {
         responseCallback(responseData);
 });

//发送数据到native
window.mobileAPI.send(
         data
         , function(responseData) {
         document.getElementById("show").innerHTML = "repsonseData from java, data = " + responseData
      }
);

// 发送事件到native
window.mobileAPI.accessNative(
          'submitFromWeb'
          , {'param': '中文测试'}
          , function(responseData) {
          document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
     }
);

```
