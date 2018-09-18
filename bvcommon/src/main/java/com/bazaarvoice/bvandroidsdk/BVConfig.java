package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Type;

import java.util.Locale;

import okio.BufferedSource;
import okio.Okio;

public final class BVConfig {
  // analyticsDefaultLocale can't be final if we want to set it in our custom deserializer.
  private Locale analyticsDefaultLocale;
  private final String apiKeyConversations;
  private final String apiKeyConversationsStores;
  private final String apiKeyCurations;
  private final String apiKeyLocation;
  private final String apiKeyShopperAdvertising;
  private final String clientId;
  private final boolean dryRunAnalytics;

  BVConfig(Builder builder) {
    this.analyticsDefaultLocale = builder.analyticsDefaultLocale;
    this.apiKeyConversations = builder.apiKeyConversations;
    this.apiKeyConversationsStores = builder.apiKeyConversationsStores;
    this.apiKeyCurations = builder.apiKeyCurations;
    this.apiKeyLocation = builder.apiKeyLocation;
    this.apiKeyShopperAdvertising = builder.apiKeyShopperAdvertising;
    this.clientId = builder.clientId;
    this.dryRunAnalytics = builder.dryRunAnalytics;
  }

  Locale getAnalyticsDefaultLocale() {
    return analyticsDefaultLocale;
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
    private Locale analyticsDefaultLocale;
    private String apiKeyConversations;
    private String apiKeyConversationsStores;
    private String apiKeyCurations;
    private String apiKeyLocation;
    private String apiKeyShopperAdvertising;
    private String clientId;
    private boolean dryRunAnalytics = false;

    public Builder() {
    }

    private Builder(BVConfig bvConfig) {
      this.analyticsDefaultLocale = bvConfig.analyticsDefaultLocale;
      this.apiKeyConversations = bvConfig.apiKeyConversations;
      this.apiKeyConversationsStores = bvConfig.apiKeyConversationsStores;
      this.apiKeyCurations = bvConfig.apiKeyCurations;
      this.apiKeyLocation = bvConfig.apiKeyLocation;
      this.apiKeyShopperAdvertising = bvConfig.apiKeyShopperAdvertising;
      this.clientId = bvConfig.clientId;
      this.dryRunAnalytics = bvConfig.dryRunAnalytics;
    }

    public Builder analyticsDefaultLocale(Locale analyticsDefaultLocale) {
      this.analyticsDefaultLocale = analyticsDefaultLocale;
      return this;
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
      BVConfig bvConfig;
      try {
        InputStream inputStream = context.getAssets().open(configFileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        String bvConfigStr = source.readUtf8();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BVConfig.class, new BVConfigDeserializer());
        Gson gson = gsonBuilder.create();
        bvConfig = gson.fromJson(bvConfigStr, BVConfig.class);
      } catch (JsonSyntaxException e) {
        throw new RuntimeException("Failed to parse configuration file " + configFileName, e);
      } catch (IOException e) {
        throw new RuntimeException("Failed to find configuration file " + configFileName, e);
      }

      return bvConfig;
    }
  }

  static class BVConfigDeserializer implements JsonDeserializer<BVConfig> {

    private static final String ANALYTICS_LOCALE_IDENTIFIER = "analyticsLocaleIdentifier";

    @Override
    public BVConfig deserialize(
            JsonElement paramJsonElement,
            Type paramType,
            JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {

      BVConfig config = new Gson().fromJson(paramJsonElement.getAsJsonObject(), BVConfig.class);

      try {
        JsonElement analyticsLocaleIdentifier =
                paramJsonElement.getAsJsonObject()
                        .get(ANALYTICS_LOCALE_IDENTIFIER);

        if (analyticsLocaleIdentifier != null) {
          String analyticsLocaleString = analyticsLocaleIdentifier.getAsString();
              if (analyticsLocaleString != null) {
                config.analyticsDefaultLocale = new Locale("", analyticsLocaleString.toUpperCase());
              }
        }
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Failed to create default Locale from configuration file " + e);
      }

      return config;
    }
  }
}
