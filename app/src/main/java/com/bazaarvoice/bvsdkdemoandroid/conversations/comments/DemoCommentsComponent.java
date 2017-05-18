package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import com.bazaarvoice.bvsdkdemoandroid.DemoAppComponent;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Component;

@DemoActivityScope
@Component(dependencies = DemoAppComponent.class, modules = {DemoCommentsModule.class})
public interface DemoCommentsComponent {
  void inject(DemoCommentsActivity activity);
}
