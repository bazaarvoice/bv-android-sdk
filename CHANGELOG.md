# Changelog

# 4.2.0

## **NEW** Location

This release provides a new module for adding location awareness to your app. This module facilitates 
knowing when your shoppers are entering and exiting your physical retail locations. You will be 
provided a very valuable opportunity to present a shopper with your location aware content. Perhaps 
you have a simple greeting or coupon? Aside from the value you add within your mobile experience, 
Bazaarvoice can use this location context to enhance our existing products. Rating and Reviews 
filtered down to the city level, recommended products for nearby people like you, local social media 
content (Curations), and more are all possible by using the Location module.

For more information, please refer to the location documenation 
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

This release provides a vast improvement over the current display and submission API for Conversations. 
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