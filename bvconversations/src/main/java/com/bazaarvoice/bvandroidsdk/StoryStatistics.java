package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

class StoryStatistics {

    @SerializedName("Name")
    String name;
    @SerializedName("TotalStoryCount")
    int totalStoryCount;
    @SerializedName("NotHelpFulVoteCount")
    int notHelpFulVoteCount;
    @SerializedName("HelpFulVoteCount")
    int helpFulVoteCount;
    @SerializedName("FeaturedStoryCount")
    int featuredStoryCount;
    @SerializedName("TagDistribvutionOrder")
    List<String> tagDistribvutionOrder;
    @SerializedName("FirstSubmissionTime")
    Date firstSubmissionTime;
    @SerializedName("LastSubmissionTime")
    Date lastSubmissionTime;
    @SerializedName("TagDistribution")
    TagDistribution tagDistribution;

    public int getTotalStoryCount() {
        return totalStoryCount;
    }

    @Nullable public Date getLastSubmissionTime() {
        return lastSubmissionTime;
    }

    public int getNotHelpFulVoteCount() {
        return notHelpFulVoteCount;
    }

    @Nullable public List<String> getTagDistribvutionOrder() {
        return tagDistribvutionOrder;
    }

    @Nullable public Date getFirstSubmissionTime() {
        return firstSubmissionTime;
    }

    public int getHelpFulVoteCount() {
        return helpFulVoteCount;
    }

    public int getFeaturedStoryCount() {
        return featuredStoryCount;
    }

    @Nullable public String getName() {
        return name;
    }

    @Nullable public TagDistribution getTagDistribution() {
        return tagDistribution;
    }

    private class TagDistribution {

        Topic topic;

        @Nullable public Topic getTopic() {
            return topic;
        }
    }

    private class Topic {
        List<TopicValue> values;

        public List<TopicValue> getValues() {
            return values;
        }
    }

    private class TopicValue {
        String value;
        int count;

        @Nullable public String getValue() {
            return value;
        }

        public int getCount() {
            return count;
        }
    }
}
