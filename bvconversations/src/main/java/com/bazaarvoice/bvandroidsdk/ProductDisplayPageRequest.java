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

/**
 * Request to get info and stats for a {@link Product}
 */
public class ProductDisplayPageRequest extends SortableProductRequest {
  private ProductDisplayPageRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends SortableProductRequest.Builder<Builder, ProductDisplayPageRequest> {
    public Builder(@NonNull String productId) {
      super();
      addFilter(new Filter(Filter.Type.Id, EqualityOperator.EQ, productId));
    }

    @Override
    public ProductDisplayPageRequest build() {
      return new ProductDisplayPageRequest(this);
    }
  }
}