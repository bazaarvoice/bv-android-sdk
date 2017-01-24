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
import com.bazaarvoice.bvandroidsdk.Pin;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;

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

    @Inject
    DemoAuthorPresenter(DemoAuthorContract.View view, @Named("AuthorId") String authorId, BVConversationsClient conversationsClient, PrettyTime prettyTime) {
        this.view = view;
        this.authorId = authorId;
        this.conversationsClient = conversationsClient;
        this.prettyTime = prettyTime;
    }

    @Override
    public void start() {
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
                    return;
                }
                view.showRecentReviewBody(mostRecentReview.getReviewText());
                // TODO view.showRecentReviewImage(mostRecentReview.getPhotos());
                view.showRecentReviewRating(mostRecentReview.getRating());
                view.showRecentReviewTimePosted(prettyTime.format(mostRecentReview.getSubmissionDate()));
            }
        }

        @Override
        public void onFailure(BazaarException exception) {
            exception.printStackTrace();
        }
    };

    private PinClient.PinsCallback pinCb = new PinClient.PinsCallback() {
        @Override
        public void onSuccess(List<Pin> pins) {
            if (pins == null || pins.isEmpty()) {
                Log.e(TAG, "No PINS available");
                return;
            }

            view.showProductsToReview(pins);
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.e(TAG, "Failed to get PINs", throwable);
        }
    };

    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }
}
