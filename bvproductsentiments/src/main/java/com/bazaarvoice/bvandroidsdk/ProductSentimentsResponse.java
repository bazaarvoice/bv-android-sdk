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

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Base options for a ProductSentiments response, to display.
 */
public abstract class ProductSentimentsResponse {
  @SerializedName(value = "HasErrors", alternate = "hasErrors") private Boolean hasErrors;
  @SerializedName(value = "Errors", alternate = "errors") private List<Error> errors;

  public Boolean getHasErrors() {
    return hasErrors;
  }

  public List<Error> getErrors() {
    return errors;
  }
}