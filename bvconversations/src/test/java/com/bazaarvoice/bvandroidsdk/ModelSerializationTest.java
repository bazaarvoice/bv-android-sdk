package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ModelSerializationTest {


    Gson gson;

    @Before
    public void setup() {
        gson = new Gson();
    }

    @Test
    public void shouldSerializeReviewSubmissionResponse() throws Exception {
        String data = BVBaseTest.jsonResourceFileAsString("submit_review_submit_success.json", this.getClass());
        ReviewSubmissionResponse submissionResponse = gson.fromJson(data, ReviewSubmissionResponse.class);
        assertNotNull(submissionResponse);
        assertNotNull(submissionResponse.getSubmissionId());
    }

    @Test
    public void shouldSerializeCommentsSubmissionResponse() throws Exception {
        String data = BVBaseTest.jsonResourceFileAsString("comment_submission_response.json", this.getClass());
        CommentSubmissionResponse submissionResponse = gson.fromJson(data, CommentSubmissionResponse.class);
        assertNotNull(submissionResponse);
        assertNotNull(submissionResponse.getSubmissionId());
    }

    @Test
    public void shouldSerializeQuestionsSubmissionResponse() throws Exception {
        String data = BVBaseTest.jsonResourceFileAsString("question_submission_response.json", this.getClass());
        QuestionSubmissionResponse submissionResponse = gson.fromJson(data, QuestionSubmissionResponse.class);
        assertNotNull(submissionResponse);
        assertNotNull(submissionResponse.getSubmissionId());
    }

    @Test
    public void shouldSerializeAnswerSubmissionResponse() throws Exception {
        String data = BVBaseTest.jsonResourceFileAsString("answers_submission_response.json", this.getClass());
        AnswerSubmissionResponse submissionResponse = gson.fromJson(data, AnswerSubmissionResponse.class);
        assertNotNull(submissionResponse);
        assertNotNull(submissionResponse.getSubmissionId());
        assertNotNull(submissionResponse.getAnswer().getSubmissionId());
    }

}
