/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.types.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * wrapper around {@code AnalyticsManger}
 * for sending Conversations analytics events
 */
public class ConversationsAnalyticsManager {

    // *** v1 ConversationsAnalyticsManager - DEPRECATED PLEASE USE v2 below ***

    @Deprecated
    public static void sendDisplayAnalyticsEvent(RequestType requestType, JSONObject response) {
        Set<String> productIdSet = new HashSet<>();

        try {
            JSONArray results = response.getJSONArray("Results");

            if (results == null || results.length() == 0) {
                return;
            }

            for (int i=0; i<results.length(); i++) {
                JSONObject resultJsonObject = results.getJSONObject(i);
                Map<String, Object> resultInfo = getResultInfo(requestType, resultJsonObject);
                if (resultInfo == null) {
                    return;
                }

                sendUgcImpressionEvent(resultInfo);

                String productId = (String) resultInfo.get("productId");
                String categoryId = (String) resultInfo.get("categoryId");
                String brand = (String) resultInfo.get("brand");
                if (!productIdSet.contains(productId)) {
                    productIdSet.add(productId);
                    sendProductPageViewEvent(productId, categoryId, brand);
                }
            }

        } catch (Exception e) {
            BazaarException bazaarException = new BazaarException("Error sending display event", e);
            bazaarException.printStackTrace();
        }
    }

    @Deprecated
    public static void sendSubmissionAnalyticsEvent(RequestType requestType, BazaarParams bazaarParams) {
        SubmitUsedFeatureSchema.SubmitFeature submitFeature = getSubmitFeatureFromRequestType(requestType);

        if (submitFeature == null) {
            return;
        }

        boolean isFingerprintUsed = false;
        String productId = null;
        String categoryId = null;
        if (bazaarParams != null) {
            if (bazaarParams instanceof SubmissionParams) {
                SubmissionParams submissionParams = (SubmissionParams) bazaarParams;
                String fingerprint = submissionParams.getFingerprint();
                isFingerprintUsed = fingerprint != null && !fingerprint.isEmpty();

                productId = submissionParams.getProductId();
                categoryId = submissionParams.getCategoryId();
            }
        }

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        SubmitUsedFeatureSchema schema = new SubmitUsedFeatureSchema.Builder(submitFeature, magpieMobileAppPartialSchema)
                .fingerprint(isFingerprintUsed)
                .build();

        schema.addKeyVal("productId", productId);
        schema.addKeyVal("categoryId", categoryId);

        analyticsManager.enqueueEvent(schema);
    }

    @Deprecated
    private static SubmitUsedFeatureSchema.SubmitFeature getSubmitFeatureFromRequestType(RequestType requestType) {
        SubmitUsedFeatureSchema.SubmitFeature submitFeature = null;

        switch (requestType) {
            case ANSWERS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.ANSWER;
                break;
            case REVIEW_COMMENTS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.COMMENT;
                break;
            case STORY_COMMENTS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.STORY_COMMENT;
                break;
            case FEEDBACK:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.FEEDBACK;
                break;
            case QUESTIONS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.ASK;
                break;
            case REVIEWS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.WRITE;
                break;
            case STORIES:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.STORY;
                break;
        }

        return submitFeature;
    }

    @Deprecated
    private static Map<String, Object> getResultInfo(RequestType requestType, JSONObject response) throws JSONException {
        Map<String, Object> eventMap = new HashMap<String, Object>();

        switch (requestType) {
            case REVIEWS:
                eventMap.put("contentType", "Review");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("visible", false);
                eventMap.put("productId", response.getString("ProductId"));
                break;
            case QUESTIONS:
                eventMap.put("contentType", "IncludeBase");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("ProductId"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case PRODUCTS:
                eventMap.put("contentType", "Product");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("Id"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                JSONObject brandJsonObject = response.getJSONObject("Brand");
                if (brandJsonObject != null) {
                    String brand = brandJsonObject.getString("Name");
                    if (brand != null) {
                        eventMap.put("brand", brand);
                    }
                }
                break;
            case STATISTICS:
                eventMap.put("contentType", "Statistic");
                eventMap.put("contentId", "");
                eventMap.put("productId", response.getJSONObject("Statistics").getString("ProductId"));
                break;
            case STORY_COMMENTS:
                eventMap.put("contentType", "Comment");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case CATEGORIES:
                eventMap.put("contentType", "Category");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case STORIES:
                eventMap.put("contentType", "Story");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case ANSWERS:
                eventMap.put("contentType", "Answer");
                eventMap.put("contentId", response.getString("Id"));
                break;
            default:
                break;
        }

        return eventMap;
    }

    @Deprecated
    private static void sendUgcImpressionEvent(Map<String, Object> dataMap) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        String productId = (String) dataMap.get("productId");
        String bvProduct = (String) dataMap.get("bvProduct");
        UgcImpressionSchema schema = new UgcImpressionSchema(magpieMobileAppPartialSchema, productId, bvProduct);
        analyticsManager.enqueueEvent(schema);
    }


    @Deprecated
    private static void sendProductPageViewEvent(String productId, String categoryId, String brand) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        ProductPageViewSchema schema = new ProductPageViewSchema(productId, categoryId, brand, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema);
    }


    // *** v2 ConversationsAnalyticsManager ***

    public static void sendUsedFeatureInViewEvent(String productId, MagpieBvProduct magpieBvProduct) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        ConversationSchemas.InViewUsedFeatureSchema schema = new ConversationSchemas.InViewUsedFeatureSchema(productId, magpieBvProduct, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema);
    }

    public static void sendUgcImpressionEvent(String productId, String contentId, ConversationSchemas.AnalyticsContentType analyticsContentType, MagpieBvProduct magpieBvProduct) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        ConversationSchemas.UgcImpressionSchema schema = new ConversationSchemas.UgcImpressionSchema(magpieMobileAppPartialSchema, productId, contentId, analyticsContentType, magpieBvProduct);
        analyticsManager.enqueueEvent(schema);
    }

    public static void sendUgcImpressionEventReviews(List<Review> reviews) {
        for (Review review : reviews) {
            String productId = "", contentId = "";
            if (review != null) {
                productId = review.getProductId();
                contentId = review.getId();
            }
            ConversationsAnalyticsManager.sendUgcImpressionEvent(productId, contentId, ConversationSchemas.AnalyticsContentType.Review, MagpieBvProduct.RATINGS_AND_REVIEWS);
        }
    }

    public static void sendUgcImpressionEventStoreReviews(List<StoreReview> reviews) {
        for (StoreReview review : reviews) {
            String productId = "", contentId = "";
            if (review != null) {
                productId = review.getProductId();
                contentId = review.getId();
            }
            ConversationsAnalyticsManager.sendUgcImpressionEvent(productId, contentId, ConversationSchemas.AnalyticsContentType.StoreReview, MagpieBvProduct.RATINGS_AND_REVIEWS);
        }
    }

    public static void sendUgcImpressionEventStores(List<Store> stores) {
        for (Store store : stores) {
            String productId = "";
            if (store != null) {
                productId = store.getId();
            }
            ConversationsAnalyticsManager.sendUgcImpressionEvent(productId, "", ConversationSchemas.AnalyticsContentType.Store, MagpieBvProduct.RATINGS_AND_REVIEWS);
        }
    }

    public static void sendUgcImpressionEventQAndA(List<Question> questions) {
        for (Question question : questions) {
            String productId = "", questionId = "";
            if (question != null) {
                productId = question.getProductId();
                questionId = question.getId();

                List<Answer> answers = question.getAnswers();
                if (answers != null) {
                    for (Answer answer : answers) {
                        String answerId = "";
                        if (answer != null) {
                            answerId = answer.getId();
                        }
                        ConversationsAnalyticsManager.sendUgcImpressionEvent(productId, answerId, ConversationSchemas.AnalyticsContentType.Answer, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
                    }
                }
            }
            ConversationsAnalyticsManager.sendUgcImpressionEvent(productId, questionId, ConversationSchemas.AnalyticsContentType.Question, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
        }
    }

    public static void sendUsedFeatureScrolledEvent(String productId, MagpieBvProduct magpieBvProduct) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        ConversationSchemas.ScrolledUsedFeatureSchema schema = new ConversationSchemas.ScrolledUsedFeatureSchema(productId, magpieBvProduct, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema);
    }

    public static void sendProductPageView(@NonNull MagpieBvProduct magpieBvProduct, @Nullable Product product) {
        if (product != null) {
            int numQuestions = product.getQaStatistics() != null ? product.getQaStatistics().getTotalQuestionCount() : 0;
            int numAnswers = product.getQaStatistics() != null ? product.getQaStatistics().getTotalAnswerCount() : 0;
            int numReviews = product.getReviewStatistics() != null ? product.getReviewStatistics().getTotalReviewCount() : 0;
            String brand = product.getBrandExternalId();
            String categoryId = product.getCategoryId();

            AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
            MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
            ProductPageViewSchema schema = new ProductPageViewSchema.Builder(product.getId(), magpieMobileAppPartialSchema)
                    .numQuestions(numQuestions)
                    .numAnswers(numAnswers)
                    .numReviews(numReviews)
                    .brand(brand)
                    .categoryId(categoryId)
                    .magpieBvProduct(magpieBvProduct)
                    .build();
            analyticsManager.enqueueEvent(schema);
        }
    }

    static void sendSuccessfulConversationsDisplayResponse(ConversationsResponse conversationResponse) {
        if (conversationResponse instanceof ReviewResponse) {
            ReviewResponse reviewResponse = (ReviewResponse) conversationResponse;
            ConversationsAnalyticsManager.sendUgcImpressionEventReviews(reviewResponse.getResults());
        }
        else if (conversationResponse instanceof StoreReviewResponse) {
            StoreReviewResponse storeReviewResponse = (StoreReviewResponse) conversationResponse;
            ConversationsAnalyticsManager.sendUgcImpressionEventStoreReviews(storeReviewResponse.getResults());
        }
        else if (conversationResponse instanceof BulkStoreResponse) {
            BulkStoreResponse bulkStoresResponse = (BulkStoreResponse) conversationResponse;
            ConversationsAnalyticsManager.sendUgcImpressionEventStores(bulkStoresResponse.getResults());
        }
        else if (conversationResponse instanceof QuestionAndAnswerResponse) {
            QuestionAndAnswerResponse questionAndAnswerResponse = (QuestionAndAnswerResponse) conversationResponse;
            ConversationsAnalyticsManager.sendUgcImpressionEventQAndA(questionAndAnswerResponse.getResults());
        }
        else if (conversationResponse instanceof ProductDisplayPageResponse) {
            ProductDisplayPageResponse productDisplayPageResponse = (ProductDisplayPageResponse) conversationResponse;
            List<Product> products = productDisplayPageResponse.getResults();
            if (products == null || products.isEmpty()) {
                return;
            }
            Product product = products.get(0);
            sendProductPageView(MagpieBvProduct.RATINGS_AND_REVIEWS, product);
        }
        else if (conversationResponse instanceof ReviewResponse) {
            ReviewResponse reviewResponse = (ReviewResponse) conversationResponse;
            List<Review> reviews = reviewResponse.getResults();
            if (reviews == null || reviews.isEmpty()) {
                return;
            }
            Review review = reviews.get(0);
            if (review != null) {
                Product product = review.getProduct();
                sendProductPageView(MagpieBvProduct.RATINGS_AND_REVIEWS, product);
            }
        }
        else if (conversationResponse instanceof QuestionAndAnswerResponse) {
            QuestionAndAnswerResponse questionAndAnswerResponse = (QuestionAndAnswerResponse) conversationResponse;
            List<Question> questions = questionAndAnswerResponse.getResults();
            if (questions == null || questions.isEmpty()) {
                return;
            }
            Question question = questions.get(0);
            if (question != null) {
                Product product = question.getProduct();
                sendProductPageView(MagpieBvProduct.QUESTIONS_AND_ANSWERS, product);
            }
        }
    }

    public static void sendUsedFeatureUgcContentSubmission(@NonNull SubmitUsedFeatureSchema.SubmitFeature submitFeature, @Nullable String productId, boolean hasFingerprint) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        SubmitUsedFeatureSchema schema = new SubmitUsedFeatureSchema.Builder(submitFeature, magpieMobileAppPartialSchema)
                .productId(productId)
                .fingerprint(hasFingerprint)
                .build();
        analyticsManager.enqueueEvent(schema);
    }

    static void sendSuccessfulConversationsSubmitResponse(ConversationsSubmissionRequest request) {
        boolean hasFingerPrint = request.getFingerprint() != null && !request.getFingerprint().isEmpty();

        if (request instanceof QuestionSubmissionRequest) {
            QuestionSubmissionRequest questionSubmissionRequest = (QuestionSubmissionRequest) request;
            String productId = questionSubmissionRequest.getProductId();
            sendUsedFeatureUgcContentSubmission(SubmitUsedFeatureSchema.SubmitFeature.ASK, productId, hasFingerPrint);
        }
        if (request instanceof AnswerSubmissionRequest) {
            AnswerSubmissionRequest answerSubmissionRequest = (AnswerSubmissionRequest) request;
            String productId = answerSubmissionRequest.getProductId();
            sendUsedFeatureUgcContentSubmission(SubmitUsedFeatureSchema.SubmitFeature.ANSWER, productId, hasFingerPrint);
        }
        if (request instanceof ReviewSubmissionRequest) {
            ReviewSubmissionRequest reviewSubmissionRequest = (ReviewSubmissionRequest) request;
            String productId = reviewSubmissionRequest.getProductId();
            sendUsedFeatureUgcContentSubmission(SubmitUsedFeatureSchema.SubmitFeature.WRITE, productId, hasFingerPrint);
        }
    }

    static void sendSuccessfulConversationsPhotoUpload(PhotoUpload request) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        SubmitUsedFeatureSchema schema = new SubmitUsedFeatureSchema.Builder(SubmitUsedFeatureSchema.SubmitFeature.PHOTO, magpieMobileAppPartialSchema)
                .build();
        analyticsManager.enqueueEvent(schema);
    }

}
