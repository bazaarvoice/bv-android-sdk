package com.bazaarvoice.bvandroidsdk;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.mockito.MockitoAnnotations.initMocks;

public class BVLocaleServiceManagerTest {

    private static final String ANALYTICS_ROOT_URL_DEFAULT_PRODUCTION =
            "https://network.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_DEFAULT_STAGING =
            "https://network-stg.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_EU_PRODUCTION =
            "https://network-eu.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_EU_STAGING =
            "https://network-eu-stg.bazaarvoice.com/";

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldRouteToEU() throws Exception {
        List<Locale> localeList = new ArrayList<Locale>() {{
            add(new Locale("", "AT")); // Austria
            add(new Locale("", "040"));
            add(new Locale("", "BE")); // Belgium
            add(new Locale("", "056"));
            add(new Locale("", "BG")); // Bulgaria
            add(new Locale("", "100"));
            add(new Locale("", "CH")); // Switzerland
            add(new Locale("", "756"));
            add(new Locale("", "CY")); // Republic of Cyprus
            add(new Locale("", "196"));
            add(new Locale("", "CZ")); // Czech Republic
            add(new Locale("", "203"));
            add(new Locale("", "DE")); // Germany
            add(new Locale("", "276"));
            add(new Locale("", "DK")); // Denmark
            add(new Locale("", "208"));
            add(new Locale("", "ES")); // Spain
            add(new Locale("", "724"));
            add(new Locale("", "EE")); // Estonia
            add(new Locale("", "233"));
            add(new Locale("", "FI")); // Finland
            add(new Locale("", "246"));
            add(new Locale("", "FR")); // France
            add(new Locale("", "250"));
            add(new Locale("", "GB")); // Great Britain / UK
            add(new Locale("", "826"));
            add(new Locale("", "GR")); // Greece
            add(new Locale("", "300"));
            add(new Locale("", "HR")); // Croatia
            add(new Locale("", "191"));
            add(new Locale("", "HU")); // Hungary
            add(new Locale("", "348"));
            add(new Locale("", "IE")); // Ireland
            add(new Locale("", "372"));
            add(new Locale("", "IS")); // Iceland
            add(new Locale("", "352"));
            add(new Locale("", "IT")); // Italy
            add(new Locale("", "380"));
            add(new Locale("", "LI")); // Liechtenstein
            add(new Locale("", "438"));
            add(new Locale("", "LT")); // Lithuania
            add(new Locale("", "440"));
            add(new Locale("", "LU")); // Luxembourg
            add(new Locale("", "442"));
            add(new Locale("", "LV")); // Latvia
            add(new Locale("", "428"));
            add(new Locale("", "MT")); // Malta
            add(new Locale("", "470"));
            add(new Locale("", "NL")); // Netherlands
            add(new Locale("", "528"));
            add(new Locale("", "NO")); // Norway
            add(new Locale("", "578"));
            add(new Locale("", "PL")); // Poland
            add(new Locale("", "616"));
            add(new Locale("", "PT")); // Portugal
            add(new Locale("", "620"));
            add(new Locale("", "RO")); // Romania
            add(new Locale("", "642"));
            add(new Locale("", "SE")); // Sweden
            add(new Locale("", "752"));
            add(new Locale("", "SI")); // Slovenia
            add(new Locale("", "705"));
            add(new Locale("", "SK")); // Slovakia
            add(new Locale("", "703"));
        }};

        BVLocaleServiceManager localeServiceManager = BVLocaleServiceManager.getInstance();

        for (Locale cursor : localeList) {
            assertEquals(ANALYTICS_ROOT_URL_EU_STAGING,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, true));
            assertEquals(ANALYTICS_ROOT_URL_EU_PRODUCTION,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, false));

            assertNotEquals(ANALYTICS_ROOT_URL_DEFAULT_STAGING,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, true));
            assertNotEquals(ANALYTICS_ROOT_URL_DEFAULT_PRODUCTION,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, false));
        }
    }

    @Test
    public void shouldRouteToDefault() throws Exception {
        List<Locale> localeList = new ArrayList<Locale>() {{
            add(new Locale("", "AF")); // Afghanistan
            add(new Locale("", "BY")); // Belarus
            add(new Locale("", "BJ")); // Benin
            add(new Locale("", "CA")); // Canada
            add(new Locale("", "CL")); // Chile
            add(new Locale("", "CX")); // Christmas Island
            add(new Locale("", "DJ")); // Djibouti
            add(new Locale("", "DO")); // Dominican Republic
            add(new Locale("", "ER")); // Eritrea
            add(new Locale("", "ET")); // Ethiopia
            add(new Locale("", "FJ")); // Fiji
            add(new Locale("", "FO")); // Faroe Islands
            add(new Locale("", "GL")); // Greenland
            add(new Locale("", "GU")); // Guam
            add(new Locale("", "HT")); // Haiti
            add(new Locale("", "HN")); // Honduras
            add(new Locale("", "IN")); // India
            add(new Locale("", "IQ")); // Iraq
            add(new Locale("", "IL")); // Israel
            add(new Locale("", "LA")); // Lao PDR
            add(new Locale("", "LB")); // Lebanon
            add(new Locale("", "LY")); // Libya
            add(new Locale("", "LR")); // Liberia
            add(new Locale("", "MY")); // Malaysia
            add(new Locale("", "NP")); // Nepal
            add(new Locale("", "NI")); // Nicaragua
            add(new Locale("", "PA")); // Panama
            add(new Locale("", "PR")); // Puerto Rico
            add(new Locale("", "RU")); // Russian Federation
            add(new Locale("", "SG")); // Singapore
            add(new Locale("", "SO")); // Somalia
            add(new Locale("", "SY")); // Syria
        }};

        BVLocaleServiceManager localeServiceManager = BVLocaleServiceManager.getInstance();

        for (Locale cursor : localeList) {
            assertNotEquals(ANALYTICS_ROOT_URL_EU_STAGING,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, true));
            assertNotEquals(ANALYTICS_ROOT_URL_EU_PRODUCTION,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, false));

            assertEquals(ANALYTICS_ROOT_URL_DEFAULT_STAGING,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, true));
            assertEquals(ANALYTICS_ROOT_URL_DEFAULT_PRODUCTION,
                    localeServiceManager.resourceFor(BVLocaleServiceManager.Service.ANALYTICS, cursor, false));
        }
    }
}
