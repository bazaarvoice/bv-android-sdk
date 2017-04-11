package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

import com.bazaarvoice.bvsdkdemoandroid.R;

public class DemoRequiredKeyUiUtil {
  private static AlertDialog getNoApiKeyAlertDialog(Context activityContext, String displayName, String featureName) {
    Resources res = activityContext.getResources();
    return new AlertDialog.Builder(activityContext)
        .setMessage(String.format(res.getString(R.string.view_demo_error_message), displayName, featureName))
        .setPositiveButton(res.getString(R.string.okay), null)
        .create();
  }

  public static AlertDialog getNoReccosApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_recommendations));
  }

  public static AlertDialog getNoCurationsApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_curations));
  }

  public static AlertDialog getNoConversationsApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_conversations));
  }

  public static AlertDialog getNoLocationApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_location));
  }

  public static AlertDialog getNoStoresApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_conversations_stores));
  }

  public static AlertDialog getNoPinApiKeyDialog(Context activityContext, String displayName) {
    return getNoApiKeyAlertDialog(activityContext, displayName, activityContext.getString(R.string.demo_pin));
  }
}
