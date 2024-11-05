package com.bazaarvoice.bvandroidsdk;
import com.google.gson.annotations.SerializedName;

public class AverageRatingReviews  {

    @SerializedName("positive")
    private double positive;
    @SerializedName("negative")
    private int negative;
    @SerializedName("incentivized")
    private int incentivized;

    public double getPositive() {
        return positive;
    }

    public void setPositive(double positive) {
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
