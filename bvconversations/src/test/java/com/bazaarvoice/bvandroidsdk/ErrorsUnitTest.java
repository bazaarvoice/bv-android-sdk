package com.bazaarvoice.bvandroidsdk;

import com.google.gson.GsonBuilder;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ErrorsUnitTest {
  private ConversationsResponse getMockConversationsErrors() throws Exception {
    return BVBaseTest.parseJsonResourceFile("conversations_errors.json", ReviewResponse.class, new GsonBuilder().setPrettyPrinting().create());
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
    assertEquals("YOLO_LYFE", firstError.getCode());
    assertEquals("So borked, good luck", firstError.getMessage());
    assertEquals(ErrorCode.ERROR_UNKNOWN, firstError.getErrorCode());
  }
}
