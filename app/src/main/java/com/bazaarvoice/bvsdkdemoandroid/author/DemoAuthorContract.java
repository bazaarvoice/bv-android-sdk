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

import com.bazaarvoice.bvandroidsdk.Badge;
import com.bazaarvoice.bvandroidsdk.Pin;
import com.bazaarvoice.bvsdkdemoandroid.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.DemoBaseView;

import java.util.List;

interface DemoAuthorContract {

    interface View extends DemoBaseView<Presenter> {
        void showAuthorNickname(String authorNickname);
        void showAuthorLocation(String authorLocation);
        void showAuthorBadges(List<Badge> badges);
        void showProductsToReview(List<Pin> pins);

        void showRecentReviewImage(String imageUrl);
        void showRecentReviewRating(float rating);
        void showRecentReviewBody(String body);
        void showRecentReviewTimePosted(String timePosted);
    }

    interface Presenter extends DemoBasePresenter {

    }

}
