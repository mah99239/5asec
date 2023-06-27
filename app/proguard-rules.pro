# Add project-specific ProGuard rules here.
# By default, the Android SDK uses ProGuard version 6.2.2.
# You can control the set of applied configuration files using the
# proguardFiles setting in the build.gradle file.
#
# For more details, see:
#   https://developer.android.com/studio/build/shrink-code

# Optimization passes eliminate unused code and reduce the size of the APK.
# Keep in mind that these options may remove code that is actually needed
# for the application to function correctly. You should test thoroughly
# after enabling these options.
#
# For more details, see:
#   https://developer.android.com/studio/build/shrink-code#optimization-passes

# Enable optimization passes.
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# Keep the entry points of the application.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.core.app.NotificationCompat$Style
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends androidx.appcompat.app.ActionBar
-keep public class * extends androidx.appcompat.widget.Toolbar
-keep public class * extends androidx.lifecycle.ViewModel
-keep public class * extends androidx.lifecycle.AndroidViewModel

# Keep the classes and methods used by the Android Support Library.
-dontwarn android.support.**
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Keep the classes and methods used by the Google Play Services APIs.
# Note that you should only include the APIs that you actually use in your application.
-keep class com.google.android.gms.common.api.** { *; }
-keep class com.google.android.gms.location.** { *; }
-keep class com.google.android.gms.maps.** { *; }

# Keep the classes and methods used by the Retrofit library.
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Keep the classes and methods used by the OkHttp library.
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Ignore warnings for certain classes and attributes.
-dontwarn javax.management.**
-dontwarn org.xml.sax.**

# Renames obfuscated class members to shorter names.
-obfuscationdictionary dictionary.txt# Add project-specific ProGuard rules here.
                                     # By default, the Android SDK uses ProGuard version 6.2.2.
                                     # You can control the set of applied configuration files using the
                                     # proguardFiles setting in the build.gradle file.
                                     #
                                     # For more details, see:
                                     #   https://developer.android.com/studio/build/shrink-code

                                     # Optimization passes eliminate unused code and reduce the size of the APK.
                                     # Keep in mind that these options may remove code that is actually needed
                                     # for the application to function correctly. You should test thoroughly
                                     # after enabling these options.
                                     #
                                     # For more details, see:
                                     #   https://developer.android.com/studio/build/shrink-code#optimization-passes

                                     # Enable optimization passes.
                                     -optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

                                     # Keep the entry points of the application.
                                     -keep public class * extends android.app.Activity
                                     -keep public class * extends android.app.Application
                                     -keep public class * extends android.app.Service
                                     -keep public class * extends android.content.BroadcastReceiver
                                     -keep public class * extends android.content.ContentProvider
                                     -keep public class * extends androidx.core.app.NotificationCompat$Style
                                     -keep public class * extends androidx.fragment.app.Fragment
                                     -keep public class * extends androidx.appcompat.app.AppCompatActivity
                                     -keep public class * extends androidx.appcompat.app.ActionBar
                                     -keep public class * extends androidx.appcompat.widget.Toolbar
                                     -keep public class * extends androidx.lifecycle.ViewModel
                                     -keep public class * extends androidx.lifecycle.AndroidViewModel

                                     # Keep the classes and methods used by the Android Support Library.
                                     -dontwarn android.support.**
                                     -dontwarn androidx.**
                                     -keep class androidx.** { *; }
                                     -keep interface androidx.** { *; }

                                     # Keep the classes and methods used by the Google Play Services APIs.
                                     # Note that you should only include the APIs that you actually use in your application.
                                     -keep class com.google.android.gms.common.api.** { *; }
                                     -keep class com.google.android.gms.location.** { *; }
                                     -keep class com.google.android.gms.maps.** { *; }

                                     # Keep the classes and methods used by the Retrofit library.
                                     -keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault
                                     -keepclassmembers,allowshrinking,allowobfuscation interface * {
                                         @retrofit2.http.* <methods>;
                                     }
                                     -keep class retrofit2.** { *; }
                                     -keep interface retrofit2.** { *; }

                                     # Keep the classes and methods used by the OkHttp library.
                                     -keep class okhttp3.** { *; }
                                     -keep interface okhttp3.** { *; }

                                     # Ignore warnings for certain classes and attributes.
                                     -dontwarn javax.management.**
                                     -dontwarn org.xml.sax.**

                                     # Renames obfuscated class members to shorter names.
                                     -obfuscationdictionary dictionary.txt