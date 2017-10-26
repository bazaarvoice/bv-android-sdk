package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.content.Context;
import android.content.Intent;

import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.home.DemoHomeActivity;

import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil.LaunchCode.API;
import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil.LaunchCode.NEW;

public class DemoLaunchIntentUtil {
  public static final class LaunchCode {
    public static final int API = 1;
    public static final int NEW = 2;
  }

  public static Intent getLaunchIntent(Context currentContext, int launchCode) {
    switch (launchCode) {
      case API: {
        return new Intent(currentContext, DemoMainActivity.class);
      }
      case NEW:
      default: {
        return new Intent(currentContext, DemoHomeActivity.class);
      }
    }
  }
}
