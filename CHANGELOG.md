## 2.0.3 (July 19, 2013)
* Changed http from Apache HttpClient to HttpUrlConnection.
* fixed post requests to send all the data in the body
* removed mimetype related code, now sending everything using Octet-Stream
* no longer an option to send blocking http requests
* changed any method not needed by user to protected to hide it behind API 
* when building parameter strings for URL, we now use StringBuilder as opposed to concatenating strings
* simplified logic by spliting code used to build GET and POST requests in their appropriate method, as opposed to the send method of BazaarRequest

## 2.0.2 (Jun 10, 2013)
* Added the option to upload a file without using a byte array
* Stopped cloning the byte array during async uploads.

## 2.0.1 (Feb 6, 2013)
* API Version 5.4 support

## 2.0.0 (Feb 1, 2013)
* Major SDK Overhaul -- Adds com.bazaarvoice.types, parameter structure simplified

## 1.2.1 (Oct 1, 2012)

* Adds functionality for includes search ```Search_[TYPE]```

## 1.2.0 (Aug 16, 2012)

* Now officially supporting API 5.3.
* Now uses IncludeType enum for ```Sort_[TYPE]``` and ```Limit_[TYPE]```.

## 1.1.2 (Aug 8, 2012)

* Added OnUiBazaarResponse to ease making UI changes when responses come in.
* Updated reference apps a bit.

## 1.1.1 (Jul 30, 2012)

* Added Product Browser Widget example.
* Fixed issue with ```tag_<dimension>``` and ```rating_<dimension>``` parameters.

## 1.1.0 (Jul 26, 2012)

* Adds Feedback submission. Now supporting API 5.2.
* Type for ```Includes``` and ```Filter_[TYPE]``` is now an enum.
* API Version parameter is now an enum.

