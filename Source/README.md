bv-android-sdk-dev
==================

Bazaarvoice Android SDK Source Code Development Repo

The Android version is currently set to API Level 3. The var is in project.properties.

#How to contribute to the project

1. Fork this repo
2. Make your edits
3. Do a pull request
	- Please make sure to discribe in your comments what was changed and why.
	- **If you are doing something, code wise, which is not intuitive to a majority of developers, please properly comment your code for ease of maintenance.**

System Requirements
-

You should have the Android SDK installed on your system. If you need it, go to: http://developer.android.com/sdk/index.html

If you didn't have the SDK, chances are you don't have the ADT plugin for Eclipse either. Get that by following the steps here: http://developer.android.com/sdk/installing/installing-adt.html

This is intended to work with Eclipse but it should work with IntelliJ IDEA as well.

Building The Library
-
**Note: the Tests directory already has the library included in it, but this is necessary for future builds.**

From the command line:

1. Navigate to the BVAndroidSDK directory.
2. Type ```/path/to/android-sdk/tools/android update lib-project --path .```
3. Type ```ant clean release```
4. The library should be in the /bin directory under the name ```classes.jar```. You can rename it as you like.
5. To use the library, add it to the /libs directory in whatever application or test application you are building.

With Eclipse:

1. Open Eclipse and select File -> Import...
2. Under General, choose "Existing projects into workspace".
3. Next to "Select Root Directory", _Browse..._ to the BVAndroidSDK directory and select Open.
4. Click Finish.
5. By default, Eclipse will build the project automatically.  Otherwise, build the project by selecting Project -> Build Project.  The library should be in the /bin directory under the name ```bazaarandroidsdk.jar```. You can rename it as you like.
6. To use the library, add it to the /libs directory in whatever application or test application you are building.

Running the Unit Tests
-

From the Command Line:

1. Navigate to the /Tests directory.
2. Type ```/path/to/android-sdk/tools/android update project --path .```
3. Type ```ant debug install run-tests```

With Eclipse:

1. Open Eclipse and select File -> Import...
2. Under General, choose "Existing projects into workspace".
3. Next to "Select Root Directory", _Browse..._ to the Tests directory and select Open.
4. Click Finish.
5. Run each test individually (or in groups) by selecting the class (or package) and clicking run.

Generate Docs
-

With Eclipse:

1. Navigate to Project -> Generate Javadoc
2. Under "Select types for which Javadoc will be generated", highlight only src/com.bazaarvoice and src/com.bazaarvoice.types.  Within com.bazaarvoice, exclude Utils.java and Media.java, since these are not client-facing.
3. Note the output directory.
4. Click finish and output javadoc.

License
-

Our SDK is distributed under the Apache 2.0 license, so go nuts. The SDK shall be used for Good, not Evil.

