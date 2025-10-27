package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import com.bazaarvoice.bvsdkdemoandroid.DemoAppComponent;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorPresenterModule;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;

import dagger.Component;

@DemoActivityScope
@Component(dependencies = DemoAppComponent.class, modules = {DemoProductSentimentsPresenterModule.class})
public interface DemoProductSentimentsComponent {
    void inject(DemoProductSentimentsActivity activity);
}