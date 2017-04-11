package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

public final class BVConfig {
  private final String apiKeyConversations;
  private final String apiKeyConversationsStores;
  private final String apiKeyCurations;
  private final String apiKeyLocation;
  private final String apiKeyShopperAdvertising;
  private final String apiKeyPIN;
  private final String clientId;
  private final boolean dryRunAnalytics;

  BVConfig(Builder builder) {
    this.apiKeyConversations = builder.apiKeyConversations;
    this.apiKeyConversationsStores = builder.apiKeyConversationsStores;
    this.apiKeyCurations = builder.apiKeyCurations;
    this.apiKeyLocation = builder.apiKeyLocation;
    this.apiKeyShopperAdvertising = builder.apiKeyShopperAdvertising;
    this.apiKeyPIN = builder.apiKeyPIN;
    this.clientId = builder.clientId;
    this.dryRunAnalytics = builder.dryRunAnalytics;
  }

  String getApiKeyConversations() {
    return apiKeyConversations;
  }

  String getApiKeyConversationsStores() {
    return apiKeyConversationsStores;
  }

  String getApiKeyCurations() {
    return apiKeyCurations;
  }

  String getApiKeyLocation() {
    return apiKeyLocation;
  }

  String getApiKeyShopperAdvertising() {
    return apiKeyShopperAdvertising;
  }

  String getApiKeyPIN() {
    return apiKeyPIN;
  }

  String getClientId() {
    return clientId;
  }

  boolean isDryRunAnalytics() {
    return dryRunAnalytics;
  }

  Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {
    private String apiKeyConversations;
    private String apiKeyConversationsStores;
    private String apiKeyCurations;
    private String apiKeyLocation;
    private String apiKeyShopperAdvertising;
    private String apiKeyPIN;
    private String clientId;
    private boolean dryRunAnalytics = false;

    public Builder() {}

    private Builder(BVConfig bvConfig) {
      this.apiKeyConversations = bvConfig.apiKeyConversations;
      this.apiKeyConversationsStores = bvConfig.apiKeyConversationsStores;
      this.apiKeyCurations = bvConfig.apiKeyCurations;
      this.apiKeyLocation = bvConfig.apiKeyLocation;
      this.apiKeyShopperAdvertising = bvConfig.apiKeyShopperAdvertising;
      this.apiKeyPIN = bvConfig.apiKeyPIN;
      this.clientId = bvConfig.clientId;
      this.dryRunAnalytics = bvConfig.dryRunAnalytics;
    }

    public Builder apiKeyConversations(String apiKeyConversations) {
      this.apiKeyConversations = apiKeyConversations;
      return this;
    }

    public Builder apiKeyConversationsStores(String apiKeyConversationsStores) {
      this.apiKeyConversationsStores = apiKeyConversationsStores;
      return this;
    }

    public Builder apiKeyCurations(String apiKeyCurations) {
      this.apiKeyCurations = apiKeyCurations;
      return this;
    }

    public Builder apiKeyLocation(String apiKeyLocation) {
      this.apiKeyLocation = apiKeyLocation;
      return this;
    }

    public Builder apiKeyShopperAdvertising(String apiKeyShopperAdvertising) {
      this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
      return this;
    }

    public Builder apiKeyPIN(String apiKeyPIN) {
      this.apiKeyPIN = apiKeyPIN;
      return this;
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder dryRunAnalytics(boolean dryRunAnalytics) {
      this.dryRunAnalytics = dryRunAnalytics;
      return this;
    }

    public BVConfig build() {
      return new BVConfig(this);
    }
  }

  static class BVConfigUtil {
    private static final String PROD_FILE_NAME = "bvsdk_config_prod.json";
    private static final String STAGING_FILE_NAME = "bvsdk_config_staging.json";

    @NonNull
    private static String getFileName(BazaarEnvironment bazaarEnvironment) {
      return bazaarEnvironment == BazaarEnvironment.STAGING ? STAGING_FILE_NAME : PROD_FILE_NAME;
    }

    @NonNull
    static BVConfig getBvConfig(Context context, BazaarEnvironment bazaarEnvironment) {
      String configFileName = getFileName(bazaarEnvironment);
      BVConfig bvConfig = null;
      try {
        InputStream inputStream = context.getAssets().open(configFileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        String bvConfigStr = source.readUtf8();
        Gson gson = new Gson();
        bvConfig = gson.fromJson(bvConfigStr, BVConfig.class);
      } catch (JsonSyntaxException e) {
        throw new RuntimeException("Failed to parse configuration file " + configFileName, e);
      } catch (IOException e) {
        throw new RuntimeException("Failed to find configuration file " + configFileName, e);
      }

      return bvConfig;
    }
  }
}
