package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import okhttp3.Request;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormInputTypeTest {
  private BasicRequestFactory requestFactory;
  private RequestFactoryUtils requestFactoryUtils;

  @Before
  public void setUp() throws Exception {
    requestFactory = RequestFactoryUtils.createTestRequestFactory();
    requestFactoryUtils = new RequestFactoryUtils(requestFactory);
  }

  @Test
  public void reviewSubmissionShouldHaveSelectOption() throws Exception {
    FormField formField = mock(FormField.class);
    when(formField.getId()).thenReturn("some_config_option");
    when(formField.getFormInputType()).thenReturn(FormInputType.SELECT);
    FormFieldOption formFieldOption = mock(FormFieldOption.class);
    when(formFieldOption.getValue()).thenReturn("option_3");
    when(formField.getFormFieldOptions()).thenReturn(Collections.singletonList(formFieldOption));
    final Request okRequest = createSelectOptionReviewSubmissionRequest("some_config_option", "option_3");

    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "some_config_option", "option_3");
  }

  // region Stub Data
  private Request createSelectOptionReviewSubmissionRequest(String key, String value) {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod1")
        .addCustomSubmissionParameter(key, value)
        .build();
    return requestFactory.create(request);
  }

  // endregion
}
