package com.bazaarvoice.bvandroidsdk;

final class BVRemoteConfig {

    private static final String RELATIVE_CONFIG_TEMPLATE_URL = "incubator-mobile-apps/sdk/v1/android/%s/%s/%s";

    static String getFeatureConfigUrl(String remoteFeatureName, String remoteFileName) {
        BVSDK bvsdk = BVSDK.getInstance();
        String amazonS3Url = bvsdk.getRootApiUrls().getNotificationConfigUrl();
        String clientId = bvsdk.getClientId();
        return amazonS3Url + String.format(RELATIVE_CONFIG_TEMPLATE_URL,
                clientId, remoteFeatureName, remoteFileName);
    }

}
