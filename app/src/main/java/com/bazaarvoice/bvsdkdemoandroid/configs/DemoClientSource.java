package com.bazaarvoice.bvsdkdemoandroid.configs;

import java.util.List;

interface DemoClientSource {
  boolean doesSourceExist();
  List<DemoClient> getStagingClients();
  List<DemoClient> getProdClients();

  class Util {
    static DemoClient getConfigFromClientId(String clientId, List<DemoClient> demoClientList) {
      DemoClient clientConfig = DemoClient.EMPTY_CONFIG;
      for (DemoClient currConfig : demoClientList) {
        if (currConfig.getClientId().equals(clientId)) {
          clientConfig = currConfig;
          break;
        }
      }
      return clientConfig;
    }
  }
}
