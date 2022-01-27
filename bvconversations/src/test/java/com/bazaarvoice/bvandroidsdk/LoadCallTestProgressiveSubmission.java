package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class LoadCallTestProgressiveSubmission extends LoadCallTest {

    private BVConversationsClient client;
    @Rule
    public LoadCallTest.BVConversationsClientRule clientRule = new LoadCallTest.BVConversationsClientRule();
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected void modifyPropertiesToInitSDK() {
        super.modifyPropertiesToInitSDK();
        when(bvAuthenticatedUser.getUserAuthString()).thenReturn(UUID.randomUUID().toString());
        bvAuthenticatedUser.getUserAuthString();
    }

    private ProgressiveSubmitRequest getStubProgressiveSubmissionRequest() {
        Map<String, Object> submissionFields = new HashMap<>();
        submissionFields.put("rating", 4);
        submissionFields.put("agreedtotermsandconditions", true);
        return new ProgressiveSubmitRequest.Builder("product1","submissionSessionToken", "en_US")
                .submissionFields(submissionFields)
                .build();
    }

    private InitiateSubmitRequest getStubInitiateProgressiveSessionFormRequest() {
        List<String> productIds = new ArrayList<>();
        productIds.add("product1");

        return new InitiateSubmitRequest
                .Builder(productIds, "locale")
                .extended(true)
                .authenticationProvider(new BVHostedAuthenticationProvider("uas"))
                .build();
    }

    private PhotoUploadRequest getStubPhotoUploadRequest() {

        File file = readFile("bv_sample_image.png", this.getClass());
        return new PhotoUploadRequest.Builder(new PhotoUpload(file, "comments", PhotoUpload.ContentType.REVIEW))
                .build();
    }

    @Test
    public void shouldCallFailureWithConversationsExceptionWhenHasErrorsIsTrue() throws Exception {
        final InitiateSubmitRequest request = getStubInitiateProgressiveSessionFormRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_initiate_submit_response_with_errors.json");
        LoadCallProgressiveSubmission loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback() {

            @Override
            public void onSuccess(@NonNull ConversationsResponse response) {

            }

            @Override
            public void onFailure(ConversationsSubmissionException exception) {
                assertNotNull(exception);
                assertEquals(exception.getMessage(), "Request has errors");
            }
        });
    }

    @Test
    public void shouldCallSuccessAndReturnParsedDataForInitiateSubmitRequest() throws Exception {
        final InitiateSubmitRequest request = getStubInitiateProgressiveSessionFormRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_initiate_submit_response.json");
        LoadCallProgressiveSubmission loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<InitiateSubmitResponse>() {
            @Override
            public void onSuccess(InitiateSubmitResponse response) {
                assertNotNull(response);
                assertFalse(response.getHasErrors());
                assertNotNull(response.getData());
                assertNotNull(response.getData().getProductFormData());
                assertNotNull(response.getData().getProductFormData().get("product4").getSubmissionSessionToken());
                Review review = response.getData().getProductFormData().get("product4").getReview();
                assertNotNull(review);
                assertEquals("product4", review.getProductId());
                assertEquals("123456789020abc", review.getSubmissionId());
                assertTrue(response.getData().getProductFormData().containsKey("product4"));
                Map<String, FormField> fields = response.getData().getProductFormData().get("product4").getFields();
                assertEquals(FormInputType.TEXT, fields.get("FreeTextQuestion").getFormInputType());
                assertEquals(FormInputType.CHOICE, fields.get("UsageFrequency1").getFormInputType());
                assertEquals(FormInputType.URL, fields.get("photourl_1").getFormInputType());
                assertEquals(FormInputType.BOOLEAN, fields.get("isrecommended").getFormInputType());
                assertEquals(FormInputType.INTEGER, fields.get("Slider").getFormInputType());
                assertEquals(response.getData().getProductFormData().get("product4").getFormFields().size(), 27);
                assertEquals(response.getData().getUserId(),"zt9veeogfppnyelf2imdtvup2u");

            }

            @Override
            public void onFailure(@NonNull ConversationsSubmissionException exception) {
                fail();
            }
        });
    }

    @Test
    public void shouldCallSuccessAndReturnParsedDataForInitiateSubmitRequestWithExtendedResponse() throws Exception {
        final InitiateSubmitRequest request = getStubInitiateProgressiveSessionFormRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_initiate_submit_extended_response.json");
        LoadCallProgressiveSubmission loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<InitiateSubmitResponse>() {
            @Override
            public void onSuccess(InitiateSubmitResponse response) {
                assertNotNull(response);
                assertFalse(response.getHasErrors());
                assertNotNull(response.getData());
                assertNotNull(response.getData().getProductFormData());
                assertNotNull(response.getData().getProductFormData().get("product4").getSubmissionSessionToken());
                assertTrue(response.getData().getProductFormData().containsKey("product4"));
                assertEquals(26, response.getData().getProductFormData().get("product4").getFormFields().size());
                List<FormField> formFieldMap = response.getData().getProductFormData().get("product4").getFormFields();
                assertNotNull(formFieldMap);
                assertNotNull(formFieldMap.get(0));
            }

            @Override
            public void onFailure(@NonNull ConversationsSubmissionException exception) {
                fail();
            }
        });
    }

    @Test
    public void shouldCallSuccessAndReturnParsedDataForProgressiveSubmitRequest() throws Exception {
        final ProgressiveSubmitRequest request = getStubProgressiveSubmissionRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_review_submission_response.json");
        LoadCallProgressiveSubmission loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<ProgressiveSubmitResponse>() {
            @Override
            public void onSuccess(ProgressiveSubmitResponse response) {
                assertNotNull(response);
                assertFalse(response.getHasErrors());
                assertNotNull(response.getData().getReview());
                assertEquals("1234567890abcdef",response.getData().getSubmissionSessionToken());
                assertEquals("123456abcd", response.getData().getSubmissionId());
                assertFalse(response.getData().isFormComplete());
            }

            @Override
            public void onFailure(ConversationsSubmissionException exception) {
                fail();
            }
        });
    }

    @Test
    public void shouldCallFailAndReturnParsedDataForProgressiveSubmitRequestWithFormErrors() throws Exception {
        final ProgressiveSubmitRequest request = getStubProgressiveSubmissionRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_review_submission_response_with_errors.json");
        LoadCallProgressiveSubmission<ProgressiveSubmitRequest, ProgressiveSubmitResponse> loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<ProgressiveSubmitResponse>() {
            @Override
            public void onSuccess(ProgressiveSubmitResponse response) {
                fail();
            }

            @Override
            public void onFailure(ConversationsSubmissionException exception) {
                assertNotNull(exception);
                assertNotNull(exception.getErrors());
                assertNotNull(exception.getFieldErrors());
                assertEquals(2, exception.getFieldErrors().size());
                assertEquals("rating", exception.getFieldErrors().get(0).getField());
                assertEquals("ERROR_FORM_INVALID", exception.getFieldErrors().get(0).getCode());
                assertEquals(SubmissionErrorCode.ERROR_FORM_INVALID, exception.getFieldErrors().get(0).getErrorCode());
                assertEquals("The rating value is out of the configured range", exception.getFieldErrors().get(0).getMessage());
                assertEquals("agreedtotermsandconditions", exception.getFieldErrors().get(1).getField());
                assertEquals("ERROR_FORM_REQUIRED", exception.getFieldErrors().get(1).getCode());
                assertEquals(SubmissionErrorCode.ERROR_FORM_REQUIRED, exception.getFieldErrors().get(1).getErrorCode());
                assertEquals("required", exception.getFieldErrors().get(1).getMessage());
            }
        });
    }

    @Test
    public void shouldCallFailAndReturnParsedDataForProgressiveSubmitRequestHostedAuthWithFormErrors() throws Exception {
        final ProgressiveSubmitRequest request = getStubProgressiveSubmissionRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("progressive_submission_review_submission_response_hostedauth_with_errors.json");
        LoadCallProgressiveSubmission<ProgressiveSubmitRequest, ProgressiveSubmitResponse> loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<ProgressiveSubmitResponse>() {
            @Override
            public void onSuccess(ProgressiveSubmitResponse response) {
                fail();
            }

            @Override
            public void onFailure(ConversationsSubmissionException exception) {
                assertNotNull(exception);
                assertNotNull(exception.getErrors());
                assertEquals("Supplied Submission session token does not match the Review and Subject", exception.getErrors().get(0).getMessage());

            }
        });
    }

    @Test
    public void shouldCompletePhotoUploadRequestSucessfully() throws Exception {
        final PhotoUploadRequest request = getStubPhotoUploadRequest();
        final BVConversationsClient client = clientRule.getClient();
        clientRule.enqueueHttp200("photo_upload_response.json");
        LoadCallProgressiveSubmission<PhotoUploadRequest, PhotoUploadResponse> loadCallProgressiveSubmission = client.prepareCall(request);

        loadCallProgressiveSubmission.loadAsync(new ConversationsSubmissionCallback<PhotoUploadResponse>() {
            @Override
            public void onSuccess(@NonNull PhotoUploadResponse response) {
                assertNotNull(response);
                assertNotNull(response.getPhoto());
                assertNotNull(response.getPhoto().getContent());
                assertNotNull(response.getPhoto().getContent().getNormalUrl());
                assertNotNull(response.getPhoto().getContent().getThumbnailUrl());
                assertEquals("normalurl",response.getPhoto().getContent().getNormalUrl());
                assertEquals("thumbnailurl",response.getPhoto().getContent().getThumbnailUrl());
            }

            @Override
            public void onFailure(@NonNull ConversationsSubmissionException exception) {
                fail();
            }
        });
    }

}
