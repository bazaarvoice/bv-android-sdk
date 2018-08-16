/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Bazaarvoice provided {@link FrameLayout} to contain a single Bazaarvoice
 * recommended product {@link BVProduct}. This view will allow Bazaarvoice to receive feedback
 * about the users interaction with the Product in order to help influence
 * future recommendations.
 *
 * RecommendationView must be a child view of {@link RecommendationsContainerView} or {@link RecommendationsRecyclerView}
 * RecommendationView must have a {@link BVProduct} associated by calling {@link #setBvProduct(BVProduct)}
 *
 */
public final class RecommendationView extends BVView {

    private static final String TAG = RecommendationView.class.getSimpleName();

    private BVProduct bvProduct;

    public RecommendationView(Context context) {
        super(context);
    }

    public RecommendationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * @param bvProduct Bazaarvoice recommended product that should be associated with this view
     */
    public void setBvProduct(BVProduct bvProduct) {
        this.bvProduct = bvProduct;
    }

    @Override
    public String getProductId() {
        if (bvProduct == null) {
            throw new IllegalStateException("Must associate BVProduct with RecommendationView");
        }
        return bvProduct.getId();
    }

    @Override
    public void onFirstTimeOnScreen() {
        ShopperProfile shopperProfile = getShopperProfileFromParentViews();
        RecommendationsAnalyticsManager.sendFeatureUsedEvent(TAG, shopperProfile, bvProduct);
    }

    @Override
    public void onTap() {
        ShopperProfile shopperProfile = getShopperProfileFromParentViews();
        RecommendationsAnalyticsManager.sendProductConversionEvent(shopperProfile, bvProduct);
    }

    private ShopperProfile getShopperProfileFromParentViews() {
        boolean foundPersonalizationView = false;
        ShopperProfile shopperProfile = null;
        ViewParent currentParent = getParent();

        while (currentParent != null && !foundPersonalizationView) {
            if (getParent() instanceof PersonalizationView) {
                shopperProfile = ((PersonalizationView) getParent()).getShopperProfile();
                foundPersonalizationView = true;
            }
            currentParent = currentParent.getParent();
        }
        if (!foundPersonalizationView) {
            throw new IllegalStateException("RecommendationView must be a child view of RecommendationsContainerView or RecommendationsRecyclerView");
        }
        return shopperProfile;
    }

}
