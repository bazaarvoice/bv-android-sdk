package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.*;

/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
class RecommendationAttributesPartialSchema extends BvPartialSchema {

    private static final String KEY_RKC = "RKC";
    private static final String KEY_RKT = "RKT";
    private static final String KEY_RKP = "RKP";
    private static final String KEY_RKI = "RKI";
    private static final String KEY_RKB = "RKB";
    private static final String KEY_RS = "RS";
    private static final String KEY_SPONSORED = "sponsored";

    private long rkc;
    private long rkt;
    private long rkp;
    private long rki;
    private long rkb;
    private String rs;
    private boolean sponsored;

    private RecommendationAttributesPartialSchema(Builder builder) {
        rkc = builder.rkc;
        rkt = builder.rkt;
        rkp = builder.rkp;
        rki = builder.rki;
        rkb = builder.rkb;
        rs = builder.rs;
        sponsored = builder.sponsored;
    }


    @Override
    public void addPartialData(Map<String, Object> dataMap) {
        mapPutSafe(dataMap, KEY_RKC, rkc);
        mapPutSafe(dataMap, KEY_RKT, rkt);
        mapPutSafe(dataMap, KEY_RKP, rkp);
        mapPutSafe(dataMap, KEY_RKI, rki);
        mapPutSafe(dataMap, KEY_RKB, rkb);
        mapPutSafe(dataMap, KEY_RS, rs);
        mapPutSafe(dataMap, KEY_SPONSORED, sponsored);
    }

    public static final class Builder {
        private long rkc;
        private long rkt;
        private long rkp;
        private long rki;
        private long rkb;
        private String rs;
        private boolean sponsored;

        public Builder() {
        }

        public Builder rkc(long val) {
            rkc = val;
            return this;
        }

        public Builder rkt(long val) {
            rkt = val;
            return this;
        }

        public Builder rkp(long val) {
            rkp = val;
            return this;
        }

        public Builder rki(long val) {
            rki = val;
            return this;
        }

        public Builder rkb(long val) {
            rkb = val;
            return this;
        }

        public Builder rs(String val) {
            rs = val;
            return this;
        }

        public Builder sponsored(boolean val) {
            sponsored = val;
            return this;
        }

        public RecommendationAttributesPartialSchema build() {
            return new RecommendationAttributesPartialSchema(this);
        }
    }
}
