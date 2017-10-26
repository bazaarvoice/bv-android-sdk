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

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

import java.util.List;

public interface DemoCarouselContract {

    interface View extends DemoBaseView<Presenter> {
        <ProductType extends BVDisplayableProductContent> void updateContent(List<ProductType> contentList);
        void showEmpty(boolean showing);
        void showEmptyMessage(String message);
        void showLoading(boolean showing);
        void setOnItemClickListener(OnItemClickListener onItemClickListener);
        void showProductTapped(String name);
    }

    interface Presenter extends DemoBasePresenter {

    }

    interface OnItemClickListener {
        void onItemClicked(BVDisplayableProductContent productContent);
    }
}
