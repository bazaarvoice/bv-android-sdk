package com.bazaarvoice.bvandroidsdk;

class BVEventKeys {
  static class Event {
    static final String CLASS = "cl";
    static final String TYPE = "type";
    static final String SOURCE = "source";
  }

  static class Transaction {
    static final String CITY = "city";
    static final String COUNTRY = "country";
    static final String CURRENCY = "currency";
    static final String ORDER_ID = "orderId";
    static final String STATE = "state";
    static final String SHIPPING = "shipping";
    static final String TAX = "tax";
    static final String TOTAL = "total";
    static final String ITEMS = "items";
  }

  static class TransactionItem {
    static final String SKU = "sku";
    static final String CATEGORY = "category";
    static final String IMAGE_URL = "imageurl";
    static final String NAME = "name";
    static final String PRICE = "price";
    static final String QUANTITY = "quantity";
  }

  static class FeatureUsedEvent {
    static final String PRODUCT_ID = "productId";
    static final String BRAND = "brand";
    static final String BV_PRODUCT_TYPE = "bvProduct";
    static final String CONTENT_ID = "contentId";
    static final String BV_CONTENT_TYPE = "contentType";
    static final String DETAIL_1 = "detail1";
    static final String CONTAINER_ID = "component";
    static final String BV_FEATURE_TYPE = "name";
    static final String INTERACTION = "interaction";
    static final String HAS_FINGERPRINT = "fingerprinting";
  }

  static class PageViewEvent {
    static final String BV_PRODUCT_TYPE = "bvProduct";
    static final String PRODUCT_ID = "productId";
    static final String CATEGORY_ID = "categoryId";
    static final String REPORTING_GROUP = "reportingGroup";
  }

  static class ImpressionEvent {
    static final String PRODUCT_ID = "productId";
    static final String CONTENT_ID = "contentId";
    static final String BV_PRODUCT_TYPE = "bvProduct";
    static final String BV_CONTENT_TYPE = "contentType";
    static final String CATEGORY_ID = "categoryId";
    static final String BRAND = "brand";
  }

  static class MobileEvent {
    static final String MOBILE_SOURCE = "mobileSource";
    static final String ADVERTISING_ID = "advertisingId";
    static final String CLIENT_ID = "client";
  }

  static class CommonAnalyticsParams {
    static final String HASHED_IP = "HashedIP";
    static final String USER_AGENT = "UA";
  }

  static class NonCommerceConversionEvent {
    static final String LOAD_ID = "loadId";
    static final String HAD_PII = "hadPII";
    static final String VALUE = "value";
    static final String LABEL = "label";
  }

  static class MobileAppLifecycleEvent {
    static final String APP_STATE = "appState";
    static final String MOBILE_OS = "mobileOS";
    static final String MOBILE_OS_VERSION = "mobileOSVersion";
    static final String MOBILE_DEVICE_NAME = "mobileDeviceName";
    static final String MOBILE_APP_IDENTIFIER = "mobileAppIdentifier";
    static final String MOBILE_APP_VERSION = "mobileAppVersion";
    static final String BV_SDK_VERSION = "bvSDKVersion";
  }

  static class PersonalizationEvent {
    static final String PROFILE_ID = "profileId";
  }

  static final String BATCH = "batch";
}
