package com.bazaarvoice.bvsdkdemoandroid.configs;

import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

class DemoSingleTenantSource implements DemoClientSource {
  String DEMO_CONFIG_STAGING_FILE = "bvsdk_config_staging.json";
  String DEMO_CONFIG_PROD_FILE = "bvsdk_config_prod.json";

  private DemoAssetsUtil demoAssetsUtil;
  private List<DemoClient> stagingClients = Collections.emptyList();
  private List<DemoClient> prodClients = Collections.emptyList();

  @Inject
  DemoSingleTenantSource(DemoAssetsUtil demoAssetsUtil) {
    this.demoAssetsUtil = demoAssetsUtil;
    if (doesSourceExist()) {
      DemoClient demoStagingClient = demoAssetsUtil.parseJsonFileFromAssets(DEMO_CONFIG_STAGING_FILE, DemoClient.class);
      DemoClient demoProdClient = demoAssetsUtil.parseJsonFileFromAssets(DEMO_CONFIG_PROD_FILE, DemoClient.class);
      demoStagingClient.setDisplayName("BVSDK Config Staging");
      demoProdClient.setDisplayName("BVSDK Config Prod");
      this.stagingClients = Collections.singletonList(demoStagingClient);
      this.prodClients = Collections.singletonList(demoProdClient);
    }
  }

  @Override
  public boolean doesSourceExist() {
    return bvConfigProdFileExists() && bvConfigStagingFileExists();
  }

  @Override
  public List<DemoClient> getProdClients() {
    return prodClients;
  }

  @Override
  public List<DemoClient> getStagingClients() {
    return stagingClients;
  }

  private boolean bvConfigStagingFileExists() {
    return demoAssetsUtil.assetFileExists(DEMO_CONFIG_STAGING_FILE);
  }

  private boolean bvConfigProdFileExists() {
    return demoAssetsUtil.assetFileExists(DEMO_CONFIG_PROD_FILE);
  }
}
