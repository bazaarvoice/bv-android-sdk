package com.bazaarvoice.bvandroidsdk;

import okhttp3.Request;

interface RequestFactory {
  <RequestType extends ConversationsRequest> Request create(RequestType request);
}
