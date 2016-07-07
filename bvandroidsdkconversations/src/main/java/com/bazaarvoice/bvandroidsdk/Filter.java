package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class Filter {

    static final String paramKey = "Filter";

    private final String queryString;
    private final UGCOption option;
    private final EqualityOperator equalityOperator;
    private final List<String> filterValues;

    Filter(UGCOption option, EqualityOperator equalityOperator, String value) {
        this.option = option;
        this.equalityOperator = equalityOperator;
        this.filterValues = new ArrayList<>();
        filterValues.add(value);
        this.queryString = this.toString();
    }

    Filter(UGCOption option, EqualityOperator equalityOperator, List<String> values) {
        this.option = option;
        this.equalityOperator = equalityOperator;
        this.filterValues = values;
        Collections.sort(this.filterValues);
        this.queryString = this.toString();
    }

     public String toString() {
        Collections.sort(filterValues);
        return String.format("Filter=%s:%s:%s",option.getKey(), equalityOperator.getKey(), StringUtils.componentsSeparatedByWithEscapes(filterValues, ","));
    }

    String getQueryString() {
        return queryString;
    }

    enum Type implements UGCOption{
        Id("Id"),
        ProductId("ProductId"),
        AverageOverallRating("AverageOverallRating"),
        CategoryAncestorId("CategoryAncestorId"),
        CategoryId("CategoryId"),
        IsActive("IsActive"),
        IsDisabled("IsDisabled"),
        LastAnswerTime("LastAnswerTime"),
        LastQuestionTime("LastQuestionTime"),
        LastReviewTime("LastReviewTime"),
        LastStoryTime("LastStoryTime"),
        Name("Name"),
        RatingsOnlyReviewCount("RatingsOnlyReviewCount"),
        TotalAnswerCount("TotalAnswerCount"),
        TotalQuestionCount("TotalQuestionCount"),
        TotalReviewCount("TotalReviewCount"),
        TotalStoryCount("TotalStoryCount");

        private final String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        ReviewOptions.Sort a = ReviewOptions.Sort.CampaignId;
    }
}
