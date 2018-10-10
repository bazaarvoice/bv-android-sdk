package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * all errors known/unknown go to failure
 */
public class LoadCallV7Test extends LoadCallTest {
  // region display sync

  @Test
  public void displaySyncV7FromMainThread_Should_ThrowException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    expectedException.expect(ConversationsException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    LoadCallDisplay<ReviewsRequest, ReviewResponse> callDisplay = client.prepareCall(request);
    callDisplay.loadDisplaySync();
  }

  @Test
  public void displaySyncV7NoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadDisplaySync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertNotNull(reviewResponse);
    assertEquals(10, reviewResponse.getLimit().intValue());
    assertEquals(20, reviewResponse.getTotalResults().intValue());
    final Review firstReview = reviewResponse.getResults().get(0);
    assertEquals("16970619", firstReview.getId());
  }

  @Test
  public void displaySyncV7BvError_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadDisplaySync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsException exception = (ConversationsException) e.getCause();
      assertNotNull(exception);
      assertEquals("Request has errors", exception.getMessage());
      Error firstError = exception.getErrors().get(0);
      assertEquals(ErrorCode.ERROR_PARAM_INVALID_FILTER_ATTRIBUTE, firstError.getErrorCode());
    }

    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void displaySyncV7JsonParseError_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadDisplaySync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsException exception = (ConversationsException) e.getCause();
      assertNotNull(exception);
      assertEquals("Unable to parse JSON", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void displaySyncV7ConnectTimeout_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
        loadCallDisplay.cancel();
        return loadCallDisplay.loadDisplaySync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsException exception = (ConversationsException) e.getCause();
      assertNotNull(exception);
      assertEquals("Execution of call failed", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region display async

  @Test
  public void displayAsyncV7NoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewResponse response) {
        assertNotNull(response);
        assertFalse(response.getHasErrors());
        assertEquals(10, response.getLimit().intValue());
        assertEquals(20, response.getTotalResults().intValue());
        final Review firstReview = response.getResults().get(0);
        assertEquals("16970619", firstReview.getId());

      }

      @Override
      public void onFailure(@NonNull ConversationsException exception) {
        exception.printStackTrace();
        fail("Should not call onFailure");
      }
    });
  }

  @Test
  public void displayAsyncV7BvError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewResponse response) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsException exception) {
        assertNotNull(exception);
        Error firstError = exception.getErrors().get(0);
        assertEquals(ErrorCode.ERROR_PARAM_INVALID_FILTER_ATTRIBUTE, firstError.getErrorCode());
      }
    });
  }

  @Test
  public void displayAsyncV7JsonParseError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse response) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
      }
    });
  }

  @Test
  public void displayAsyncV7IoError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.cancel();
    loadCallDisplay.loadAsync(new ConversationsDisplayCallback<ReviewResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewResponse response) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsException exception) {
        assertNotNull(exception);
      }
    });
  }

  @Test
  public void displayAsyncV7_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.loadAsync(new ConversationsDisplayCallback<ReviewResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewResponse response) {
        assertNotNull(loadCallDisplay.displayV7Callback);
        assertNotNull(response);
      }

      @Override
      public void onFailure(@NonNull ConversationsException exception) {
        fail(exception.getMessage());
      }
    });
    assertNull(loadCallDisplay.displayV7Callback);
  }

  @Test
  public void displayAsyncV7_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.loadAsync(new ConversationsDisplayCallback<ReviewResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsException exception) {
        assertNotNull(loadCallDisplay.displayV7Callback);
        assertNotNull(exception);
      }
    });
    assertNull(loadCallDisplay.displayV7Callback);
  }

  // endregion

  // region preview submission sync

  @Test
  public void previewSyncV7FromMainThread_Should_ThrowWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    expectedException.expect(ConversationsSubmissionException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    call.loadSubmissionSync();
  }

  @Test
  public void previewSyncV7NoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewSubmissionResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertNotNull(reviewResponse);
    assertNotNull(reviewResponse.getFormFields());
  }

  @Test
  public void previewSyncV7BvError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException convException = (ConversationsSubmissionException) e.getCause();
      assertNotNull(convException);
      assertEquals("Request has errors", convException.getMessage());
      assertEquals(1, convException.getErrors().size());
      Error firstError = convException.getErrors().get(0);
      assertEquals(ErrorCode.ERROR_PARAM_INVALID_API_KEY, firstError.getErrorCode());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void previewSyncV7BvFormError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException convException = (ConversationsSubmissionException) e.getCause();
      assertNotNull(convException);
      assertEquals("Request has errors", convException.getMessage());
      assertEquals(1, convException.getFieldErrors().size());
      FieldError firstFieldError = convException.getFieldErrors().get(0);
      assertNotNull(firstFieldError);
      assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, firstFieldError.getErrorCode());
      assertEquals("You must agree to the terms and conditions.", firstFieldError.getMessage());
      assertEquals("agreedtotermsandconditions", firstFieldError.getField());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void previewSyncV7JsonParseError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException exception = (ConversationsSubmissionException) e.getCause();
      assertNotNull(exception);
      assertEquals("Unable to parse JSON", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void previewSyncV7ConnectTimeout_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    final BVConversationsClient client = clientRule.getClient();
    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return call.loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      e.printStackTrace();
      ConversationsSubmissionException exception = (ConversationsSubmissionException) e.getCause();
      assertNotNull(exception);
      assertEquals("Execution of call failed", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region submit submission sync

  @Test
  public void submitSyncV7FromMainThread_Should_ThrowWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    expectedException.expect(ConversationsSubmissionException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    call.loadSubmissionSync();
  }

  @Test
  public void submitSyncV7NoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewSubmissionResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertNotNull(reviewResponse);
    assertNotNull(reviewResponse.getFormFields());
  }

  @Test
  public void submitSyncV7BvError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException convException = (ConversationsSubmissionException) e.getCause();
      assertNotNull(convException);
      assertEquals("Request has errors", convException.getMessage());
      assertEquals(1, convException.getErrors().size());
      Error firstError = convException.getErrors().get(0);
      assertEquals(ErrorCode.ERROR_PARAM_INVALID_API_KEY, firstError.getErrorCode());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSyncV7BvFormError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException convException = (ConversationsSubmissionException) e.getCause();
      assertNotNull(convException);
      assertEquals("Request has errors", convException.getMessage());
      assertEquals(1, convException.getFieldErrors().size());
      FieldError firstFieldError = convException.getFieldErrors().get(0);
      assertNotNull(firstFieldError);
      assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, firstFieldError.getErrorCode());
      assertEquals("You must agree to the terms and conditions.", firstFieldError.getMessage());
      assertEquals("agreedtotermsandconditions", firstFieldError.getField());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSyncV7JsonParseError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      ConversationsSubmissionException exception = (ConversationsSubmissionException) e.getCause();
      assertNotNull(exception);
      assertEquals("Unable to parse JSON", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSyncV7ConnectTimeout_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    final BVConversationsClient client = clientRule.getClient();
    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return call.loadSubmissionSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      e.printStackTrace();
      ConversationsSubmissionException exception = (ConversationsSubmissionException) e.getCause();
      assertNotNull(exception);
      assertEquals("Execution of call failed", exception.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region form submission async

  @Test
  public void previewAsyncV7FormPreview_Should_CallSuccessWithFormFields() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Form);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        assertNotNull(response.getFormFields());
        assertEquals(38, response.getFormFields().size());
        assertFalse(response.getHasErrors());
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        fail("Should not call onFailure");
      }
    });
  }

  // endregion

  // region preview submission async

  @Test
  public void previewAsyncV7NoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        assertNotNull(response);
        assertFalse(response.getHasErrors());
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        exception.printStackTrace();
        fail("Should not fail");
      }
    });
  }

  @Test
  public void previewAsyncV7BvError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertEquals(1, exception.getErrors().size());
        Error firstError = exception.getErrors().get(0);
        assertEquals(ErrorCode.ERROR_PARAM_INVALID_API_KEY, firstError.getErrorCode());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  @Test
  public void previewAsyncV7BvFormError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertEquals(1, exception.getFieldErrors().size());
        final FieldError fieldError = exception.getFieldErrors().get(0);
        assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, fieldError.getErrorCode());

        final FormField formField = fieldError.getFormField();
        assertEquals("agreedtotermsandconditions", formField.getId());
        assertEquals(FormInputType.BOOLEAN, formField.getFormInputType());
        assertEquals("false", formField.getValue());
      }
    });
  }

  @Test
  public void previewAsyncV7JsonParseError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(exception);
        assertEquals("Unable to parse JSON", exception.getMessage());
        assertEquals(0, exception.getErrors().size());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  @Test
  public void previewAsyncV7ConnectTimeout_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(exception);
        assertEquals("Execution of call failed", exception.getMessage());
        assertEquals(0, exception.getErrors().size());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  @Test
  public void previewAsyncV7_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        assertNotNull(call.submitV7Callback);
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        fail("Should not call onFailure");
      }
    });
    assertNull(call.submitV7Callback);
  }

  @Test
  public void previewAsyncV7_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(call.submitV7Callback);
      }
    });
    assertNull(call.submitV7Callback);
  }

  // endregion

  // region submit submission async

  @Test
  public void submitAsyncV7NoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        assertNotNull(response);
        assertFalse(response.getHasErrors());
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        exception.printStackTrace();
        fail("Should not fail");
      }
    });
  }

  @Test
  public void submitAsyncV7BvError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertEquals(1, exception.getErrors().size());
        Error firstError = exception.getErrors().get(0);
        assertEquals(ErrorCode.ERROR_PARAM_INVALID_API_KEY, firstError.getErrorCode());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  @Test
  public void submitAsyncV7BvFormError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertEquals(1, exception.getFieldErrors().size());
        FieldError fieldError = exception.getFieldErrors().get(0);
        assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, fieldError.getErrorCode());
      }
    });
  }

  @Test
  public void submitAsyncV7JsonParseError_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(exception);
        assertEquals("Unable to parse JSON", exception.getMessage());
        assertEquals(0, exception.getErrors().size());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  @Test
  public void submitAsyncV7ConnectTimeout_Should_CallFailureWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(exception);
        assertEquals("Execution of call failed", exception.getMessage());
        assertEquals(0, exception.getErrors().size());
        assertEquals(0, exception.getFieldErrors().size());
      }
    });
  }

  // endregion

  // region callbacks NEXUS_USERd

  @Test
  public void submitAsyncV7_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        assertNotNull(call.submitV7Callback);
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        fail("Should not call onFailure");
      }
    });
    assertNull(call.submitV7Callback);
  }

  @Test
  public void submitAsyncV7_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
        assertNotNull(call.submitV7Callback);
      }
    });
    assertNull(call.submitV7Callback);
  }

  // endregion
}
