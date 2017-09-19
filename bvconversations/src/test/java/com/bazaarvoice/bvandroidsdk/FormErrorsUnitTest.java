package com.bazaarvoice.bvandroidsdk;

import com.google.gson.GsonBuilder;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FormErrorsUnitTest {
  private ReviewSubmissionResponse getMockReviewSubmissionResponse() throws Exception {
    return BVBaseTest.parseJsonResourceFile("form_errors.json", ReviewSubmissionResponse.class, new GsonBuilder().setPrettyPrinting().create());
  }

  @Test
  public void testFormErrors() throws Exception {
    ReviewSubmissionResponse errorResponse = getMockReviewSubmissionResponse();
    assertTrue(errorResponse.getHasErrors());
    assertEquals(errorResponse.getFieldErrors().size(), 6);
  }

  @Test
  public void testUnknownFormError() throws Exception {
    ReviewSubmissionResponse errorResponse = getMockReviewSubmissionResponse();
    assertTrue(errorResponse.getHasErrors());
    FieldError unknownFieldError = errorResponse.getFieldErrors().get(5);
    assertEquals(SubmissionErrorCode.ERROR_UNKNOWN, unknownFieldError.getErrorCode());
  }

  @Test
  public void testRatingFormError() throws Exception {
    ReviewSubmissionResponse errorResponse = getMockReviewSubmissionResponse();
    assertTrue(errorResponse.getHasErrors());
    FieldError ratingFieldError = errorResponse.getFieldErrors().get(1);
    assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRED, ratingFieldError.getErrorCode());
    assertEquals("You must enter a value for overall rating.", ratingFieldError.getMessage());
    assertEquals("rating", ratingFieldError.getField());
    assertEquals("ERROR_FORM_REQUIRED", ratingFieldError.getCode());
  }
}
