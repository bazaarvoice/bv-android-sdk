/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

class EventView {
    interface OnScreenListener<ViewType extends View> {
        void onScreen(ViewType bvView);
    }

    private static class Subject<ViewType extends View> {
        private ViewType bvView;
        private OnScreenListener<ViewType> onScreenListener;
        private final Rect rect;
        private final Point globalOffset;

        Subject(ViewType view, OnScreenListener<ViewType> onScreenListener) {
            this.bvView = view;
            this.onScreenListener = onScreenListener;
            this.rect = new Rect();
            this.globalOffset = new Point();
        }

        void processEvent() {
            if (eventViewVisible()) {
                onScreenListener.onScreen(bvView);
            }
        }

        private boolean eventViewVisible() {
            if (bvView == null) {
                return false;
            }
            boolean isVisibleOnScreen = bvView.getGlobalVisibleRect(rect, globalOffset);
            return isVisibleOnScreen;
        }
    }

    static class ScrollChangeListener<ViewType extends View> implements ViewTreeObserver.OnScrollChangedListener {
        private final Subject<ViewType> eventViewSubject;

        ScrollChangeListener(ViewType view, OnScreenListener<ViewType> onScreenListener) {
            this.eventViewSubject = new Subject<ViewType>(view, onScreenListener);
        }

        @Override
        public void onScrollChanged() {
            eventViewSubject.processEvent();
        }
    }

    static class GlobalLayoutListener<ViewType extends View> implements ViewTreeObserver.OnGlobalLayoutListener {
        private final Subject<ViewType> eventViewSubject;

        GlobalLayoutListener(ViewType view, OnScreenListener<ViewType> onScreenListener) {
            this.eventViewSubject = new Subject<ViewType>(view, onScreenListener);
        }

        @Override
        public void onGlobalLayout() {
            eventViewSubject.processEvent();
        }
    }
}
