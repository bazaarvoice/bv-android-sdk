package com.bazaarvoice.bvsdkdemoandroid.settings;

import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoSettingsModule {
  @Provides @Named("SettingsDisplayNames")
  CharSequence[] provideDisplayNames(DemoClientConfigUtils demoClientConfigUtils) {
    return demoClientsToCharSeq(demoClientConfigUtils.getCurrentSources(), new ReduceDemoClient() {
      @Override
      public String reduce(DemoClient demoClient) {
        return demoClient.getDisplayName();
      }
    });
  }

  @Provides @Named("SettingsClientIdNames")
  CharSequence[] provideClientIdNames(DemoClientConfigUtils demoClientConfigUtils) {
    return demoClientsToCharSeq(demoClientConfigUtils.getCurrentSources(), new ReduceDemoClient() {
      @Override
      public String reduce(DemoClient demoClient) {
        return demoClient.getClientId();
      }
    });
  }

  /**
   * Maps a DemoClient to one of it's fields
   */
  private interface ReduceDemoClient {
    String reduce(DemoClient demoClient);
  }

  private CharSequence[] demoClientsToCharSeq(List<DemoClient> demoClients, ReduceDemoClient reduceDemoClient) {
    CharSequence[] arr = new CharSequence[demoClients.size()];
    for (int i=0; i<arr.length; i++) {
      DemoClient demoClient = demoClients.get(i);
      CharSequence reduced = reduceDemoClient.reduce(demoClient);
      arr[i] = reduced;
    }
    return arr;
  }
}
