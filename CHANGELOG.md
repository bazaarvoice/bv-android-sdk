# Changelog
# 7.1.0
## Analytics Updates
* Completed rollout of BVPixel migration
* Removed deprecated analytics schemas

Please see commits for details.


# 7.0.0
## Updated minSdkVersion , updated dependencies supported, Removed Deprecated APIs

* The minSdkVersion is now 16.
* Updated support for various dependencies.
* Removed GridViews: `BVGridView, AnswersGridView, QuestionsGridView, RecommendationsGridView`
* Removed ListViews: `BVListView, AnswersListView, QuestionsListView, RecommendationsListView`
* Bug fix for CurationsFeedItem
#### DevicePrint SDK 
* Adds support for Iovation's Deviceprint sdk 3.0
* Removes support for Iovation's Deviceprint sdk under version 3.0

Please see commit for details.

# 6.14.5

## Bug Fix

Fixed BVTransaction additional parameters issue. Please see commit for details.

# 6.14.4

## Bug Fix

Fixed GSON parsing issue for configuration. Please see commit for details.

# 6.14.3

## Analytics and Location

Adding support for Analytics Routing based on user location/residence. Please see commit for details.

# 6.14.2

## Bug Fix

Add proper logic to test functionality for 6.14.1 changes. Please see commit for details.

# 6.14.1

## Bug Fix

Ameliorate logic inversion of async Conversation Submission path. Please see commit for details.

# 6.14.0

## Removed PIN, PIN Notifications, and Advertising

* Removed PIN module
* Removed PIN Notifications module
* Removed Advertising module
* Removed PIN API key from `BVConfig.Builder` and `BVSDK.Builder`.

This are all technically breaking changes, but no one is using them, nor do they do function, so it should
not affect anyone.


# 6.13.0

## Conversations

### New Authentication Interfaces

Added a new `AuthenticationProvider` interface that should be implemented, and provided to each submission
request. e.g.

```kotlin
val submitReq = ReviewSubmissionReq.Builder(Action.Submit, "testProd")
  .authenticationProvider(authProvider)
  .build()
client.prepareCall(submitReq).loadAsync({
  (resp) -> {
    // all good, change Action to Submit to actually send   
  },
  (convSubmitException) -> {
    val bvErrors = convSubmitException.getErrors()
    val bvFieldErrors = convSubmitException.getFieldErrors()
    for (fieldError in bvFieldErrors) {
      val submissionErrorCode = fieldError.getSubmissionErrorCode()
      val relevantFormField = fieldError.getFormField() // gets the form field info
    }
  }
})
```

* If you are configured for BV Hosted Authentication then you should use `BVHostedAuthenticationProvider`.
* If you are configured for Site Authentication then you should use `SiteAuthenticationProvider`.

For more info on how to use/create these providers, and how to check which one you are configured for,
refer to the `Conversations > Content Submission` page in the docs.  


# 6.12.0

## Conversations

### New success/failure routing, and new Action type

#### Guide

Previously fetching form data and parsing form errors was difficult. Not all paths of `Action` routed to the same callback, and most error paths did not have the required data to parse programmatically. All of the previous APIs for doing these things remains in the 6.x SDK for now, but has been marked as deprecated in favor of these new APIs.

##### Get Form Data

```kotlin
val submitReq = ReviewSubmissionReq.Builder(Action.Form, "testProd") // NEW ACTION TYPE
  .build()
client.prepareCall(submitReq).loadAsync({
  (resp) -> {
    val hasErrors = resp.getHasErrors() // not required to check anymore, always false in success
    val formFields = resp.getFormFields() // null-safe list of available form options
    for (formField in formFields) {
      val isRequired = formField.isRequired()  // indicates you must send this field
    }      
  },
  (convSubmitException) -> {
    val bvErrors = convSubmitException.getErrors() // null-safe list of generic errors
    val bvFormErrors = convSubmitException.getFieldErrors() // null-safe list of form errors
  }
})
```

##### Test Submission

```kotlin
val submitReq = ReviewSubmissionReq.Builder(Action.Preview, "testProd")
  .build()
client.prepareCall(submitReq).loadAsync({
  (resp) -> {
    // all good, change Action to Submit to actually send   
  },
  (convSubmitException) -> {
    val bvErrors = convSubmitException.getErrors()
    val bvFieldErrors = convSubmitException.getFieldErrors()
    for (fieldError in bvFieldErrors) {
      val submissionErrorCode = fieldError.getSubmissionErrorCode()
      val relevantFormField = fieldError.getFormField() // gets the form field info
    }
  }
})
```

##### Submit

Same as Test Submission, except use `Action.Submit` and it actually sends.

#### Details
* Added `Action.Form` for submission to route to success without required form fields. `Action.Preview` and `Action.Submit` will always route to failure if all required form fields are not provided
* Added `ConversationsDisplayCallback`
* Added `ConversationsSubmissionCallback`
* Added `ConversationsException` which has `#getErrors()`
* Added `ConversationsSubmissionException` which has `#getFieldErrors()`
* Added `LoadCallDisplay#loadDisplaySync()`
* Deprecated `LoadCallDisplay#loadSync()` in favor of `LoadCallDisplay#loadDisplaySync()`
* Added `LoadCallDisplay#loadAsync(conversationsDisplayCallback)`
* Deprecated `LoadCallDisplay#loadAsync(conversationsCallback)` in favor of `LoadCallDisplay#loadAsync(conversationsDisplayCallback)`
* Added `LoadCallSubmission#loadSubmissionSync()`
* Deprecated `LoadCallSubmission#loadSync()` in favor of `LoadCallSubmission#loadSubmissionSync()`
* Added `LoadCallSubmission#loadAsync(conversationsSubmissionCallback)`
* Deprecated `LoadCallSubmission#loadAsync(conversationsCallback)` in favor of `LoadCallSubmission#loadAsync(conversationsSubmissionCallback)`

### Other Stuff
* Update `BazaarRuntimeException` to not cause StackOverflowException
* Update JSON parse failure message
* Update `BVErrors` exist message
* Added `BVConversationsClientTest` to test all routes
* Updated Robolectric version for testing
* Added testing shadow for network security


# 6.11.0

## Conversations
* Added `ErrorCode` enum to parse the code strings from the `Errors` JSON field
* Added `Error#getErrorCode()` method for getting the enum
* Added unit tests for invalid error code parsing, valid error code parsing, and generic error list parsing
* Added `SubmissionErrorCode` enum to parse the code strings from the `FieldErrors` JSON field for submission
* Added `FieldError#getErrorCode()` method for getting the enum
* Added unit tests for invalid field error code parsing, valid field error code parsing, and generic field error
list parsing

# 6.10.0

## Conversations
* Add `Author#getSecondaryRatingList()`
* Add `BaseReview#getSecondaryRatingList()`
* Add `IncludeContentBase#getBadgeList()`
* Add `IncludeContentBase#getContextDataValueList()`
* Fix `BaseReviewBuilder#addContextDataValueString(String dataValueName, boolean value)` to be
named `BaseReviewBuilder#addContextDataValueBoolean(String dataValueName, boolean value)`
* Remove empty `ReviewSubmissionRequest#newBuilder()` method
* Add `ConversationsSubmissionRequest.Builder#addCustomSubmissionParameter(String key, String value)`
* Add internal FormPair class to store user custom form parameters
* Add `ConversationsDisplayRequest.Builder#ConversationsDisplayRequest.Builder#addCustomDisplayParameter(String key, String value)`
* Deprecated `ConversationsDisplayRequest.Builder#addAdditionalField(String key, String value)`
in favor of `addCustomDisplayParameter`
* Added `FormInputType` enum to introduce a strongly typed version of `String type = FormField#getType()`.
The possible values were taken from [the conversations docs on input types](https://developer.bazaarvoice.com/conversations-api/tutorials/submission/input-types). This will allow for easier parsing of which form fields have many options. e.g.

```java
final FormInputType formInputType = formField.getFormInputType();
switch(formInputType) {
  case FormInputType.SELECT: {
    List<FormFieldOption> options = formField.getFormFieldOptions();
    // present options to user
    break;
  }
}
```

# 6.9.1

## Conversations
* Added tests for all of the endpoints to verify they are what we expect them to be
* Updated ```BulkRatingsRequest``` to send with endpoint ```statistics.json``` like it should and was before, instead of ```reviews.json```

## Demo API App
* Added okhttp3 logging-interceptor
* Updated TextView to have maxLines of 5 so it won't take up the entire screen on the DemoBulkRatingsActivity screen

## Common
* Updated okhttp3 to version 3.8.0

# 6.9.0

## Conversations
* Added ```BVConversationsClient.Builder#fingerprintProvider(fpProvider)``` as an optional builder parameter

## **NEW** Authenticity Iovation
* Added ```IovationFingerprint``` as a class to pass to the ```fingerprintProvider(fpProvider)``` method

# 6.8.1

## Conversations
* Fixed expected behavior of ```ConversationsDisplayRequest.Builder#addAdditionalField(key, val)``` to support
one-to-many relationships of key-value pairs.

# 6.8.0

## Conversations
* Added multi-config support to the ```BVConversationsClient```. To use it, use the new ```BVConversationsClient.Builder```
to construct an instance, and use the ```BVConversationsClient.Builder#bvConfig(bvConfig)``` method to provide a
different config than the default provided by the ```BVSDK``` singleton. More info available on [the docs](https://bazaarvoice.github.io/bv-android-sdk/conversations_display.html)
for Conversations.
* Deprecated the default ```BVConversationsClient``` constructor.
* Add multiple value support to review display filtering

## BVPixel (Analytics)
* Added an additional API to optionally send an analytics event with a different ```clientId``` than the default
provided to the ```BVPixel``` singleton instance. More info available on [the docs](https://bazaarvoice.github.io/bv-android-sdk/bvpixel_implementation.html)
for BVPixel.

# 6.7.4

## Conversations
* Added back ```ReviewStatistics``` for the Products in the Includes section of ```ReviewsRequest```s

# 6.7.3

## Conversations
* Added ```Question#getSyndicatedSource()```, ```Answer#getSyndicatedSource()```, and ```Comment#getSyndicatedSource()```

# 6.7.2

## Conversations
* Added ```ReviewSubmissionRequest.Builder#addVideoUrl(videoUrl, optionalVideoCaption)```
* Added ```Question#isSyndicated()``` and ```Review#isSyndicated```

# 6.7.1

## Conversations
* Added ```CommentSubmissionRequest``` and ```CommentSubmissionResponse```
* Added ability to include ```Comment```s in ```AuthorsRequest```s
* Added ```AuthorIncludeType``` to be used for includes in ```AuthorsRequest```s instead of ```PDPContentType```

# 6.7.0

## Conversations
* Added ```CommentsRequest``` and ```CommentsResponse```
* Added ```ReviewIncludeType.COMMENTS``` for ```ReviewsRequest```s

# 6.6.4

## Conversations
* Added ```FormData``` to parse form fields in ```ConversationsCallback#onSuccess(response)```
* Now routing ```FormError``` to ```ConversationsCallback#onSuccess(response)```

# 6.6.3

## Conversations
* Added ```SyndicationSource``` to the ```BaseReview``` object for when ```IsSyndicated``` is true
* Added ```LoadCall#cancel()``` to be able to cancel a network request
* Updated ```ConversationsRecyclerView``` to retain a strong reference to the passed in ```LoadCallDisplay``` object, making sure to cancel the network request, and null out the ```LoadCallDisplay``` reference in ```View#onDetachedFromWindow()```. This will ensure that anonymous ```ConversationsCallbacks``` are not GC'ed before the request ends

# 6.6.2

## Conversations
* Fixed an issue where the results were still being used by the background thread after delivering them to the user on the UI thread, which could cause a ```ConcurrentModificationException```.
* Added ```getSyndicated()``` to the ```BaseReview``` object so that the ```IsSyndicated``` flag will now be parsed for reviews

# 6.6.1

## BV Demo and BV API apps
* Added defaulting to Conversations Product Catalog on homepage of BV Demo app if Recommendations keys are not available
* Updated theme in both apps

## Conversations
* ```BulkProductRequest``` and ```BulkProductResponse``` classes added. They are the same as ```ProductDisplayPageRequest``` and ```ProductDisplayPageResponse```, but do not require a product id to construct.

# 6.6.0

## Common
* Added ```BVSDK.builder(application, bazaarEnvironment)``` static factory to build the sdk with which uses Bazaarvoice generated config files to configure the BVSDK instance for you
* Deprecated ```BVSDK.builder(application, clientId)```

## Conversations
* Fixed Issue #26 - Now correctly parsing Bazaarvoice API response date fields as UTC timezone rather than local timezone

# 6.5.0

## **NEW** BVPixel (Analytics)
* Added analytics module with ```BVPixel``` interface. Now allow a simple interface to build and send analytic events without having to get into the nitty gritty of each required field
* Added BVPixel under the hood for Curations analytics
* Deprecated ```include=answers``` which our sdk allows, but does nothing for ```ProductDisplayPageRequest```s
* Added ```ConversationsDisplayRequest#addAdditionalField(String key, String value)``` to match ```BaseReviewBuilder#addAdditionalField(String key, String value)```
* Added ```ReviewIncludeType``` which only contains ```products``` at the moment
* Added ```ReviewDisplayRequestBuilder#addIncludeContent(ReviewIncludeType reviewIncludeType)```

# 6.4.0

## **NEW** Curations UI
* Added Curations UI module which depends on the Curations Module
* Added ```CurationsInfiniteRecyclerView``` widget which manages paging network requests, as well as analytic events
* Removed all old unused Curations UI Widgets
* Updated Code Sample App and Demo App to use the new ```CurationsInfiniteRecyclerView```

# 6.3.0

## Conversations

* Added ```AuthorsRequest``` and ```AuthorsResponse``` to retrieve a Conversations
Author Profile
* Deprecated the ```QuestionAndAnswerRequest.Builder#addSort(...)``` in favor of the
more explicit ```QuestionAndAnswerRequest.Builder#addQuestionSort(...)```
* Added ability to sort included ```Answer```s with
 ```QuestionAndAnswerRequest.Builder#addAnswerSort(...)```
* Added new Code Sample App and Demo App ability to view an author by id

# 6.2.0

## Conversations

* Removed deprecated Conversations classes
* Fixed URL Encoding of Submission request parameters

## Common

* Added new API wrapper for ```BVPixel``` and removed the relevant calls from ```BVSDK```

## **NEW** PIN API

* Added ```PinClient``` to get a list of products that have been purchased, and need to be reviewed

## **NEW** PIN Notifications

* Added a ```PinNotificationManager``` to queue a Post Interaction Notification based on a product id. These notifications
are remotely configurable on our backend

## Store Notifications

* Removed notifications from the Location module itself, so that this is a stand alone dependency

## **NEW** Notifications

* Added a new module for the base code to fetch remote notification configuration, and manage queueing

## Location

* Added new preferred way to subscribe to events, by listening to intents with an action of
```com.bazaarvoice.bvandroidsdk.action.GEOFENCE_VISIT```   

## General

* Added demo and code sample apps to have a cart to test ```BVPixel```

# 5.2.1

## Conversations

* Fix ```FeedbackSubmissionRequest``` constructor to not require ```Action``` parameter

# 5.2.0

## Conversations

* Added support for submitting feedback. You may now send helpfulness or flag inappropriate content for reviews, questions, or answers

# 5.1.3

## Conversations

* Fix ```BrandImageLogoURL``` correctly moved to Answer model
* Fix ```DateUtil``` parsing error that occurs on Android API 16-23, and add better catching around it

# 5.1.2

## Conversations

* Fix ```BrandImageLogoURL``` missing from Question response
* Fix sort parameter for ```QuestionAndAnswerRequest```
* Fix typo in ```RatingDistribution``` class
* Fix ```DateUtil``` parser

# 5.1.1

## Recommendations

* Make Recommendations constructor public. Recommendations was and still is available through the android View based methods, but this allows View independent access

# 5.1.0

## Curations

* Added support to geo-tag custom content submission with the Curations Submission API.

# 5.0.0

This NEXUS_USER provides a refresh to the Conversations API as well new features for geofence events.

## Conversations

* Added Conversations for Stores support
* Added Rich Push notification support for requesting store reviews
* Added Notification support integrated with BVLocation module (store geofence support)
* Fixed some max limit bugs in Conversations Display requests for Reviews and Bulk Ratings

## Curations

* Added ```CurationsFeedRequest.Builder#location(latitude, longitude)``` parameter to allow searching
for Curations feed items by location

# 4.2.0

## **NEW** Location

This NEXUS_USER provides a new module for adding location awareness to your app. This module facilitates
knowing when your shoppers are entering and exiting your physical retail locations. You will be
provided a very valuable opportunity to present a shopper with your location aware content. Perhaps
you have a simple greeting or coupon? Aside from the value you add within your mobile experience,
Bazaarvoice can use this location context to enhance our existing products. Rating and Reviews
filtered down to the city level, recommended products for nearby people like you, local social media
content (Curations), and more are all possible by using the Location module.

For more information, please refer to the location documentation
 https://bazaarvoice.github.io/bv-android-sdk/location.html

# 4.1.4

## Conversations

* Fixed visibility of methods that should be public

# 4.1.3

## Conversations

* Fixed NPE in ConversationsAnalyticsManager for response with no products

# 4.1.2

## Conversations

* Fixed AnswerOptions.Sort visibility from package-private to public

# 4.1.1

## Conversations

* Fixed Review response parsing, and updated unit tests to cover all expected response parsing

# 4.1.0

## Conversations

### Conversations Revamped

This NEXUS_USER provides a vast improvement over the current display and submission API for Conversations.
The following improvements will:

* decrease implementation time
* provide deserialized responses returned in P.O.J.O (plain old java objects), as opposed to returning
a raw json response string
* make error handling much simpler, (e.g. wrapping legacy conversations API implementation details so
you don't have to worry about it
* provide View container objects, which makes ROI reporting much simpler

We have deprecated the ```BazaarRequest``` class as well as any related classes to implement
Conversation the old way. For any clients migrating from the ```BazaarRequest``` API, please refer to
the upgrade guide: https://bazaarvoice.github.io/bv-android-sdk/upgrading_conversations.html

### Other Conversations Changes

* Fixed the implementation of the ```DisplayParams#addLimitOnIncludedType(type, limitVal)``` method to
correctly add the ```type``` to the ```limitType``` list

## General

* Updated root dependencies.gradle to distinguish BVSDK artifact depndencies versus Demo App dependencies

# 4.0.5
* Fix BVSDK to enforce singleton pattern
* Remove Scheduled Profile updates

# 4.0.4

## Common
* Fix version number sending for Analytics
* Sending Embedded-Pageview with prod/category id
## Product Recommendations
* No more pending for advertising id in Recommendations requests
## General
* Updated Robolectric and relevant tests

# 4.0.3

## Common
* Added ```BVProduct#getPrice()```
* Expose the ```Profile``` class
* Expose the ```ShopperProfile``` class which contains a ```Profile``` object

## Curations
* Expose the ```CurationsFeedResponse``` class
* Added ```CurationsProduct#getId()```

## General
* Added ```AndroidManifest.xml``` to each module with the ```android.permission.INTERNET ```
permission, so gradle manifest merger should handle implicitly requesting it for
users instead of needing to explicitly request it
* Updated docs with instructions for the new Demo App for best practices, as well as
the Code Example App. Also updated typos, etc.

# 4.0.0

## Advertising
* New changes to the BVAds class that require some minor code changes to migrate from 3.x to 4.x
  * Ads interface removed, instead simply use the BVAds class directly
  * ```Map<String, String> getCustomTargeting()``` is the only method available now through ```BVAds.getCustomTargeting()```\
  * The methods creating Google DFP ad objects can now just be called by directly invoking the Google DFP API,
  e.g. ```getTargetedInterstitialAd(Context context, String adUnitId)``` becomes ```PublisherInterstitialAd interstitialAd = new PublisherInterstitialAd(context); interstitialAd.setAdUnitId(adUnitId);```
* New Support for Bazaarvoice Curations: Ability to display custom social media feeds and post photos from your mobile app. Checkout the example project
* New BVPixel - Analytics for tracking purchase and non-transaction events for ROI reporting and omnichannel profile
* Added usage pattern for BVRecommendations for fetching and displaying product recommendations
* Removed SDK dependency on Google DFP SDK. Existing clients can now more easily add 1st party data to DFP add requests.

# 3.2.1

# General Changes
* Fix for closing out all OkHttp ResponseBody objects

# 3.2.0

## Product Recommendations
* Added ```RecommendationView``` to wrap a view displaying a single ```BVProduct``` and provide better ROI reporting and recommendations
without the need to manually send off analytic events to Bazaarvoice
* Added ```RecommendationsContainerView```, ```RecommendationsListView```, ```RecommendationsGridView```, and ```RecommendationsRecyclerView```
to display many ```RecommendationView```s, and provide additional ROI reporting and better recommendations

# 3.1.0

## Conversations
* Added iovation support to BazaarRequest#postSubmission(RequestType type, BazaarParams params, OnBazaarResponse listener)

# 3.0.1

## Product Recommendations
* New Module - In BETA, provides targeted shopper recommendations

## Advertising
* New Module - Targeted Google DFP Ads with Bazaarvoice profiles. Replaces the deprecated bv-android-ads-sdk﻿

## Conversations
* Deprecated BazaarRequest constructor requiring parameters, in favor of
new no parameter constructor
  * Support for Hosted Auth has been added. In SubmissionParams.java see,
    * SubmissionParams#setHostedAuthCallbackUrl(String hostedAuthCallbackUrl)
    * SubmissionParams#setHostedAuthEmail(String hostedAuthEmail)

## General Changes
* Added sample app to demonstrate the 3 different modules
* Can now install artifacts from Maven Central
