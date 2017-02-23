package com.bazaarvoice.bvandroidsdk;

import android.os.HandlerThread;
import android.os.Looper;

import org.robolectric.shadows.ShadowLooper;

public class BaseTestUtils {
  public static class TestHandlerThread extends HandlerThread {
    public TestHandlerThread() {
      super("Test-Thread");
    }

    @Override
    public Looper getLooper() {
      return ShadowLooper.getMainLooper();
    }
  }
}
