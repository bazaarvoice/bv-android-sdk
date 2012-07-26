package com.bazaarvoice.test;

import com.bazaarvoice.ApiVersion;
import com.bazaarvoice.BazaarRequest;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 4/26/12
 * Time: 10:51 PM
 */
public class BaseTest extends TestCase {

    public BazaarRequest request;
    public BazaarRequest submit;
    private final String tag = getClass().getSimpleName();
    
    //Set this to change version of all tests
    protected ApiVersion apiVersion = ApiVersion.FIVE_TWO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = new BazaarRequest(
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "kuy3zj9pr3n7i0wxajrzj04xo",
                apiVersion);

        submit = new BazaarRequest(
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "1wtp4lx7aww42x4154oly21ae",
                apiVersion);
    }
}
