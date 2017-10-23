package com.bazaarvoice.bvandroidsdk;

/**
 * <p>
 *  A type for classes that handle the details about which fields are necessary for
 *  authenticating {@link ConversationsSubmissionRequest}s.
 * </p>
 * <p>
 *  If your api key is enabled for Site Authentication you should use the
 *  {@link SiteAuthenticationProvider}. If your api key is enabled for BV Hosted
 *  Authentication you should use {@link BVHostedAuthenticationProvider}.
 *  You can check which authentication configuration you have by following
 *  <a href="http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/Learn/user_authentication.html#determine-your-authentication-configuration">these steps</a>.
 * </p>
 */
public interface AuthenticationProvider {}