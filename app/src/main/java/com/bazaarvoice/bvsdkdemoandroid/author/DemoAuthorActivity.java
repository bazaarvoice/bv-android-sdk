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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.carousel.DemoCarouselView;
import com.bazaarvoice.bvsdkdemoandroid.pin.DemoPinCarouselPresenter;
import com.bazaarvoice.bvsdkdemoandroid.pin.DemoPinCarouselPresenterModule;

import javax.inject.Inject;

public class DemoAuthorActivity extends AppCompatActivity {

    private static final String EXTRA_AUTHOR_ID = "extra_author_id";

    @Inject DemoAuthorPresenter authorPresenter;
    @Inject DemoPinCarouselPresenter pinCarouselPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_author_detail);
        DemoAuthorView demoAuthorView = (DemoAuthorView) findViewById(R.id.demo_author_root);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DemoCarouselView demoCarouselView = (DemoCarouselView) findViewById(R.id.pinCarouselContainer);

        String authorId = getIntent().getStringExtra(EXTRA_AUTHOR_ID);

        DaggerDemoAuthorComponent.builder()
                .demoAuthorPresenterModule(new DemoAuthorPresenterModule(demoAuthorView, authorId))
                .demoPinCarouselPresenterModule(new DemoPinCarouselPresenterModule(demoCarouselView))
                .demoAppComponent(DemoApp.getAppComponent(this))
                .build()
                .inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public static void transitionTo(Activity fromActivity, String authorId) {
        Intent intent = new Intent(fromActivity, DemoAuthorActivity.class);
        intent.putExtra(EXTRA_AUTHOR_ID, authorId);
        fromActivity.startActivity(intent);
    }
}
