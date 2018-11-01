/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;

/**
 * Bazaarvoice Provided {@link FrameLayout} to display {@link RecommendationView} objects
 */
public class RecommendationsContainerView extends BVContainerView implements BVRecommendations.BVRecommendationsLoader, PersonalizationView {
    private static final String TAG = RecommendationsContainerView.class.getSimpleName();

    private String productId, categoryId;
    private PageType pageType;
    private ShopperProfile shopperProfile;
    private WeakReference<BVRecommendations.BVRecommendationsCallback> delegateCbRef;

    private BVRecommendations.BVRecommendationsCallback receiverCb = new BVRecommendations.BVRecommendationsCallback() {
        @Override
        public void onSuccess(BVRecommendationsResponse response) {
            BVRecommendations.BVRecommendationsCallback delegateCb = delegateCbRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbRef.clear();
            delegateCb.onSuccess(response);
            shopperProfile = response.getShopperProfile();
            if (shopperProfile != null) {
                RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.CUSTOM, productId, categoryId, pageType, shopperProfile);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            BVErrorReport bvErrorReport = new BVErrorReport(
                    BVEventValues.BVProductType.CURATIONS,
                    RecommendationsRequest.class.getSimpleName(),
                    new BazaarException(throwable.getMessage(), throwable));
            BVSDK.getInstance().getBvPixel().track(bvErrorReport);
            BVRecommendations.BVRecommendationsCallback delegateCb = delegateCbRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbRef.clear();
            delegateCb.onFailure(throwable);
        }
    };

    public RecommendationsContainerView(Context context) {
        super(context);
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void loadRecommendations(RecommendationsRequest request, BVRecommendations.BVRecommendationsCallback callback) {
        checkMain();
        updateRecCallInfo(request, callback);
        BVRecommendations recommendations = new BVRecommendations();
        recommendations.getRecommendedProducts(request, receiverCb);
    }

    private void updateRecCallInfo(final RecommendationsRequest request, final BVRecommendations.BVRecommendationsCallback delegateCb) {
        this.productId = request.getProductId();
        this.categoryId = request.getCategoryId();
        this.pageType = request.getPageType();
        this.delegateCbRef = new WeakReference<>(delegateCb);
    }

    @Override
    public void onAddedToViewHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.CUSTOM);
    }

    @Override
    public ShopperProfile getShopperProfile() {
        return shopperProfile;
    }
}
