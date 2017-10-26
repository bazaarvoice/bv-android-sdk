package com.bazaarvoice.bvsdkdemoandroid.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Documented @Qualifier @Retention(RetentionPolicy.RUNTIME)
public @interface DemoAppContext {}