package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

public class BVProcuctTest extends BVBaseTest {

    @Test
    public void shouldParseWithoutError() throws Exception {
        BVProduct product = parseJsonResourceFile("bvproduct.json", BVProduct.class, gson);
        assertNotNull(product);
        assertNotNull(product.getCategoryIds());
        assertFalse(product.isImpressed());
        assertEquals(4.3f, product.getAverageRating());
        assertEquals(2, product.getCategoryIds().size());
        assertEquals(4, product.getNumReviews());
        assertEquals("http://example.com", product.getDisplayImageUrl());
        assertEquals("http://example.com", product.getProductPageUrl());
        assertEquals("product4", product.getId());
        assertEquals("4", product.getCategoryId());
    }

    @Test
    public void voidShouldParseBVReview() throws Exception {
        BVProduct product = parseJsonResourceFile("bvproduct.json", BVProduct.class, gson);
        BVReview review = product.getReview();
        assertNotNull(product);
        assertNotNull(review);
        assertNotNull(review.toString());
        assertEquals("ReviewTitle", review.getTitle());
        assertEquals("faklakjdadkldjknqnjdnqw", review.getText());
        assertEquals("test", review.getAuthor());
    }

}
