package com.bazaarvoice.test;

import android.test.AndroidTestCase;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.types.ApiVersion;

/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 4/26/12
 * Time: 10:51 PM
 */
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
