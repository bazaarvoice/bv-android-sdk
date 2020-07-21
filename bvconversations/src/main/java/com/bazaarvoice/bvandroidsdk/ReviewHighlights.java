package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ReviewHighlights{

    @SerializedName("positive")
    private Map<String, ReviewHighlight> positivesMap;

    @SerializedName("negative")
    private Map<String, ReviewHighlight> negativesMap;

    private transient List<ReviewHighlight> positives;
    private transient List<ReviewHighlight> negatives;


    public List<ReviewHighlight> getPositives() {
        if (positives == null) {
            this.positives = processContent(positivesMap);
        }
        return this.positives;
    }

    public List<ReviewHighlight> getNegatives() {
        if (negatives == null) {
         negatives = processContent(negativesMap);
        }
        return this.negatives;
    }

    //Convert from Map to List
    @NonNull
    private List<ReviewHighlight> processContent(@Nullable Map<String, ReviewHighlight> contents) { //check non null
        List<ReviewHighlight> contentList = new ArrayList<>();
        if (contents != null) {
            for (Map.Entry<String, ReviewHighlight> entry : contents.entrySet()) {
                entry.getValue().title = entry.getKey();
                contentList.add(entry.getValue());
            }
        }
        // Sort in the descending order of mentionsCount
        Collections.sort(contentList, (s1, s2) ->
                Integer.compare(s2.mentionsCount, s1.mentionsCount));
        return contentList;
    }
}