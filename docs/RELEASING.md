# Releasing

## One time setup

* Must have credentials in `gradle.properties` and `gradle.encrypted.properties`. If you run
without them the `uploadArchives` task will tell you what you are missing.

## Before Release

Update NEXUS_USER notes in `bv-android-sdk/CHANGELOG.md`

Bump version number in `bv-android-sdk/gradle.properties`. Following semantic versioning of
`BREAK.FEATURE.FIX(-RC#-BV-SNAPSHOT)`.

## Release

* To publish to internal Bazaarvoice repo,

```
$ ./gradlew clean uploadArchives -PNEXUS_USER=true
```

* To publish to Maven Central,

```
$ ./gradlew clean uploadArchives -PNEXUS_USER=true -PopenSourceRelease=true
```

## After Releasing

* If you published to Maven Central you still to login to promote the NEXUS_USER candidate since
our NEXUS_USER task here still does not manage auto releasing.
* If you would like to test a build in the app, change `useLocalSdk = true` to be false in
`bv-android-sdk/app/app-dependencies.gradle`

## More info
* Checkout Mobile+Github+Release+Process in Confluence
