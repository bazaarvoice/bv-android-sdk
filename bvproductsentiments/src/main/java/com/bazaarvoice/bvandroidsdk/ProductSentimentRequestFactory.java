package com.bazaarvoice.bvandroidsdk;

import okhttp3.Request;

interface ProductSentimentRequestFactory {
  <RequestType extends ProductSentimentsRequest> Request create(RequestType request);
}
