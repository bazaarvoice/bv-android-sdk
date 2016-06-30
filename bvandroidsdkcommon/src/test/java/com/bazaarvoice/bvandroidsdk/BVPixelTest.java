package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bazaarvoice on 3/30/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class BVPixelTest {

    String clientId = "fooClient";
    BazaarEnvironment environment = BazaarEnvironment.STAGING;
    String apiKeyShopperAd = "fooBarApiKey";

    @Before
    public void setup() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(apiKeyShopperAd)
                .build();
    }

    @Test
    public void conversionTransactionWithoutPII(){

        Map<String, Object> params = new HashMap<>();
        params.put("state", "TX");
        Transaction transaction = getTransaction(params);
        ConversionTransactionSchema schema = new ConversionTransactionSchema(new MagpieMobileAppPartialSchema.Builder().build(), transaction);
        assert(schema.getPIIEvent() == null);
    }

    @Test
    public void conversionTransactionWithPII(){

        Map<String, Object> params = new HashMap<>();
        params.put("state", "TX");
        params.put("email", "some.one@domain.com"); //This is PII
        Transaction transaction = getTransaction(params);

        ConversionTransactionSchema schema = new ConversionTransactionSchema(new MagpieMobileAppPartialSchema.Builder().build(), transaction);
        assert(schema.getPIIEvent() != null);
    }

    private Transaction getTransaction(Map<String, Object> params){
        List<TransactionItem> items = new ArrayList<>();

        TransactionItem item = new TransactionItem.Builder("12345").price(19.99).category("toys").name("Yo-yo").build();
        items.add(item);
        Transaction transaction = new Transaction.Builder("TestAndroid-orderid",19.99, items, params).build();
        return transaction;
    }

}
