/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

class RecommendationStats {

    @SerializedName("RKI")
    private long rki;

    @SerializedName("RKB")
    private long rkb;

    @SerializedName("RKP")
    private long rkp;

    @SerializedName("RKC")
    private long rkc;

    @SerializedName("RKT")
    private long rkt;

    @SerializedName("RKR")
    private long rkr;

    @SerializedName("RKN")
    private long rkn;

    private boolean activeUser;

    public long getRki() {
        return rki;
    }

    public long getRkb() {
        return rkb;
    }

    public long getRkp() {
        return rkp;
    }

    public long getRkc() {
        return rkc;
    }

    public long getRkt() {
        return rkt;
    }

    public long getRkr() { return rkr; }

    public long getRkn() { return rkn; }

    public boolean isActiveUser() {
        return activeUser;
    }
}
