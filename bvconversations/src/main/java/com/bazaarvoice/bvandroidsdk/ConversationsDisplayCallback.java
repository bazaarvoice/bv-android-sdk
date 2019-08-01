package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public interface ConversationsDisplayCallback<T extends ConversationsResponse> {
  void onSuccess(@NonNull T response);
  void onFailure(@NonNull ConversationsException exception);
}
