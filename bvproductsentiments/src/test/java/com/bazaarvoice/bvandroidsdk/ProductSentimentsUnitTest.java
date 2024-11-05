package com.bazaarvoice.bvandroidsdk;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProductSentimentsUnitTest extends BVBaseTest {

    @Test
    public void testSummarisedFeaturesResponse() throws Exception {
        SummarisedFeaturesResponse response = parseJsonResourceFile("summarised_features_response.json", SummarisedFeaturesResponse.class, gson);
        // Test "bestFeatures" and "worstFeatures" data
        for (BestFeature feature : response.getBestFeatures()) {
            assertNotNull(feature.getFeatureId());
            assertNotNull(feature.getFeature());
            assertNotNull(feature.getReviewsMentioned());
            assertEquals(90, feature.getReviewsMentioned().getTotal());
            assertEquals(84, feature.getReviewsMentioned().getPositive());
            assertEquals(5.5, feature.getAverageRatingReviews().getPositive(), 0.01);
        }

        for (WorstFeature feature : response.getWorstFeatures()) {
            assertNotNull(feature.getFeatureId());
            assertNotNull(feature.getFeature());
            assertNotNull(feature.getReviewsMentioned());
            assertEquals(90, feature.getReviewsMentioned().getTotal());
            assertEquals(84, feature.getReviewsMentioned().getPositive());
            assertEquals(5.5, feature.getAverageRatingReviews().getPositive(), 0.01);
        }
    }

    @Test
    public void testExpressionsSentimentResponse() throws Exception {
        ExpressionsSentimentResponse response = parseJsonResourceFile("retrieve_expressions_response.json", ExpressionsSentimentResponse.class, gson);
        // Test "bestFeatures" and "worstFeatures" data
        assertNotNull(response.getExpressions());
        assertEquals(3, response.getExpressions().size());
        assertEquals("comforted", response.getExpressions().get(0));
        assertEquals("relieved", response.getExpressions().get(1));
        assertEquals("tranquility", response.getExpressions().get(2));
    }

    @Test
    public void testSummarisedFeaturesQuotesResponse() throws Exception {
        SummarisedFeaturesQuotesResponse response = parseJsonResourceFile("summarised_features_quotes_response.json", SummarisedFeaturesQuotesResponse.class, gson);

        // Test "quotes" data
        assertNotNull(response.getQuotes());
        assertEquals(2, response.getQuotes().size());

        for (Quote quote : response.getQuotes()) {
            assertNotNull(quote.getQuoteId());
            assertEquals("quoteId", quote.getQuoteId());

            assertNotNull(quote.getText());
            assertEquals("Lovely fitting look nice and the quality of the leggings is lovely they wash well too!", quote.getText());

            assertNotNull(quote.getEmotion());
            assertEquals("joy", quote.getEmotion());

            assertNotNull(quote.getReviewRating());
            assertEquals(5, quote.getReviewRating());

            assertNotNull(quote.getReviewId());
            assertEquals("reviewId", quote.getReviewId());

            assertNotNull(quote.getReviewedAt());
            assertEquals("2021-05-03", quote.getReviewedAt());

            assertNotNull(quote.getTranslatedText());
            assertEquals("Lovely fitting look nice and the quality of the leggings is lovely they wash well too!", quote.getTranslatedText());

            assertNotNull(quote.getNativeLanguage());
            assertEquals("en-US", quote.getNativeLanguage());

            assertNotNull(quote.isIncentivised());
            assertFalse(quote.isIncentivised());

            assertNotNull(quote.getReviewType());
            assertEquals("NATIVE", quote.getReviewType());
        }
    }


@Test
public void testQuotesSentimentResponse() throws Exception {
    QuotesSentimentResponse response = parseJsonResourceFile("retrieve_quotes_response.json", QuotesSentimentResponse.class, gson);

    // Test "quotes" data
    assertNotNull(response.getQuotes());
    assertEquals(2, response.getQuotes().size());

    for (Quote quote : response.getQuotes()) {
        assertNotNull(quote.getId());
        assertEquals("Id", quote.getId());

        assertNotNull(quote.getText());
        assertEquals("Lovely fitting look nice and the quality of the leggings is lovely they wash well too!", quote.getText());

        assertNotNull(quote.getEmotion());
        assertEquals("joy", quote.getEmotion());

        assertNotNull(quote.getReviewRating());
        assertEquals(5, quote.getReviewRating());

        assertNotNull(quote.getReviewId());
        assertEquals("reviewId", quote.getReviewId());

        assertNotNull(quote.getReviewedAt());
        assertEquals("2021-05-03", quote.getReviewedAt());

        assertNotNull(quote.getTranslatedText());
        assertEquals("Lovely fitting look nice and the quality of the leggings is lovely they wash well too!", quote.getTranslatedText());

        assertNotNull(quote.getNativeLanguage());
        assertEquals("en-US", quote.getNativeLanguage());

        assertNotNull(quote.isIncentivised());
        assertFalse(quote.isIncentivised());

        assertNotNull(quote.getReviewType());
        assertEquals("NATIVE", quote.getReviewType());
    }
}

    @Test
    public void testFeaturesSentimentResponse() throws Exception {
        FeaturesSentimentResponse response = parseJsonResourceFile("summarised_features_with_features.json", FeaturesSentimentResponse.class, gson);

        // Test "features" data
        assertNotNull(response.getFeatures());
        assertEquals(10, response.getFeatures().size());

        List<String> expectedFeatures = Arrays.asList("comfort", "satisfaction", "quality", "fit", "purchase", "appearance", "size", "material", "price", "color");
        List<String> expectedNativeFeatures = Arrays.asList("consuelo", "satisfacción", "calidad", "caber", "compra", "apariencia", "tamaño", "material", "precio", "color");

        for (int i = 0; i < response.getFeatures().size(); i++) {
            FeatureSentiment feature = response.getFeatures().get(i);

            assertNotNull(feature.getFeature());
            assertEquals(expectedFeatures.get(i), feature.getFeature());

            assertNotNull(feature.getNativeFeature());
            assertEquals(expectedNativeFeatures.get(i), feature.getNativeFeature());
        }
    }
}
