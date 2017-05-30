# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/bazaarvoice/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn com.squareup.okhttp.*
-dontwarn com.google.android.gms.gcm.GoogleCloudMessaging
-dontwarn org.codehaus.jackson.annotate.JsonIgnore
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder

### OKHTTP

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote okhttp3.internal.Platform


### OKIO

# java.nio.file.* usage which cannot be used at runtime. Animal sniffer annotation.
-dontwarn okio.Okio


