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

package com.bazaarvoice.bvandroidsdk;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Helper class for creating a Sort query parameter
 */
class Sort {
    private final UGCOption option;
    private final SortOrder sortOrder;
    private final List<String> sortList;

    Sort(@NonNull UGCOption option, @NonNull SortOrder sortOrder) {
        this.option = option;
        this.sortOrder = sortOrder;
        this.sortList =new ArrayList<>();
    }

    Sort(@NonNull UGCOption option, @NonNull List<String> sortList) {
        this.option = option;
        this.sortList = sortList;
        this.sortOrder = SortOrder.CUSTOM_SORT_ORDER;
    }

    @Override
    public String toString() {
        String string;
         if(sortOrder.getKey().equals(SortOrder.CUSTOM_SORT_ORDER.getKey())) {
             string = String.format("%s:%s", option.getKey(),  StringUtils.componentsSeparatedByWithEscapes(sortList, ","));
         }else {
             string = String.format("%s:%s", option.getKey(), sortOrder.getKey());
         }

        return string;
    }
}