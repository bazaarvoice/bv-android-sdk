package com.bazaarvoice.bvandroidsdk;

import android.os.HandlerThread;
import android.os.Looper;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.NetworkSecurityPolicyWorkaround.class})
public abstract class LoadCallTest {
  @Rule public BVConversationsClientRule clientRule = new BVConversationsClientRule();
  @Rule public ExpectedException expectedException = ExpectedException.none();

  // region stubs and rules

  protected ReviewsRequest getStubDisplayRequest() {
    return new ReviewsRequest.Builder("prod1", 20, 0).build();
  }

  protected ReviewSubmissionRequest getStubSubmissionRequest(Action action) {
    return new ReviewSubmissionRequest.Builder(action, "prod1").build();
  }

  protected static final class BVConversationsClientRule extends ExternalResource {
    private final MockWebServer server;
    private final ExecutorService executorService;
    private BVConversationsClient client;
    private boolean started = true;

    public BVConversationsClientRule() {
      server = new MockWebServer();
      final HttpUrl httpUrl = server.url("/");
      client = getStubConvClient(httpUrl);
      executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public Statement apply(Statement base, Description description) {
      return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {
      if (!started) {
        server.start();
      }
      super.before();
    }

    @Override
    protected void after() {
      super.after();
      try {
        server.close();
        started = false;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public <T> void enqueueHttp200(String jsonFileAsResponse) throws Exception {
      final String responseStr = BVBaseTest.jsonResourceFileAsString(jsonFileAsResponse, this.getClass());
      final MockResponse mockResponse = new MockResponse().setBody(responseStr).setResponseCode(200);
      server.enqueue(mockResponse);
    }

    public <T> Future<T> submit(Callable<T> callable) {
      return executorService.submit(callable);
    }

    public BVConversationsClient getClient() {
      return client;
    }

    private BVConversationsClient getStubConvClient(HttpUrl httpUrl) {
      final BVConfig bvConfig = new BVConfig.Builder().clientId("testClient").apiKeyConversations("fakekey").build();
      final BVSDK bvsdk = mock(BVSDK.class);
      final BVPixel bvPixel = mock(BVPixel.class);
      when(bvsdk.getBvPixel()).thenReturn(bvPixel);
      final BVMobileInfo bvMobileInfo = mock(BVMobileInfo.class);
      final BVUserProvidedData bvUserProvidedData = mock(BVUserProvidedData.class);
      when(bvUserProvidedData.getBvMobileInfo()).thenReturn(bvMobileInfo);
      when(bvsdk.getBvUserProvidedData()).thenReturn(bvUserProvidedData);
      final BVSDK.BVWorkerData bvWorkerData = getStubBvWorkerData(httpUrl);
      when(bvsdk.getBvWorkerData()).thenReturn(bvWorkerData);
      return new BVConversationsClient.Builder(bvsdk)
          .bvConfig(bvConfig)
          .bgLooper(Looper.myLooper())
          .uiLooper(Looper.myLooper())
          .build();
    }

    private BVSDK.BVWorkerData getStubBvWorkerData(HttpUrl httpUrl) {
      final BVRootApiUrls bvRootApiUrls = mock(BVRootApiUrls.class);
      when(bvRootApiUrls.getBazaarvoiceApiRootUrl()).thenReturn(httpUrl.toString());
      final OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .connectTimeout(1, TimeUnit.SECONDS)
          .readTimeout(1, TimeUnit.SECONDS)
          .writeTimeout(1, TimeUnit.SECONDS)
          .build();
      final String bvSdkUserAgent = "user-agent";
      final HandlerThread testBgHandlerThread = new HandlerThread("testBgHandlerThread");
      final Looper bgLooper = testBgHandlerThread.getLooper();
      return new BVSDK.BVWorkerData(new Gson(), bvRootApiUrls, okHttpClient, bvSdkUserAgent, testBgHandlerThread, bgLooper);
    }
  }

  // endregion
}
