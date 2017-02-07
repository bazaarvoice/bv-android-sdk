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

package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvsdkdemoandroid.ads.DemoAdFragment;
import com.bazaarvoice.bvsdkdemoandroid.carousel.DemoCarouselView;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigModule;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConversationsAPIFragment;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConversationsStoresAPIFragment;
import com.bazaarvoice.bvsdkdemoandroid.conversations.answers.DemoAnswersActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings.DemoBulkRatingsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.productstats.DemoProductStatsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoStoreReviewsActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsFragment;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsPostActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailFragment;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailPagerAdapter;
import com.bazaarvoice.bvsdkdemoandroid.curations.feed.DemoCurationsFeedActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.map.DemoCurationsMapsActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoFancyProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductDetailCurationsAdapter;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductDetailRecAdapter;
import com.bazaarvoice.bvsdkdemoandroid.location.DemoLocationFragment;
import com.bazaarvoice.bvsdkdemoandroid.pin.DemoPinFragment;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoRecommendationsFragment;
import com.bazaarvoice.bvsdkdemoandroid.recshome.DemoRecsAdapter;
import com.bazaarvoice.bvsdkdemoandroid.recshome.DemoRecsHeaderPagerAdapter;
import com.bazaarvoice.bvsdkdemoandroid.recshome.DemoRecsHomeActivity;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoPreferencesFragment;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoPreferencesSelectedFragment;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DemoAppModule.class, DemoConfigModule.class, DemoBvModule.class})
public interface DemoAppComponent {
    BVSDK getBvSdk();
    BVConversationsClient getBvConvClient();
    CurationsImageLoader getCurationsImageLoader();
    PinClient getPinClient();
    PrettyTime getPrettyTime();
    DemoConfigUtils getDemoConfigUtils();

    void inject(DemoAdFragment adsFragment);
    void inject(DemoMainActivity activity);
    void inject(DemoAnswersActivity activity);
    void inject(DemoBulkRatingsActivity activity);
    void inject(DemoConversationsAPIFragment fragment);
    void inject(DemoConversationsStoresAPIFragment fragment);
    void inject(DemoProductStatsActivity activity);
    void inject(DemoQuestionsActivity demoQuestionsActivity);
    void inject(DemoReviewsActivity activity);
    void inject(DemoStoreReviewsActivity activity);
    void inject(DemoCurationsFragment fragment);
    void inject(DemoCurationsPostActivity activity);
    void inject(DemoCurationsDetailActivity activity);
    void inject(DemoCurationsFeedActivity activity);
    void inject(DemoCurationsMapsActivity activity);
    void inject(DemoFancyProductDetailActivity activity);
    void inject(DemoLocationFragment fragment);
    void inject(DemoPinFragment fragment);
    void inject(DemoRecommendationsFragment fragment);
    void inject(DemoRecsHomeActivity activity);
    void inject(DemoPreferencesFragment fragment);
    void inject(DemoPreferencesSelectedFragment fragment);
    void inject(DemoApp app);
    void inject(DemoCurationsDetailFragment fragment);
    void inject(DemoCurationsDetailPagerAdapter adapter);
    void inject(DemoProductDetailRecAdapter adapter);
    void inject(DemoProductDetailCurationsAdapter adapter);
    void inject(DemoRecsAdapter adapter);
    void inject(DemoRecsHeaderPagerAdapter adapter);
    void inject(DemoCarouselView carousel);
}
