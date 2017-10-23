package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.WorkerThread;

/**
 * <p>
 *  Authentication that clients configured for
 *  <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Collect/site_authentication.html">Site Authentication</a>
 *  should use. You can check which authentication configuration you have by following
 *  <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Learn/user_authentication.html#determine-your-authentication-configuration">these steps</a>.
 * </p>
 * <p>
 *  You will need to implement this to be able to retrieve an encrypted User Authentication String (UAS)
 *  from your companies backend.
 * </p>
 * <p>
 *  Your company may have an existing endpoint to retrieve this from, if your web team has already implemented
 *  this. If not you will need to follow <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Collect/site_authentication.html">these steps</a>.
 * </p>
 * <p>
 *  This function will be called synchronously on a background thread, whenever a {@link ConversationsSubmissionRequest} is
 *  sent. To avoid hitting the network repeatedly implement caching as desired. For example,
 *  <code><pre>public class MySiteAuthenticationProvider extends SiteAuthenticationProvider {
 *  private final MyUasService myUasService;
 *  private final SharedPreferences sharedPrefs;
 *  private final String userPrefKey;
 *
 *  public MySiteAuthenticationProvider(MyUasService myUasService, SharedPreferences sharedPrefs, String username) {
 *    this.myUasService = myUasService;
 *    this.sharedPrefs = sharedPrefs;
 *    this.userPrefKey = String.format("%s_uas", username);
 *  }
 *
 *  &#64;Override
 *  public String getUas() {
 *    String uas = sharedPrefs.getString(userPrefKey, null);
 *    if (uas == null) {
 *      uas = myUasService.getUas();
 *      sharedPrefs.edit().putString(userPrefKey, uas);
 *    }
 *    return uas;
 *  }
 *}</pre></code>
 * </p>
 */
public abstract class SiteAuthenticationProvider implements AuthenticationProvider {
  @WorkerThread public abstract String getUas();
}