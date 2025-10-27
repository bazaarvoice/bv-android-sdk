package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import com.bazaarvoice.bvandroidsdk.BVProductSentimentsClient;
import com.bazaarvoice.bvsdkdemoandroid.productsentiments.DemoProductSentimentsContract;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;

@Module
public class DemoProductSentimentsPresenterModule {

    private final DemoProductSentimentsContract.View view;
    private final String productId;

    public DemoProductSentimentsPresenterModule(DemoProductSentimentsContract.View view, String productId) {
        this.view = view;
        this.productId = productId;
    }

    @Provides
    DemoProductSentimentsContract.View providesProductSentimentsView() {
        return view;
    }

    @Provides @Named("ProductId")
    String providesProductId() {
        return productId;
    }

}