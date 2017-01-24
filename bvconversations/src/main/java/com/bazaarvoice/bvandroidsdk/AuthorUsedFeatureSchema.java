package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Custom schema for author/profile used feature events
 */
class AuthorUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "native-mobile-sdk";
    private static final String bvProduct = "Profiles";
    private static final String name = "Default";

    public AuthorUsedFeatureSchema(String profileId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
        super(source, name, bvProduct);
        addPartialSchema(magpieMobileAppPartialSchema);
        addProfileId(profileId);
        addInteraction(false);
    }
}
