package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bazaarvoice on 3/30/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BVPixelTest {

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
