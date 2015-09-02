# Bazaarvoice Android SDK
*Version: 2.1.1*
***
The Bazaarvoice software development kit (SDK) for Android is an Android static library that provides an easy way to generate REST calls to the Bazaarvoice Developer API. Using this SDK, mobile developers can quickly integrate Bazaarvoice content into their native Android apps.

Developers can display content as well as submit content such as Ratings & Reviews, Ask & Answer and Stories, including text and photo. Included reference apps and a Quick Start Guide will allow developers to integrate Bazaarvoice quickly, with little overhead.

**Where To Start:**  
1.  Read the Quick Start guide (provided for Eclipse and Android Studio projects) to get familiar with installation and building a bare-bones, simple application that uses the SDK.  
2.  The documentation, located in the docs folder, provides additional information into the capabilities of the SDK itself.  
3.  Sample applications, located in the apps folder, provide example uses of the SDK and implementation best practices.    

**A note on universal API requirement:**  
As per the [API Upgrading guide](https://developer.bazaarvoice.com/apis/conversations/upgrading/upgrade_guide), Bazaarvoice is moving to a universal api endpoint, `api.bazaarvoice.com`, instead of `reviews.<customer_name>.bazaarvoice.com` or similar. Therefore, forming a `BazaarRequest` has changed slightly (it's gotten easier!), and an example is available in the Quick Start Guide. 

## Requirements
* Bazaarvoice Platform API version 5.4 or older
* Signed Data Usage Amendment, if not already in place
* API configured and enabled by Bazaarvoice
* API key to access client's staging and production data
* Go to the [Bazaarvoice Developer Portal](http://developer.bazaarvoice.com) to get the above completed
- [Android Studio IDE](https://developer.android.com/sdk/index.html) or [Eclipse IDE with Android Developer Tools](https://developer.android.com/tools/help/adt.html)

## What's Included
This package includes the following:

* Android SDK based on Bazaarvoice Developer API
* Quick Start Guide - Quickly get familiar with installation and building a bare-bones, simple application that uses the SDK
* Reference applications
* **Product browse example:** Illustrates using the Bazaarvoice Android SDK to query, browse and display ratings and reviews for products
* **Review submission example:** Illustrates using the Bazaarvoice Android SDK to submit a photo-review that includes a user generated photo (camera/gallery), star rating and review text.
* **Intent registration example:** Illustrates using the Bazaarvoice Android SDK to register an intent with an Android phone to allow sharing a photo from any context.
* **Product browse widget:** Illustrates using the Bazaarvoice Android SDK to make a widget that displays products and launches a brower app that shows reviews.


## License

## License
See [Bazaarvoice's API Terms of Use](https://developer.bazaarvoice.com/legal/terms_of_use). Except as otherwise noted, the Bazaarvoice Android SDK licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
