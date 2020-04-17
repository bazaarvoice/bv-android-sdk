package com.bazaarvoice.bvandroidsdk;

import android.os.Build;

import com.bazaarvoice.bvandroidsdk_analytics.BuildConfig;

public class BVEventValues {
    static final String BVSDK_USER_AGENT = "Mozilla/5.0 (Linux; Android "+ Build.VERSION.RELEASE +" " + Build.DEVICE+ " "+ Build.MODEL+") bvsdk-android/" + BuildConfig.BVSDK_VERSION_NAME;
    public enum BVFeatureUsedEventType {
    WRITE_REVIEW("Write"),
    ASK_QUESTION("Question"),
    ANSWER_QUESTION("Answer"),
    FEEDBACK("Helpfulness"),
    INAPPROPRIATE("Inappropriate"),
    PHOTO("Photo"),
    SCROLLED("Scrolled"),
    IN_VIEW("InView"),
    PROFILE("Profile"),
    CONTENT_CLICK("Click"),
    NOTIFICATION("PushNotification"),
    ENGAGED("Engaged");

    private String value;

    BVFeatureUsedEventType(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public enum BVImpressionContentType {
    REVIEW("Review"),
    QUESTION("Question"),
    ANSWER("Answer"),
    COMMENT("Comment"),
    PRODUCT_RECOMMENDATION("Recommendation"),
    CURATIONS_FEED_ITEM("SocialPost"),
    STORE("Store"),
    STORE_REVIEW("StoreReview");

    private String value;

    BVImpressionContentType(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public enum BVProductType {
    AUTHENTICATION("Authentication"),
    CONVERSATIONS_REVIEWS("RatingsAndReviews"),
    CONVERSATIONS_QANDA("AskAndAnswer"),
    CONVERSATIONS_REVIEWHIGHLIGHTS("ReviewHighlights"),
    CONVERSATIONS_PROFILE("Profiles"),
    CURATIONS("Curations"),
    PERSONALIZATION("Personalization"),
    VISIT("Visit"),
    USED("Used"),
    PROGRESSIVE_SUBMISSION("ProgressiveSubmission");

    private String value;

    BVProductType(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public enum BVEventType {
    USED("Used"),
    TRANSACTION("Transaction"),
    PRODUCT("Product"),
    PROFILE("ProfileMobile"),
    UGC("UGC"),
    MOBILE_APP("MobileApp"),
    PERSONALIZATION("Personalization"),
    VIEWED_CGC("UsedViewedUGC"),
    VISIT("Visit"),
    EMBEDDED("Embedded"),
    ERORR("Error"),
    RECORD("Record");

    private String value;

    BVEventType(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public enum BVEventClass {
    FEATURE("Feature"),
    CONVERSION("Conversion"),
    PERSONALIZATION("Personalization"),
    PAGE_VIEW("PageView"),
    IMPRESSION("Impression"),
    PII_CONVERSION("PIIConversion"),
    LIFECYCLE("Lifecycle"),
    LOCATION("Location"),
    ERROR("Error");

    private String value;

    BVEventClass(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  enum BVEventSource {
    NATIVE_MOBILE_SDK("native-mobile-sdk"),
    NATIVE_MOBILE_CUSTOM("native-mobile-custom");

    private String value;

    BVEventSource(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  enum AppState {
    LAUNCHED("launched"), PAUSED("background"), RESUMED("active");

    private String value;

    AppState(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }


  static final String NONTRACKING_TOKEN = "nontracking";
  static final String MOBILE_SOURCE = "bv-android-sdk";
  static final String HASHED_IP = "default";
  static final String MOBILE_OS = "Android";
  static final String FEATURE_IN_VIEW = "InView";
}
