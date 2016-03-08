# Changelog

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