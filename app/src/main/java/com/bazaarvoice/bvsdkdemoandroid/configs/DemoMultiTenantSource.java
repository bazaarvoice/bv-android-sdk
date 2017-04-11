package com.bazaarvoice.bvsdkdemoandroid.configs;

import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

class DemoMultiTenantSource implements DemoClientSource {
  private String MULTI_TENANT_CONFIG_FILE = "demo_app_configs.json";

  private DemoAssetsUtil demoAssetsUtil;
  private List<DemoClient> stagingClients = Collections.emptyList();
  private List<DemoClient> prodClients = Collections.emptyList();

  @Inject
  DemoMultiTenantSource(DemoAssetsUtil demoAssetsUtil) {
    this.demoAssetsUtil = demoAssetsUtil;
    if (doesSourceExist()) {
      DemoMultiTenantContainer demoMultiTenantContainer = demoAssetsUtil.parseJsonFileFromAssets(MULTI_TENANT_CONFIG_FILE, DemoMultiTenantContainer.class);
      stagingClients = demoMultiTenantContainer.getStagingConfigs();
      prodClients = demoMultiTenantContainer.getProdConfigs();
    }
  }

  @Override
  public boolean doesSourceExist() {
    return demoAssetsUtil.assetFileExists(MULTI_TENANT_CONFIG_FILE);
  }

  @Override
  public List<DemoClient> getProdClients() {
    return prodClients;
  }

  @Override
  public List<DemoClient> getStagingClients() {
    return stagingClients;
  }

}
