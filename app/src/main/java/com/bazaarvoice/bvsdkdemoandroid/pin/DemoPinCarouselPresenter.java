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

package com.bazaarvoice.bvsdkdemoandroid.pin;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.Pin;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvsdkdemoandroid.carousel.DemoCarouselContract;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoPinCarouselPresenter implements DemoCarouselContract.Presenter {

    private static final String TAG = "PinCarouselPres";
    private DemoCarouselContract.View view;
    private PinClient pinClient;

    @Inject
    public DemoPinCarouselPresenter(@Named("PinCarouselView") DemoCarouselContract.View view, PinClient pinClient) {
        this.view = view;
        this.pinClient = pinClient;
        view.setOnItemClickListener(new DemoCarouselContract.OnItemClickListener() {
            @Override
            public void onItemClicked(BVDisplayableProductContent productContent) {
                showProductTapped(productContent);
            }
        });
    }

    @Override
    public void start() {
        view.showLoading(true);
        pinClient.getPendingPins(pinCb);
    }

    private PinClient.PinsCallback pinCb = new PinClient.PinsCallback() {
        @Override
        public void onSuccess(List<Pin> pins) {
            showStartLoading();

            if (pins == null || pins.isEmpty()) {
                showNoPinsFailure();
            } else {
                showContentSuccess(pins);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            showFetchPinsFailure(throwable);
        }
    };

    private void showProductTapped(BVDisplayableProductContent productContent) {
        view.showProductTapped(productContent.getDisplayName());
    }

    private void showStartLoading() {
        view.showLoading(true);
        view.showEmpty(false);
    }

    private void showContentSuccess(List<Pin> pins) {
        view.showLoading(false);
        view.showEmpty(false);
        view.updateContent(pins);
    }

    private void showNoPinsFailure() {
        Log.e(TAG, "No PINS available");
        view.showLoading(false);
        view.showEmpty(true);
        view.showEmptyMessage("No PINs available");
    }

    private void showFetchPinsFailure(Throwable throwable) {
        view.showLoading(false);
        Log.e(TAG, "Failed to get PINs", throwable);
        view.showEmptyMessage("Failed to get PINs");
        view.showEmpty(true);
    }

    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }

}
