package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okio.BufferedSource;
import okio.Okio;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

public class ShopperProfileTest extends BVBaseTest {

    @Test
    public void createBVShopperProfile() throws Exception {

        ShopperProfile shopperProfile = parseJsonResourceFile("full_profile.json", ShopperProfile.class, gson);
        Profile profile = shopperProfile.getProfile();
        assertNotNull(shopperProfile);
        assertNotNull(profile);
        assertEquals("HIGH", profile.getInterests().get("stuff1").getValue());
        assertEquals("MED", profile.getBrands().get("company1").getValue());
        assertNotNull(profile.getRecommendationStats());
        assertNotNull(profile.getBrands());
        assertNotNull(profile.getInterests());
        assertEquals("default_15", profile.getPlan());
        assertEquals("90332bdf", shopperProfile.getApiVersion());

    }

    @Test
    public void shouldReturnValidTargetedKeywords() throws Exception {
        ShopperProfile shopperProfile = parseJsonResourceFile("full_profile.json", ShopperProfile.class, gson);
        Profile profile = shopperProfile.getProfile();
        Map<String, String> targetedKeyWords = new HashMap<>();
        targetedKeyWords.put("interests", "stuff1_HIGH stuff2_LOW stuff3_MED");
        targetedKeyWords.put("brands", "company1_MED company2_LOW company3_HIGH");
        assertEquals(2, profile.getTargetingKeywords().size());
        assertEquals(targetedKeyWords, profile.getTargetingKeywords());
    }

    @Test
    public void shouldReturnRecommendedProducts() throws Exception {
        ShopperProfile shopperProfile = parseJsonResourceFile("full_profile.json", ShopperProfile.class, gson);
        Profile profile = shopperProfile.getProfile();
        assertNotNull(profile.getRecommendedProducts());
        assertEquals(10, profile.getRecommendedProducts().size());
    }

    @Test
    public void shopperProfileToStringShouldNotBeNull() throws Exception {
        ShopperProfile shopperProfile = parseJsonResourceFile("full_profile.json", ShopperProfile.class, gson);
        assertNotNull(shopperProfile.toString());
    }

    @Test
    public void profileRecommendationStatsShouldParseCorrectly() throws Exception {
        ShopperProfile shopperProfile = parseJsonResourceFile("full_profile.json", ShopperProfile.class, gson);
        Profile profile = shopperProfile.getProfile();
        RecommendationStats stats = profile.getRecommendationStats();
        assertNotNull(stats);
        assertEquals(0, stats.getRki());
        assertEquals(0, stats.getRkb());
        assertEquals(0, stats.getRkp());
        assertEquals(0, stats.getRkr());
        assertEquals(0, stats.getRkc());
        assertEquals(0, stats.getRkt());
        assertEquals(0, stats.getRkn());
    }


}
