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

package com.bazaarvoice.bvsdkdemoandroid.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCarouselView extends FrameLayout implements DemoCarouselContract.View {

    private DemoCarouselContract.Presenter presenter;
    private DemoCarouselAdapter adapter;
    @BindView(R.id.carouselHeader) TextView carouselHeaderTv;
    @BindView(R.id.carousel)
    RecyclerView carouselRecyclerView;
    @BindView(R.id.carouselEmptyMessage) TextView carouselEmptyTv;
    @BindView(R.id.carouselProgressBar) ProgressBar carouselProgressBar;
    private String carouselHeader = "";
    private boolean carouselItemShowImage = true;
    private boolean carouselItemShowTitle = true;
    private boolean carouselItemShowRating = true;

    @Inject Picasso picasso;

    public DemoCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_demo_carousel, this, true);
        setOptions(context, attrs);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        DemoApp.getAppComponent(getContext()).inject(this);
        adapter = new DemoCarouselAdapter(
                carouselItemShowImage,
                carouselItemShowTitle,
                carouselItemShowRating,
                picasso);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        // Setup Title
        carouselHeaderTv.setText(carouselHeader);

        // Setup Horizontal Carousel
        carouselRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(layoutManager);
    }

    private void setOptions(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DemoCarouselView,
                0, 0);

        try {
            carouselHeader = a.getString(R.styleable.DemoCarouselView_carouselHeader);
            carouselItemShowImage = a.getBoolean(R.styleable.DemoCarouselView_carouselItemShowImage, true);
            carouselItemShowTitle = a.getBoolean(R.styleable.DemoCarouselView_carouselItemShowTitle, true);
            carouselItemShowRating = a.getBoolean(R.styleable.DemoCarouselView_carouselItemShowRating, true);
        } finally {
            a.recycle();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ProductType extends BVDisplayableProductContent> void updateContent(List<ProductType> contentList) {
        adapter.updateContent(contentList);
    }

    @Override
    public void setPresenter(DemoCarouselContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEmpty(boolean showing) {
        carouselEmptyTv.setVisibility(showing ? VISIBLE : GONE);
    }

    @Override
    public void showEmptyMessage(String message) {
        carouselEmptyTv.setText(message);
    }

    @Override
    public void showLoading(boolean showing) {
        carouselProgressBar.setVisibility(showing ? VISIBLE : GONE);
    }

    @Override
    public void setOnItemClickListener(DemoCarouselContract.OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void showProductTapped(String name) {
        final Snackbar snackbar = Snackbar.make(this, "Tapped " + name, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.okay), new OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }
        presenter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
