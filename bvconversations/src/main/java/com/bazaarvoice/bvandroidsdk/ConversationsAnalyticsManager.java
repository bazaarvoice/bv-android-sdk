/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Helper class that wraps {@code AnalyticsManger}, builds and enqueues
 * Conversations specific Analytic events.
 */
public class ConversationsAnalyticsManager {

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
        } else if (conversationResponse instanceof AuthorsResponse) {
            AuthorsResponse authorsResponse = (AuthorsResponse) conversationResponse;
            if (!authorsResponse.getHasErrors()) {
                List<Author> authors = authorsResponse.getResults();
                for (Author author : authors) {
                    sendUsedFeatureAuthorDisplayEvent(author.getId());
                }
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

    public static void sendUsedFeatureUgcFeedbackSubmission(@NonNull SubmitUsedFeatureSchema.SubmitFeature submitFeature, @Nullable String contentId, @Nullable String contentType, @Nullable String feedbackType, boolean hasFingerprint) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        SubmitUsedFeatureSchema schema = new SubmitUsedFeatureSchema.Builder(submitFeature, magpieMobileAppPartialSchema)
                .contentId(contentId)
                .contentType(contentType)
                .detail1(feedbackType)
                .fingerprint(hasFingerprint)
                .build();
        analyticsManager.enqueueEvent(schema);
    }

    public static void sendUsedFeatureAuthorDisplayEvent(String profileId) {
        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        AuthorUsedFeatureSchema schema = new AuthorUsedFeatureSchema(profileId, magpieMobileAppPartialSchema);
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
        if (request instanceof FeedbackSubmissionRequest) {
            FeedbackSubmissionRequest feedbackSubmissionRequest = (FeedbackSubmissionRequest) request;
            String contentId = feedbackSubmissionRequest.getContentId();
            String contentType = feedbackSubmissionRequest.getContentType();
            String feedbackType = feedbackSubmissionRequest.getFeedbackType();
            sendUsedFeatureUgcFeedbackSubmission(SubmitUsedFeatureSchema.SubmitFeature.FEEDBACK, contentId, contentType, feedbackType, hasFingerPrint);
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
