bv-android-sdk-dev
==================

Bazaarvoice Android SDK Source Code Development Repo

The Android version is currently set to API 7 (2.1). The var is in project.properties.

The android-sdk directory is currently set to my path (Andy). The var is in local.properties. 


System Requirements
-

You should have the Android SDK installed on your system. If you need it, go to: http://developer.android.com/sdk/index.html

This is intended to work with Eclipse but it should work with IntelliJ IDEA as well.

Building The Library
-

**Note: the Tests directory already has the library included in it, but this is necessary for future builds.**

1. Navigate to the BVAndroidSDK directory.
2. Type ```/path/to/android-sdk/tools/android update lib-project --path .```
3. Type ```ant clean release```
4. The library should be in the /bin directory under the name ```classes.jar```. You can rename it as you like.
5. To use the library, add it to the /libs directory in whatever application or test application you are building.

Running the Unit Tests
-
1. Open Eclipse and select File -> Import...
2. Under General, choose "Existing projects into workspace".
3. Next to "Select Root Directory", _Browse..._ to the Tests directory and select Open.
4. Click Finish.
5. Run each test individually (or in groups) by selecting the class (or package) and clicking run.
