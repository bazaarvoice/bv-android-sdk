/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;

/**
 * Bazaarvoice Provided {@link android.support.v7.widget.RecyclerView} to display {@link RecommendationView} objects
 */
public final class RecommendationsRecyclerView extends BVRecyclerView implements BVRecommendations.BVRecommendationsLoader, PersonalizationView {

    private static final String TAG = RecommendationsRecyclerView.class.getSimpleName();

    private String productId, categoryId;
    private PageType pageType;
    private ShopperProfile shopperProfile;
    private WeakReference<BVRecommendations.BVRecommendationsCallback> delegateCbRef;

    private BVRecommendations.BVRecommendationsCallback receiverCb = new BVRecommendations.BVRecommendationsCallback() {
        @Override
        public void onSuccess(BVRecommendationsResponse response) {
            shopperProfile = response.getShopperProfile();
            BVRecommendations.BVRecommendationsCallback delegateCb = delegateCbRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbRef.clear();
            delegateCb.onSuccess(response);
            RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.RECYCLERVIEW, productId, categoryId, pageType, shopperProfile);
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
    public void loadRecommendations(RecommendationsRequest recommendationsRequest, BVRecommendations.BVRecommendationsCallback callback) {
        checkMain();
        updateRecCallInfo(recommendationsRequest, callback);
        BVRecommendations recommendations = new BVRecommendations();
        recommendations.getRecommendedProducts(recommendationsRequest, receiverCb);
    }

    private void updateRecCallInfo(final RecommendationsRequest recommendationsRequest, final BVRecommendations.BVRecommendationsCallback delegateCb) {
        this.productId = recommendationsRequest.getProductId();
        this.categoryId = recommendationsRequest.getCategoryId();
        this.pageType = recommendationsRequest.getPageType();
        this.delegateCbRef = new WeakReference<>(delegateCb);
    }

    @Override
    public void onViewGroupInteractedWith() {
        if (isNestedScrollingEnabled()) {
            RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.RECYCLERVIEW, pageType, shopperProfile);
        }
    }

    @Override
    public void onAddedToViewHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.RECYCLERVIEW);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        if (!isNestedScrollingEnabled()) {
            RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.RECYCLERVIEW, pageType, shopperProfile);
        }
        return super.startNestedScroll(axes);
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public ShopperProfile getShopperProfile() {
        return shopperProfile;
    }
}
