/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;

/**
 * Bazaarvoice Provided {@link android.support.v7.widget.RecyclerView} to display {@link RecommendationView} objects
 */
public final class RecommendationsRecyclerView extends BVRecyclerView implements BVRecommendations.BVRecommendationsLoader {

    private static final String TAG = RecommendationsRecyclerView.class.getSimpleName();

    private String productId, categoryId;
    private WeakReference<BVRecommendations.BVRecommendationsCallback> delegateCbRef;

    public RecommendationsRecyclerView(Context context) {
        super(context);
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void loadRecommendations(RecommendationsRequest request, BVRecommendations.BVRecommendationsCallback callback) {
        checkMain();
        updateRecCallInfo(request.getProductId(), request.getCategoryId(), callback);
        BVRecommendations recommendations = new BVRecommendations();
        recommendations.getRecommendedProducts(request, receiverCb);
    }

    private void updateRecCallInfo(final String productId, final String categoryId, final BVRecommendations.BVRecommendationsCallback delegateCb) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.delegateCbRef = new WeakReference<BVRecommendations.BVRecommendationsCallback>(delegateCb);
    }

    private BVRecommendations.BVRecommendationsCallback receiverCb = new BVRecommendations.BVRecommendationsCallback() {
        @Override
        public void onSuccess(List<BVProduct> recommendedProducts) {
            BVRecommendations.BVRecommendationsCallback delegateCb = delegateCbRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbRef.clear();
            delegateCb.onSuccess(recommendedProducts);
            if (recommendedProducts != null) {
                RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.RECYCLERVIEW, productId, categoryId, recommendedProducts.size());
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            BVRecommendations.BVRecommendationsCallback delegateCb = delegateCbRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbRef.clear();
            delegateCb.onFailure(throwable);
        }
    };

    @Override
    public void onViewGroupInteractedWith() {
        if (isNestedScrollingEnabled()) {
            RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.RECYCLERVIEW);
        }
    }

    @Override
    public void onAddedToViewHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.RECYCLERVIEW);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        if (!isNestedScrollingEnabled()) {
            RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.RECYCLERVIEW);
        }
        return super.startNestedScroll(axes);
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
