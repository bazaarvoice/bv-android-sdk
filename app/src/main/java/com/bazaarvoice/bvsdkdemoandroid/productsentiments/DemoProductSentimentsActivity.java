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

package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

public class DemoProductSentimentsActivity extends AppCompatActivity {

    private static final String EXTRA_ProductSentiments_ID = "extra_productsentiments_id";

    @Inject
    DemoProductSentimentsPresenter ProductSentimentsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_productsentiments_detail);
        DemoProductSentimentsView demoProductSentimentsView = (DemoProductSentimentsView) findViewById(R.id.demo_ProductSentiments_root);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String ProductSentimentsId = getIntent().getStringExtra(EXTRA_ProductSentiments_ID);

        DaggerDemoProductSentimentsComponent.builder()
                .demoProductSentimentsPresenterModule(new DemoProductSentimentsPresenterModule(demoProductSentimentsView, ProductSentimentsId))
                .demoAppComponent(DemoApp.getAppComponent(this))
                .build()
                .inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public static void transitionTo(Context fromActivity, String ProductSentimentsId) {
        Intent intent = new Intent(fromActivity, DemoProductSentimentsActivity.class);
        intent.putExtra(EXTRA_ProductSentiments_ID, ProductSentimentsId);
        fromActivity.startActivity(intent);
    }
}
