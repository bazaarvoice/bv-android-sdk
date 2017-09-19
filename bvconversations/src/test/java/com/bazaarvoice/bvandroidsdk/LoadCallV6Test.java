package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * errors randomly routed to different paths
 */
public class LoadCallV6Test extends LoadCallTest {
  // region display sync

  @Test
  public void displaySyncFromMainThread_Should_ThrowException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    expectedException.expect(BazaarRuntimeException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    LoadCallDisplay<ReviewsRequest, ReviewResponse> callDisplay = client.prepareCall(request);
    callDisplay.loadSync();
  }

  @Test
  public void displaySyncNoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
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
  public void displaySyncBvError_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Request has errors", bazaarException.getMessage());
    }

    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void displaySyncJsonParseError_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Unable to parse JSON", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void displaySyncConnectTimeout_Should_ThrowBazaarException() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewResponse> reviewResponseCallable = new Callable<ReviewResponse>() {
      @Override
      public ReviewResponse call() throws Exception {
        final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
        loadCallDisplay.cancel();
        return loadCallDisplay.loadSync();
      }
    };
    final Future<ReviewResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Execution of call failed", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region display async

  @Test
  public void displayAsyncNoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        assertNotNull(reviewResponse);
        assertFalse(reviewResponse.getHasErrors());
        assertEquals(10, reviewResponse.getLimit().intValue());
        assertEquals(20, reviewResponse.getTotalResults().intValue());
        final Review firstReview = reviewResponse.getResults().get(0);
        assertEquals("16970619", firstReview.getId());
      }

      @Override
      public void onFailure(BazaarException exception) {
        exception.printStackTrace();
        fail("Should not call onFailure");
      }
    });
  }

  @Test
  public void displayAsyncBvError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
      }
    });
  }

  @Test
  public void displayAsyncJsonParseError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
      }
    });
  }

  @Test
  public void displayAsyncIoError_Should_CallFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.cancel();
    loadCallDisplay.loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        fail("Should not get to onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
      }
    });
  }

  @Test
  public void displayAsync_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        assertNotNull(loadCallDisplay.displayCallback);
        assertNotNull(reviewResponse);
      }

      @Override
      public void onFailure(BazaarException exception) {
        fail(exception.getMessage());
      }
    });
    assertNull(loadCallDisplay.displayCallback);
  }

  @Test
  public void displayAsync_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewsRequest request = getStubDisplayRequest();
    clientRule.enqueueHttp200("display_bv_errors.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallDisplay<ReviewsRequest, ReviewResponse> loadCallDisplay = client.prepareCall(request);
    loadCallDisplay.loadAsync(new ConversationsCallback<ReviewResponse>() {
      @Override
      public void onSuccess(ReviewResponse reviewResponse) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(loadCallDisplay.displayCallback);
        assertNotNull(exception);
      }
    });
    assertNull(loadCallDisplay.displayCallback);
  }

  // endregion

  // region preview submission sync

  @Test
  public void previewSyncFromMainThread_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    expectedException.expect(BazaarRuntimeException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadSync();
  }

  @Test
  public void previewSyncNoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewSubmissionResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertNotNull(reviewResponse);
    assertNotNull(reviewResponse.getFormFields());
  }

  @Test
  public void previewSyncBvError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Request has errors", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void previewSyncBvFormError_Should_ReturnImmediateWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewSubmissionResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertEquals(1, reviewResponse.getFieldErrors().size());
    FieldError fieldError = reviewResponse.getFieldErrors().get(0);
    assertNotNull(fieldError);
    assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, fieldError.getErrorCode());
    assertEquals("You must agree to the terms and conditions.", fieldError.getMessage());
    assertEquals("agreedtotermsandconditions", fieldError.getField());
  }

  @Test
  public void previewSyncJsonParseError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Unable to parse JSON", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void previewSyncConnectTimeout_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    final BVConversationsClient client = clientRule.getClient();
    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return call.loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed"); // Failing because of where call is being set
    } catch (ExecutionException e) {
      e.printStackTrace();
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Execution of call failed", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region submit submission sync

  @Test
  public void submitSyncFromMainThread_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("reviews_all_reviews.json");
    final BVConversationsClient client = clientRule.getClient();

    expectedException.expect(BazaarRuntimeException.class);
    expectedException.expectMessage("Method call should not happen from the main thread.");
    LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadSync();
  }

  @Test
  public void submitSyncNoErrors_Should_ReturnImmediateWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);
    final ReviewSubmissionResponse reviewResponse = reviewResponseFuture.get();

    Robolectric.flushBackgroundThreadScheduler();

    assertNotNull(reviewResponse);
  }

  @Test
  public void submitSyncBvError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Request has errors", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSyncBvFormError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Request has form errors", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSubmitSyncJsonParseError_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Unable to parse JSON", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  @Test
  public void submitSyncConnectTimeout_Should_ThrowException() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    final BVConversationsClient client = clientRule.getClient();

    final Callable<ReviewSubmissionResponse> reviewResponseCallable = new Callable<ReviewSubmissionResponse>() {
      @Override
      public ReviewSubmissionResponse call() throws Exception {
        return client.prepareCall(request).loadSync();
      }
    };
    final Future<ReviewSubmissionResponse> reviewResponseFuture = clientRule.submit(reviewResponseCallable);

    try {
      reviewResponseFuture.get();
      fail("Should not succeed");
    } catch (ExecutionException e) {
      BazaarException bazaarException = (BazaarException) e.getCause();
      assertNotNull(bazaarException);
      assertEquals("Execution of call failed", bazaarException.getMessage());
    }
    Robolectric.flushBackgroundThreadScheduler();
  }

  // endregion

  // region preview submission async

  @Test
  public void previewAsyncNoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertNotNull(response);
        assertFalse(response.getHasErrors());
      }

      @Override
      public void onFailure(BazaarException exception) {
        exception.printStackTrace();
        fail("Should not fail");
      }
    });
  }

  @Test
  public void previewAsyncBvError_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Request has errors", exception.getMessage());
      }
    });
  }

  @Test
  public void previewAsyncBvFormError_Should_CallSuccessWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertNotNull(response);
        assertTrue(response.getHasErrors());
        assertEquals(1, response.getFieldErrors().size());
        FieldError fieldError = response.getFieldErrors().get(0);
        assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, fieldError.getErrorCode());
        assertEquals("You must agree to the terms and conditions.", fieldError.getMessage());
        assertEquals("agreedtotermsandconditions", fieldError.getField());
      }

      @Override
      public void onFailure(BazaarException exception) {
        fail("Should not call onFailure");
      }
    });
  }

  @Test
  public void previewAsyncJsonParseError_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Unable to parse JSON", exception.getMessage());
      }
    });
  }

  @Test
  public void previewAsyncConnectTimeout_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Execution of call failed", exception.getMessage());
      }
    });
  }

  @Test
  public void previewAsync_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertNotNull(call.submitCallback);
      }

      @Override
      public void onFailure(BazaarException exception) {
        fail("Should not call onFailure");
      }
    });
    assertNull(call.submitCallback);
  }

  @Test
  public void previewAsync_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Preview);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(call.submitCallback);
      }
    });
    assertNull(call.submitCallback);
  }

  // endregion

  // region submit submission async

  @Test
  public void submitAsyncNoErrors_Should_CallSuccessWithResults() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertNotNull(response);
        assertFalse(response.getHasErrors());
      }

      @Override
      public void onFailure(BazaarException exception) {
        exception.printStackTrace();
        fail("Should not fail");
      }
    });
  }

  @Test
  public void submitAsyncBvError_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bverror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Request has errors", exception.getMessage());
      }
    });
  }

  @Test
  public void submitAsyncBvFormError_Should_CallSuccessWithErrorData() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_bvformerror.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertTrue(response.getHasErrors());
        FieldError fieldError = response.getFieldErrors().get(0);
        assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRES_TRUE, fieldError.getErrorCode());
      }

      @Override
      public void onFailure(BazaarException exception) {
        fail("Should not call onFailure");
      }
    });
  }

  @Test
  public void submitAsyncJsonParseError_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Unable to parse JSON", exception.getMessage());
      }
    });
  }

  @Test
  public void submitAsyncConnectTimeout_Should_CallFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    final BVConversationsClient client = clientRule.getClient();

    client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(exception);
        assertEquals("Execution of call failed", exception.getMessage());
      }
    });
  }

  // endregion

  // region callbacks released

  @Test
  public void submitAsync_Should_FreeCallbackAfterSuccess() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("submit_review_preview_success.json");
    clientRule.enqueueHttp200("submit_review_submit_success.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        assertNotNull(call.submitCallback);
      }

      @Override
      public void onFailure(BazaarException exception) {
        fail("Should not call onFailure");
      }
    });
    assertNull(call.submitCallback);
  }

  @Test
  public void submitAsync_Should_FreeCallbackAfterFailure() throws Exception {
    final ReviewSubmissionRequest request = getStubSubmissionRequest(Action.Submit);
    clientRule.enqueueHttp200("bad_json_response.json");
    final BVConversationsClient client = clientRule.getClient();

    final LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> call = client.prepareCall(request);
    call.loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        fail("Should not call onSuccess");
      }

      @Override
      public void onFailure(BazaarException exception) {
        assertNotNull(call.submitCallback);
      }
    });
    assertNull(call.submitCallback);
  }

  // endregion
}
