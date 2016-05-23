/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.conversations.kitchensink;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvandroidsdk.BazaarRequest;
import com.bazaarvoice.bvandroidsdk.DisplayParams;
import com.bazaarvoice.bvandroidsdk.OnBazaarResponse;
import com.bazaarvoice.bvandroidsdk.SubmissionMediaParams;
import com.bazaarvoice.bvandroidsdk.SubmissionParams;
import com.bazaarvoice.bvandroidsdk.types.Action;
import com.bazaarvoice.bvandroidsdk.types.Equality;
import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvandroidsdk.types.IncludeStatsType;
import com.bazaarvoice.bvandroidsdk.types.MediaParamsContentType;
import com.bazaarvoice.bvandroidsdk.types.RequestType;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * TODO: Description Here
 */
public class DemoConversationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public DemoConversationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DemoConversationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemoConversationsFragment newInstance() {
        DemoConversationsFragment fragment = new DemoConversationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_conversations, container, false);

        int[] btnIds = new int[]{R.id.reviewBtn, R.id.storiesBtn, R.id.answerBtn,R.id.categoriesBtn, R.id.commentsBtn,
                R.id.profileBtn, R.id.productBtn,R.id.questionBtn, R.id.cmtStoryBtn, R.id.statsBtn, R.id.reviewSubBtn,
                R.id.cmtSubBtn, R.id.questionSubBtn, R.id.photoSubBtn, R.id.answerSubBtn, R.id.videoSubBtn,
                R.id.storiesSubBtn,R.id.feedbackSubBtn};

        for (int btnId : btnIds) {
            final Button btn = (Button) view.findViewById(btnId);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (readyForDemo()) {
                        handleButtonClicked(btn);
                    }
                }
            } );
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        String conversationsKey = currentConfig.apiKeyConversations;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(conversationsKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_conversations));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }


    private void handleButtonClicked(Button button) {
        switch (button.getId()) {
            case R.id.reviewBtn :
                handleReviews();
               break;
            case R.id.storiesBtn :
                handleStories();
                break;
            case R.id.answerBtn :
                handleAnswers();
                break;
            case R.id.categoriesBtn :
                handleCategories();
                break;
            case R.id.commentsBtn :
                handleComments();
                break;
            case R.id.profileBtn :
                handleProfiles();
                break;
            case R.id.productBtn :
                handleProducts();
                break;
            case R.id.questionBtn :
                handleQuestions();
                break;
            case R.id.cmtStoryBtn :
                handleCommentStory();
                break;
            case R.id.statsBtn :
                handleStats();
                break;
            case R.id.reviewSubBtn :
                handleReviewSub();
                break;
            case R.id.cmtSubBtn :
                handleCommentsSub();
                break;
            case R.id.questionSubBtn :
                handleQuestionSub();
                break;
            case R.id.photoSubBtn :
                handlePhotosSub();
                break;
            case R.id.answerSubBtn :
                handleAnswersSub();
                break;
            case R.id.videoSubBtn :
                handleVideoSub();
                break;
            case R.id.storiesSubBtn :
                handleStoriesSub();
                break;
            case R.id.feedbackSubBtn :
                handleFeedbackSub();
                break;
            default:
                break;
        }
    }

    private OnBazaarResponse listener = new OnBazaarResponse() {
        @Override
        public void onResponse(String url, JSONObject response) {

            String deserializedResponse = "";
            try {
                deserializedResponse = response.toString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DemoMainActivity activity = (DemoMainActivity) getActivity();
            activity.transitionToBVConversationDetail(url, deserializedResponse);
        }

        @Override
        public void onException(String message, Throwable exception) {

        }
    };


    private void handleReviews() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.REVIEWS, null, listener);
    }

    private void handleStories() {
        BazaarRequest request = new BazaarRequest();
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "14181");
        request.sendDisplayRequest(RequestType.STORIES, params, listener);
    }

    private void handleAnswers() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.ANSWERS, null, listener);
    }

    private void handleCategories() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.CATEGORIES, null, listener);
    }

    private void handleComments() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, null, listener);
    }

    private void handleProfiles() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.PROFILES, null, listener);
    }

    private void handleProducts() {
        BazaarRequest request = new BazaarRequest();
        DisplayParams params = new DisplayParams();
        String searchTerm = "Shirt";
        params.setSearch(searchTerm);
        params.addStats(IncludeStatsType.REVIEWS);
        request.sendDisplayRequest(RequestType.PRODUCTS, params, listener);
    }

    private void handleQuestions() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.QUESTIONS, null, listener);
    }

    private void handleCommentStory() {
        BazaarRequest request = new BazaarRequest();
        request.sendDisplayRequest(RequestType.STORY_COMMENTS, null, listener);
    }

    private void handleStats() {
        BazaarRequest request = new BazaarRequest();
        DisplayParams params = new DisplayParams();
        params.addFilter("ProductId", Equality.EQUAL, new String[]{"test1", "test2", "test3"});
        params.addStats(IncludeStatsType.REVIEWS);
        params.addStats(IncludeStatsType.NATIVE_REVIEWS);
        request.sendDisplayRequest(RequestType.STATISTICS, params, listener);
    }

    private void handleReviewSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setProductId(String.valueOf(new Random().nextLong()));
        params.setCategoryId(String.valueOf(new Random().nextLong()));
        params.setUserId("123abcd");
        params.setRating(5);
        params.setTitle("Test Title" + (new Random()).nextLong());
        params.setReviewText("Lorem ipsum dolor sit amet, vim at harum molestie, ne vim posse senserit. Quas animal utroque ei duo. Has ne animal omittam maluisset. Te est dicat scaevola invidunt, eum eu maiongorum scripserit. Eos ei nibh ignota, ex has oratio suscipiantur." + (new Random()).nextLong());
        params.setUserNickname("testnickname");
        params.setAction(Action.SUBMIT);
        params.addPhotoUrl("http://apitestcustomer.ugc.bazaarvoice.com/bvstaging/5555/ps_amazon_s3_3rgg6s4xvev0zhzbnabyneo21/photo.jpg");
        params.addVideoUrl("http://www.youtube.com");

        // Add iovation fingerprint to Bazaarvoice Request
        // String blackbox = getBlackbox(getContext().getApplicationContext());
        // params.setFingerprint(blackbox);

        request.postSubmission(RequestType.REVIEWS, params, listener);
    }

    private void handleCommentsSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setCommentText("This is a review comment");
        params.setReviewId("83964");
        params.setUserId("123abc");
        request.postSubmission(RequestType.REVIEW_COMMENTS, params, listener);
    }

    private void handleQuestionSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setCategoryId("1020");
        params.setLocale("en_US");
        params.setUserId("123abc");
        params.setQuestionSummary("Some question text");
        request.postSubmission(RequestType.QUESTIONS, params, listener);
    }

    private void handlePhotosSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionMediaParams params = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
        params.setUserId("123abc");
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bv_sample_image);
        params.setPhoto(image, "testimage");
        request.postSubmission(RequestType.PHOTOS, params, listener);
    }

    private void handleAnswersSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setQuestionId("6104");
        params.setUserId("123abc");
        params.setQuestionSummary("Some answer text");
        request.postSubmission(RequestType.ANSWERS, params, listener);
    }

    private void handleVideoSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionMediaParams params = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
        params.setUserId("123abc");

        InputStream videoStream = getResources().openRawResource(R.raw.sample_mpeg4);

        try {
            params.setVideo(toByteArray(videoStream), "Test Video");
        }catch(Exception e){
            e.printStackTrace();
        }

        request.postSubmission(RequestType.VIDEOS, params, listener);
    }

    private void handleStoriesSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setTitle("This is the title");
        params.setStoryText("This is a story");
        params.setCategoryId("1020235");
        params.setUserId("123abc");
        request.postSubmission(RequestType.STORIES, params, listener);
    }

    private void handleFeedbackSub() {
        BazaarRequest request = new BazaarRequest();
        SubmissionParams params = new SubmissionParams();
        params.setContentType(FeedbackContentType.REVIEW);
        params.setContentId("83964");
        params.setUserId("123abc");
        params.setFeedbackType(FeedbackType.HELPFULNESS);
        params.setVote(FeedbackVoteType.NEGATIVE);
        request.postSubmission(RequestType.FEEDBACK, params, listener);
    }


    public static byte[] toByteArray(InputStream is) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = is.read();
        while(reads != -1){ baos.write(reads);
            reads = is.read();
        }
        return baos.toByteArray();
    }
}
