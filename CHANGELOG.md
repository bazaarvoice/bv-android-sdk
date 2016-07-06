# Changelog

# 4.0.4

* Fix version number sending for Analytics

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