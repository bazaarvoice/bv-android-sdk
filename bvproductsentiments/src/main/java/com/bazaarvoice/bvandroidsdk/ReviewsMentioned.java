package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ReviewsMentioned {
    @SerializedName("total")
    private int total;
    @SerializedName("positive")
    private int positive;
    @SerializedName("negative")
    private int negative;
    @SerializedName("incentivized")
    private int incentivized;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public int getIncentivized() {
        return incentivized;
    }

    public void setIncentivized(int incentivized) {
        this.incentivized = incentivized;
    }
}
