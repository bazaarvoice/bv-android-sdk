package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: List of current products that must be chosen for all analytics to
 * show up in reporting correctly
 */
enum MagpieBvProduct {
    RATINGS_AND_REVIEWS("RatingsAndReviews"),
    QUESTIONS_AND_ANSWERS("AskAndAnswer"),
    STORIES("Stories"),
    PROFILES("Profiles"),
    CURATIONS("Curations"),
    SPOTLIGHTS("Spotlights"),
    RECOMMENDATIONS("Recommendations"),
    SELLER_RATINGS_DISPLAY("SellerRatingsDisplay"),
    REVIEW_HIGHLIGHTS("ReviewHighlights");

    private String magpieBvProduct;

    MagpieBvProduct(String magpieBvProduct) {
        this.magpieBvProduct = magpieBvProduct;
    }

    @Override
    public String toString() {
        return magpieBvProduct;
    }
}
