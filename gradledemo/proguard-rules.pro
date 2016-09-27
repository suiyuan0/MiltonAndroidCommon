#依赖注入的框架需要加上其避免混淆规则: 比如butterknife:
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#友盟第三方登录和分享:  关于qq,微信,sina的jar包的混淆规则:
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
#-libraryjars libs/SocialSDK_QQZone_2.jar   # 注意,这条会报重复混淆,要注释掉
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class [your_pkg].R$*{
    public static final int *;
}


#报错:找不到某个jar包中的类:
#解决方法:不混淆这个jar包:
# 不混淆七牛的jar包
#（声明lib文件）-libraryjars **.jar
#（不提示警告）-dontwarn com.xx.bbb.**
#（不进行混淆）-keep class com.xx.bbb.** { *;}

#-libraryjars libs/qiniu-android-sdk-7.0.9.jar -->jar包已被统一声明,所以这里不用再重复声明
-dontwarn com.qiniu.android.**
-keep class com.qiniu.android.** { *;}



#运行,eventbus报错:
#说明eventbus的包不能混淆
#解决:保留eventbus中的类,以及我们的类中的onevent方法:
#eventbus
-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#javabean类多层调用时空指针:
#找不到内部的类:
#解决:不混淆domain包下的javabean:
# 保留domain包中的类
-keep class com.qxinli.android.domain.** { *; }

#其他:不混淆volley,jpush:
# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }

#jpush
#-libraryjars libs/jpush-sdk-release1.6.3.jar
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }