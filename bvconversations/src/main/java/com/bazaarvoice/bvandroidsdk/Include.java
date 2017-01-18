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

/**
 * Helper class to add {@link PDPContentType} items to
 * be included in a conversations response
 */
class Include {

    private final PDPContentType type;
    private final Integer limit;

    Include(PDPContentType type, Integer limit) {
        this.type = type;
        this.limit = limit;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    String getLimitParamKey() {
        return String.format("Limit_%s", this.type.toString());
    }

    Integer getLimit() {
        return this.limit;
    }
}
