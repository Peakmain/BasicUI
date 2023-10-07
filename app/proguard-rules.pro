
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
# AndroidX
-keep class com.google.android.material.**{*;}
-keep class androidx.**{*;}
-keep public class * extends androidx.**
-keep interface androidx.**{*;}
-keep @androidx.annotation.Keep class *
-keepclassmembers class *{
    @androidx.annotation.Keep *;
}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-keep class com.peakmain.basicui.bean.*{*;}

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }