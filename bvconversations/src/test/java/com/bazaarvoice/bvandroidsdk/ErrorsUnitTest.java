package com.bazaarvoice.bvandroidsdk;

import com.google.gson.GsonBuilder;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ErrorsUnitTest {
  private ConversationsResponse getMockConversationsErrors() throws Exception {
    return BVBaseTest.parseJsonResourceFile("conversations_errors.json", ReviewResponse.class, new GsonBuilder().setPrettyPrinting().create());
  }

  private ReviewHighlightsResponse getMockReviewHighlightsErrors() throws Exception {
    return BVBaseTest.parseJsonResourceFile("review_highlights_errors.json", ReviewHighlightsResponse.class, new GsonBuilder().setPrettyPrinting().create());
  }

  @Test
  public void testErrorReviewHighlightsResponse() throws Exception{
    ReviewHighlightsResponse errorResponse = getMockReviewHighlightsErrors();
    assertTrue(errorResponse.getHasErrors());
    Error firstError = errorResponse.getErrors().get(0);
    assertEquals("NotAvailable", firstError.getMessage());
  }

  @Test
  public void testErrorResponse() throws Exception {
    ConversationsResponse errorResponse = getMockConversationsErrors();
    assertTrue(errorResponse.getHasErrors());
    assertEquals(errorResponse.getErrors().size(), 2);
  }

  @Test
  public void testErrorResponseNoAPIKey() throws Exception {
    ConversationsResponse errorResponse = getMockConversationsErrors();
    Error firstError = errorResponse.getErrors().get(0);
    assertEquals("ERROR_PARAM_INVALID_API_KEY", firstError.getCode());
    assertEquals("The passKey provided is invalid.", firstError.getMessage());
    assertEquals(ErrorCode.ERROR_PARAM_INVALID_API_KEY, firstError.getErrorCode());
  }

  @Test
  public void testErrorResponseUnknownError() throws Exception {
    ConversationsResponse errorResponse = getMockConversationsErrors();
    Error firstError = errorResponse.getErrors().get(1);
    assertEquals("ERROR_400", firstError.getCode());
    assertEquals("Error in request", firstError.getMessage());
    assertEquals(ErrorCode.ERROR_UNKNOWN, firstError.getErrorCode());
  }

  @Test
  public void testErrorListIsNull() {
    ConversationsException exception = new ConversationsException("Has Errors", null);
    String errorMsg = exception.getErrorListMessages();
    assertEquals("Conversations Exception\n", errorMsg);
  }
}
