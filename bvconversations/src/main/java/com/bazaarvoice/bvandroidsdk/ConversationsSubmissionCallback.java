package com.bazaarvoice.bvandroidsdk;


import androidx.annotation.NonNull;

public interface ConversationsSubmissionCallback<T extends ConversationsResponse> {
  void onSuccess(@NonNull T response);
  void onFailure(@NonNull ConversationsSubmissionException exception);
}
