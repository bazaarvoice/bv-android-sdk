/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

import java.util.List;

public class DemoQuestionsCache extends DemoCache<List<Question>> {

    private static DemoQuestionsCache instance;

    private DemoQuestionsCache() {
        super();
    }

    public static DemoQuestionsCache getInstance() {
        if (instance == null) {
            instance = new DemoQuestionsCache();
        }
        return instance;
    }

    @Override
    protected String getKey(List<Question> questions) {
        return null;
    }
}
