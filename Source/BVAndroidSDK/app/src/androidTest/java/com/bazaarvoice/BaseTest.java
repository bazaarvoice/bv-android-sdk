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
package com.bazaarvoice;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.bazaarvoice.types.ApiVersion;

public class BaseTest extends InstrumentationTestCase {

    public BazaarRequest request;
    public BazaarRequest submit;
    
    protected Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = new BazaarRequest(
                "apitestcustomer",
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "kuy3zj9pr3n7i0wxajrzj04xo",
                 ApiVersion.FIVE_FOUR);

        submit = new BazaarRequest(
                "apitestcustomer",
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "1wtp4lx7aww42x4154oly21ae",
                ApiVersion.FIVE_FOUR);
        
        mContext = this.getInstrumentation().getContext();
    }
}
