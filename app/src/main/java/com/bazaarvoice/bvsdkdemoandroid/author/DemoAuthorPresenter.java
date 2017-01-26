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

package com.bazaarvoice.bvsdkdemoandroid.author;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.Author;
import com.bazaarvoice.bvandroidsdk.AuthorsRequest;
import com.bazaarvoice.bvandroidsdk.AuthorsResponse;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.Badge;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Error;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageRequest;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoAuthorPresenter implements DemoAuthorContract.Presenter {
    private static final String TAG = "DemoAuthorPresenter";

    private DemoAuthorContract.View view;
    private String authorId;
    private BVConversationsClient conversationsClient;
    private PrettyTime prettyTime;
    private DemoConfigUtils demoConfigUtils;

    @Inject
    DemoAuthorPresenter(DemoAuthorContract.View view, @Named("AuthorId") String authorId, BVConversationsClient conversationsClient, PrettyTime prettyTime, DemoConfigUtils demoConfigUtils) {
        this.view = view;
        this.authorId = authorId;
        this.conversationsClient = conversationsClient;
        this.prettyTime = prettyTime;
        this.demoConfigUtils = demoConfigUtils;
    }

    @Override
    public void start() {
        view.showRecentReview(false); // Wait until we have a review to show it

        boolean showPinFeature = demoConfigUtils.getCurrentConfig().hasPin();
        view.showProductsToReview(showPinFeature);

        AuthorsRequest request = new AuthorsRequest.Builder(authorId)
                .addIncludeContent(PDPContentType.Reviews, 1)
                .addReviewSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                .addIncludeStatistics(PDPContentType.Reviews)
                .addReviewSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                .build();
        conversationsClient.prepareCall(request).loadAsync(authorsCb);
    }

    private ConversationsCallback<AuthorsResponse> authorsCb = new ConversationsCallback<AuthorsResponse>() {
        @Override
        public void onSuccess(AuthorsResponse response) {
            if (response.getHasErrors()) {
                for (Error error : response.getErrors()) {
                    Log.e(TAG, error.getMessage());
                }
                return;
            }

            List<Author> authors = response.getResults();
            if (authors.size() > 0) {
                Author author = authors.get(0);
                if (author == null) {
                    return;
                }
                String authorNickname = author.getUserNickname();
                String authorLocation = author.getLocation();
                Map<String, Badge> badgeMap = author.getBadges();
                List<Badge> badges = new ArrayList<>(badgeMap.values());
                Review mostRecentReview = null;
                if (response.getIncludes() != null && response.getIncludes().getReviews() != null && !response.getIncludes().getReviews().isEmpty()) {
                    mostRecentReview = response.getIncludes().getReviews().get(0);
                }

                view.showAuthorNickname(authorNickname);
                view.showAuthorLocation(authorLocation);
                view.showAuthorBadges(badges);

                if (mostRecentReview == null) {
                    view.showRecentReview(false);
                } else {
                    view.showRecentReview(true);
                    view.showRecentReviewBody(mostRecentReview.getReviewText());
                    view.showRecentReviewRating(mostRecentReview.getRating());
                    view.showRecentReviewTimePosted(prettyTime.format(mostRecentReview.getSubmissionDate()));
                    ProductDisplayPageRequest pdpRequest = new ProductDisplayPageRequest.Builder(mostRecentReview.getProductId()).build();
                    conversationsClient.prepareCall(pdpRequest).loadAsync(pdpCb);
                }
            }
        }

        @Override
        public void onFailure(BazaarException exception) {
            exception.printStackTrace();
        }
    };

    private ConversationsCallback<ProductDisplayPageResponse> pdpCb = new ConversationsCallback<ProductDisplayPageResponse>() {
        @Override
        public void onSuccess(ProductDisplayPageResponse response) {
            if (response == null || response.getHasErrors()) {
                view.showRecentReviewImage(false);
                Log.d(TAG, "Failed to get Review Details");
                return;
            }

            List<Product> products = response.getResults();
            if (products.isEmpty()) {
                view.showRecentReviewImage(false);
                Log.d(TAG, "No products found");
                return;
            }

            Product firstProduct = products.get(0);

            if (firstProduct.getDisplayName() != null && !firstProduct.getDisplayImageUrl().isEmpty()) {
                view.showRecentReviewProductName(firstProduct.getDisplayName());
            }

            String displayImageUrl = firstProduct.getDisplayImageUrl();
            if (displayImageUrl == null || displayImageUrl.isEmpty()) {
                view.showRecentReviewImage(false);
                Log.d(TAG, "No image found");
                return;
            }

            view.showRecentReviewImage(displayImageUrl);
        }

        @Override
        public void onFailure(BazaarException exception) {
            Log.e(TAG, "Failed to get Review Details", exception);
            view.showRecentReviewImage(false);
        }
    };

    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }
}
