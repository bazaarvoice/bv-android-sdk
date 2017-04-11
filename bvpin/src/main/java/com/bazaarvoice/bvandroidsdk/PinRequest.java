package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

class PinRequest {
    private static final String RELATIVE_URL_TEMPLATE = "%spin/toreview?passkey=%s&bvid=%s&client=%s";

    String getUrlString(@NonNull String advertisingId) {
        BVSDK bvsdk = BVSDK.getInstance();
        String baseUrl = bvsdk.getBvWorkerData().getRootApiUrls().getBazaarvoiceApiRootUrl();
        BVUserProvidedData bvUserProvidedData = bvsdk.getBvUserProvidedData();
        String pinApiKey = bvUserProvidedData.getBvApiKeys().getApiKeyPin();
        String clientId = bvUserProvidedData.getClientId();

        String formattedAdId = String.format("%s%s", "magpie_idfa_", advertisingId);

        return String.format(RELATIVE_URL_TEMPLATE, baseUrl, pinApiKey, formattedAdId, clientId);
    }
}
