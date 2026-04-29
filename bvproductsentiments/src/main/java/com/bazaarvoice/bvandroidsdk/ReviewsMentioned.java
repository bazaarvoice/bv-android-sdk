package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ReviewsMentioned {
    @SerializedName("total")
    private float total;
    @SerializedName("positive")
    private float positive;
    @SerializedName("negative")
    private float negative;
    @SerializedName("incentivized")
    private float incentivized;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getPositive() {
        return positive;
    }

    public void setPositive(float positive) {
        this.positive = positive;
    }

    public float getNegative() {
        return negative;
    }

    public void setNegative(float negative) {
        this.negative = negative;
    }

    public float getIncentivized() {
        return incentivized;
    }

    public void setIncentivized(float incentivized) {
        this.incentivized = incentivized;
    }
}
