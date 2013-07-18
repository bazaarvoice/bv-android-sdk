/*******************************************************************************
 * Copyright 2013 Bazaarvoice
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
 ******************************************************************************/
package com.bazaarvoice.test;

import android.test.AndroidTestCase;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.types.ApiVersion;

public class BaseTest extends AndroidTestCase {

    public BazaarRequest request;
    public BazaarRequest submit;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = new BazaarRequest(
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "kuy3zj9pr3n7i0wxajrzj04xo",
                 ApiVersion.FIVE_FOUR);

        submit = new BazaarRequest(
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "1wtp4lx7aww42x4154oly21ae",
                ApiVersion.FIVE_FOUR);
    }
}
