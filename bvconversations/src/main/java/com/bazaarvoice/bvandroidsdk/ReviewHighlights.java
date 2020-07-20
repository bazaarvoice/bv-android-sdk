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


    protected Map<String, ReviewHighlight> getpositivesMap() {
        return positivesMap;
    }
    protected Map<String, ReviewHighlight> getnegativesMap() {
        return negativesMap;
    }

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

    //Converting map to List and desending list to descending order wrt mentionsCount
    @NonNull
    private List<ReviewHighlight> processContent(@Nullable Map<String, ReviewHighlight> contents) { //check non null
        List<ReviewHighlight> contentList = new ArrayList<>();
        if (contents != null) {
            for (Map.Entry<String, ReviewHighlight> entry : contents.entrySet()) {
                entry.getValue().title = entry.getKey();
                contentList.add(entry.getValue());
            }
        }
        Collections.sort(contentList, (s1, s2) ->
                Integer.compare(s2.mentionsCount, s1.mentionsCount));
        return contentList;
    }
}