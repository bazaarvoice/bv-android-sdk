package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class RecommendationsEmbeddedPageViewSchema extends EmbeddedPageViewSchema {

    private static final String bvProduct = "Recommendations";
    private static final String source = "recommendation-mob";
    private static final String PRODUCT_ID = "productId";
    private static final String CATEGORY_ID = "categoryId";
    private static final String NUM_RECOMMENDATIONS = "numRecommendations";

    RecommendationsEmbeddedPageViewSchema(Builder builder) {
        super(builder.magpieMobileAppPartialSchema, builder.reportingGroup, bvProduct, source);
        addKeyVal(PRODUCT_ID, builder.productId);
        addKeyVal(CATEGORY_ID, builder.categoryId);
        if (builder.hasRecs) {
            addKeyVal(NUM_RECOMMENDATIONS, builder.numRecommendations);
        }
    }

    public static class Builder {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;
        ReportingGroup reportingGroup;
        String productId;
        String categoryId;
        int numRecommendations;
        boolean hasRecs = false;

        public Builder(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, ReportingGroup reportingGroup) {
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
            this.reportingGroup = reportingGroup;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder numRecommendations(int numRecommendations) {
            this.numRecommendations = numRecommendations;
            this.hasRecs = true;
            return this;
        }

        public RecommendationsEmbeddedPageViewSchema build() {
            return new RecommendationsEmbeddedPageViewSchema(this);
        }
    }
}
