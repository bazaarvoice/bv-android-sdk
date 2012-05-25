package com.requiem.bazaarvoice;

/**
 * User: gary
 * Date: 4/9/12
 * Time: 9:35 PM
 *
 * Enum for all the different types of requests we can make to BazaarVoice
 */
public enum RequestType {
    REVIEWS("reviews", "submitreview"),
    QUESTIONS("questions", "submitquestion"),
    ANSWERS("answers", "submitanswer"),
    STORIES("stories", "submitstory"),
    REVIEW_COMMENTS("reviewcomments", "submitreviewcomment"),
    STORY_COMMENTS("storycomments", "submitstorycomment"),
    PROFILES("authors", "submitauthor"),
    PHOTOS(null, "uploadphoto"),
    VIDEOS(null, "uploadvideo"),
    PRODUCTS("products", null),
    CATEGORIES("categories", null),
    STATISTICS("statistics", null);

    private String displayName;
    private String submissionName;

    RequestType(String displayName, String submissionName) {
        this.displayName = displayName;
        this.submissionName = submissionName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getSubmissionName() {
        return submissionName;
    }



}
