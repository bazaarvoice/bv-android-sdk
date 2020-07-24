package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReviewHighlightsResponse  extends ConversationsDisplayResponse {

   @SerializedName("subjects") private ReviewHighlights reviewHighlights;
   @SerializedName("error") private String error;
   private transient List<Error> errors;


    public  ReviewHighlights getReviewHighlights() {
        return reviewHighlights;
    }

    public Boolean getHasErrors() {
        return error!= null ;
    }

    public List<Error> getErrors() {
        if(errors == null){
            Error reviewHighlightsError = new Error(error,null);
            errors = new ArrayList<Error>(Arrays.asList(reviewHighlightsError));
        }
        return errors;
    }
}
