# Android with components *with* ProGuard.
# ProGuard is a Java optimizer and obfuscator that helps reduce the size of
# your app and makes it harder for people to reverse engineer.
# For more details, see:
#   http://developer.android.com/guide/developing/tools/proguard.html
#   http://proguard.sourceforge.net/index.html

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files with the
# proguardFiles setting in build.gradle.
#
# For more details on creating rules, see the ProGuard manual:
#   http://proguard.sourceforge.net/manual/usage.html
#
# A common pattern is to keep method names for native JNI methods
# with the @Keep annotation or the -keepattributes *Annotation* option.
# Without this, the obfuscated method names will not match the names
# expected by the native code.

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces you get when ProGuard has obfuscated
# your code. This will make the stack traces longer and harder
# to read, but it might help you in debugging issues.
#-keepattributes SourceFile,LineNumberTable

# If you have a native library that you want to keep all its classes,
# you can use the following rule:
#-keep class * extends java.lang.Object

# Keep names of classes that implement Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep R classes
-keep class **.R$* {
    <fields>;
}

# Keep Room database classes
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.Database { *; }
-keep class * extends androidx.room.Entity { *; }
-keep class * extends androidx.room.Dao { *; }
