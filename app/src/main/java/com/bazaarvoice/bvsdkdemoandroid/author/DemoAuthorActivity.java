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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;

public class DemoAuthorActivity extends AppCompatActivity {

    private static final String EXTRA_AUTHOR_ID = "extra_author_id";

    @Inject DemoAuthorPresenter authorPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_author_detail);
        DemoAuthorView demoAuthorView = (DemoAuthorView) findViewById(R.id.demo_author_root);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String authorId = getIntent().getStringExtra(EXTRA_AUTHOR_ID);

        DaggerDemoAuthorComponent.builder()
                .demoAuthorPresenterModule(new DemoAuthorPresenterModule(demoAuthorView, authorId))
                .demoAppComponent(DemoApp.getAppComponent(this))
                .build()
                .inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public static void transitionTo(Context fromActivity, String authorId) {
        Intent intent = new Intent(fromActivity, DemoAuthorActivity.class);
        intent.putExtra(EXTRA_AUTHOR_ID, authorId);
        fromActivity.startActivity(intent);
    }
}
