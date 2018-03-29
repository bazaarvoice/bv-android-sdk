package com.bazaarvoice.bvandroidsdk;

import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class BVLocaleServiceManager {

    // Private

    private static final BVLocaleServiceManager singleton = new BVLocaleServiceManager();

    private static final String ANALYTICS_ROOT_URL_DEFAULT_PRODUCTION =
            "https://network.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_DEFAULT_STAGING =
            "https://network-stg.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_EU_PRODUCTION =
            "https://network-eu.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_EU_STAGING =
            "https://network-eu-stg.bazaarvoice.com/";

    private static final Map<ServiceConfiguration, String> analyticDefaultServiceMap =
            createDefaultServiceMap();

    private static Map<ServiceConfiguration, String> createDefaultServiceMap() {
        Map<ServiceConfiguration, String> defaultMap = new HashMap<ServiceConfiguration, String>() {{
            put(ServiceConfiguration.PRODUCTION, ANALYTICS_ROOT_URL_DEFAULT_PRODUCTION);
            put(ServiceConfiguration.STAGING, ANALYTICS_ROOT_URL_DEFAULT_STAGING);
        }};
        return defaultMap;
    }

    private static final Map<ServiceConfiguration, String> analyticEUServiceMap =
            createEUServiceMap();

    private static Map<ServiceConfiguration, String> createEUServiceMap() {
        Map<ServiceConfiguration, String> euMap = new HashMap<ServiceConfiguration, String>() {{
            put(ServiceConfiguration.PRODUCTION, ANALYTICS_ROOT_URL_EU_PRODUCTION);
            put(ServiceConfiguration.STAGING, ANALYTICS_ROOT_URL_EU_STAGING);
        }};
        return euMap;
    }

    private static final Map<ServiceType, Map<ServiceConfiguration, String>> analyticServiceMap =
            createAnalyticServiceMap();

    private static Map<ServiceType, Map<ServiceConfiguration, String>> createAnalyticServiceMap() {
        Map<ServiceType, Map<ServiceConfiguration, String>> serviceMap =
                new HashMap<ServiceType, Map<ServiceConfiguration, String>>() {{
                    put(ServiceType.DEFAULT, analyticDefaultServiceMap);
                    put(ServiceType.EU, analyticEUServiceMap);
                }};
        return serviceMap;
    }

    private static final Map<String, ServiceType> analyticMap = createAnalyticMap();

    private static Map<String, ServiceType> createAnalyticMap() {
        Map<String, ServiceType> serviceMap = new HashMap<String, ServiceType>() {{
            put("AT", ServiceType.EU); // Austria
            put("040", ServiceType.EU);
            put("BE", ServiceType.EU); // Belgium
            put("056", ServiceType.EU);
            put("BG", ServiceType.EU); // Bulgaria
            put("100", ServiceType.EU);
            put("CH", ServiceType.EU); // Switzerland
            put("756", ServiceType.EU);
            put("CY", ServiceType.EU); // Republic of Cyprus
            put("196", ServiceType.EU);
            put("CZ", ServiceType.EU); // Czech Republic
            put("203", ServiceType.EU);
            put("DE", ServiceType.EU); // Germany
            put("276", ServiceType.EU);
            put("DK", ServiceType.EU); // Denmark
            put("208", ServiceType.EU);
            put("ES", ServiceType.EU); // Spain
            put("724", ServiceType.EU);
            put("EE", ServiceType.EU); // Estonia
            put("233", ServiceType.EU);
            put("FI", ServiceType.EU); // Finland
            put("246", ServiceType.EU);
            put("FR", ServiceType.EU); // France
            put("250", ServiceType.EU);
            put("GB", ServiceType.EU); // Great Britain / UK
            put("826", ServiceType.EU);
            put("GR", ServiceType.EU); // Greece
            put("300", ServiceType.EU);
            put("HR", ServiceType.EU); // Croatia
            put("191", ServiceType.EU);
            put("HU", ServiceType.EU); // Hungary
            put("348", ServiceType.EU);
            put("IE", ServiceType.EU); // Ireland
            put("372", ServiceType.EU);
            put("IS", ServiceType.EU); // Iceland
            put("352", ServiceType.EU);
            put("IT", ServiceType.EU); // Italy
            put("380", ServiceType.EU);
            put("LI", ServiceType.EU); // Liechtenstein
            put("438", ServiceType.EU);
            put("LT", ServiceType.EU); // Lithuania
            put("440", ServiceType.EU);
            put("LU", ServiceType.EU); // Luxembourg
            put("442", ServiceType.EU);
            put("LV", ServiceType.EU); // Latvia
            put("428", ServiceType.EU);
            put("MT", ServiceType.EU); // Malta
            put("470", ServiceType.EU);
            put("NL", ServiceType.EU); // Netherlands
            put("528", ServiceType.EU);
            put("NO", ServiceType.EU); // Norway
            put("578", ServiceType.EU);
            put("PL", ServiceType.EU); // Poland
            put("616", ServiceType.EU);
            put("PT", ServiceType.EU); // Portugal
            put("620", ServiceType.EU);
            put("RO", ServiceType.EU); // Romania
            put("642", ServiceType.EU);
            put("SE", ServiceType.EU); // Sweden
            put("752", ServiceType.EU);
            put("SI", ServiceType.EU); // Slovenia
            put("705", ServiceType.EU);
            put("SK", ServiceType.EU); // Slovakia
            put("703", ServiceType.EU);
        }};
        return serviceMap;
    }

    private enum ServiceConfiguration {
        PRODUCTION,
        STAGING
    }

    private enum ServiceType {
        DEFAULT,
        EU
    }

    private BVLocaleServiceManager() {
    }

    private String resourceForAnalytics(Locale locale, boolean isStaging) {

        ServiceConfiguration config =
                (isStaging) ? ServiceConfiguration.STAGING : ServiceConfiguration.PRODUCTION;

        //
        // From the documentation on Locale::getCountry():
        //
        // Returns the country/region code for this locale, which should either be the empty
        // string, an uppercase ISO 3166 2-letter code, or a UN M.49 3-digit code.
        ServiceType serviceType =
                (analyticMap.containsKey(locale.getCountry())) ?
                        analyticMap.get(locale.getCountry()) : ServiceType.DEFAULT;

        Map<ServiceConfiguration, String> serviceMap =
                (null != analyticServiceMap.get(serviceType)) ?
                        analyticServiceMap.get(serviceType) : analyticDefaultServiceMap;

        return (null != serviceMap.get(config)) ?
                serviceMap.get(config) : ANALYTICS_ROOT_URL_DEFAULT_STAGING;
    }

    // Public

    public enum Service {
        ANALYTICS
    }

    public static BVLocaleServiceManager getInstance() {
        return singleton;
    }

    public String resourceFor(
            Service service, Locale locale, boolean isStaging) throws IllegalArgumentException {

        switch (service) {
            case ANALYTICS:
                return resourceForAnalytics(locale, isStaging);
            default:
                throw new IllegalArgumentException("No service exists for locale resource.");
        }
    }
}
