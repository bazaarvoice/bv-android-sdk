/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.HashSet;
import java.util.Set;

import static com.bazaarvoice.bvandroidsdk.EventView.ViewUtil.isVisibleOnScreen;
import static com.bazaarvoice.bvandroidsdk.EventView.ViewUtil.seenOnScreenWithId;

class EventView {
    interface EventViewListener<ViewType extends View> {
        /**
         * @param onScreen When the view enters/exits the viewport.
         */
        void onVisibleOnScreenStateChanged(boolean onScreen);

        /**
         * Called whenever a view enters the viewport. Will also be called on the same view,
         * if the id associated with it is different than the last time the view entered the
         * viewport.
         */
        void onFirstTimeOnScreen();
    }

    interface ProductView {
        String getProductId();
    }

    /**
     * Binds the provided id to listeners that can notify users about viewport visibility
     * for the provided View. Not to be confused with {@link View#VISIBLE}.
     *
     * @param view The View to observe
     * @param listener The listener to notify about viewport state transitions
     * @param productView The implementer will provide the product id associated with the View
     * @param <ViewType> The type of the View child class
     */
    public static <ViewType extends View> void bind(ViewType view, EventViewListener<ViewType> listener, ProductView productView) {
        EventView.ViewsMovingListener<ViewType> viewsMovingListener = new EventView.ViewsMovingListener<ViewType>(view, listener, productView);
        view.getViewTreeObserver().addOnGlobalLayoutListener(viewsMovingListener);
        view.getViewTreeObserver().addOnScrollChangedListener(viewsMovingListener);
    }

    static class Subject<ViewType extends View> {
        private ViewType bvView;
        private EventViewListener<ViewType> eventViewListener;
        private final Rect rect;
        private final Point globalOffset;
        private boolean lastOnScreenState = false;
        private final ProductView productView;

        Subject(ViewType view, EventViewListener<ViewType> eventViewListener, ProductView productView) {
            this.bvView = view;
            this.eventViewListener = eventViewListener;
            this.rect = new Rect();
            this.globalOffset = new Point();
            this.productView = productView;
        }

        void processEvent(String logSource) {
            boolean onScreen = isVisibleOnScreen(bvView, rect, globalOffset);
            if (onScreen != lastOnScreenState) {
                eventViewListener.onVisibleOnScreenStateChanged(onScreen);
            }
            if (onScreen && !seenOnScreenWithId(bvView, productView.getProductId())) {
                eventViewListener.onFirstTimeOnScreen();
            }
            lastOnScreenState = onScreen;
        }
    }

    static class ViewUtil {
        /**
         * Only call this when the View was seen in the viewport with an associated id
         *
         * @param view The View that will be tracked
         * @param id A unique id to associate with this view
         * @param <ViewType> The type of View child class
         * @return Whether this View+Id has already been seen in the viewport
         */
        static <ViewType extends View> boolean seenOnScreenWithId(ViewType view, String id) {
            Object seenIdsForViewObj = view.getTag(com.bazaarvoice.bvandroidsdk_common.R.string.seen_product_onscreen_with_id_set);
            if (seenIdsForViewObj == null) {
                Set<String> seenIdsForView = new HashSet<String>();
                updateSeenId(seenIdsForView, view, id);
                return false;
            } else if (seenIdsForViewObj instanceof Set) {
                Set<String> seenIdsForView = (Set<String>) seenIdsForViewObj;
                if (seenIdsForView.contains(id)) {
                    return true;
                } else {
                    updateSeenId(seenIdsForView, view, id);
                    return false;
                }
            } else {
                throw new IllegalStateException("ids associated with view are not in expected state");
            }
        }

        static <ViewType extends View> void updateSeenId(Set<String> seenIdsForView, ViewType view, String id) {
            seenIdsForView.add(id);
            view.setTag(com.bazaarvoice.bvandroidsdk_common.R.string.seen_product_onscreen_with_id_set, seenIdsForView);
        }

        static boolean isVisibleOnScreen(View view, Rect rect, Point globalOffset) {
            if (view == null) {
                throw new IllegalArgumentException("view must not be null");
            }
            return view.getGlobalVisibleRect(rect, globalOffset);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static class ViewsMovingListener<ViewType extends View> implements ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener {
        private final Subject<ViewType> eventViewSubject;

        ViewsMovingListener(ViewType view, EventViewListener<ViewType> eventViewListener, ProductView productView) {
            this.eventViewSubject = new Subject<ViewType>(view, eventViewListener, productView);
        }

        @Override
        public void onScrollChanged() {
            eventViewSubject.processEvent("onScrollChanged");
        }

        @Override
        public void onGlobalLayout() {
            eventViewSubject.processEvent("onGlobalLayout");
        }

    }
}
