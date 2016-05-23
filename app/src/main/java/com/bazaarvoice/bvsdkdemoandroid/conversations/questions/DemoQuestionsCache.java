/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

import java.util.List;

public class DemoQuestionsCache extends DemoCache<List<BazaarQuestion>> {

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
    protected String getKey(List<BazaarQuestion> bazaarQuestions) {
        return null;
    }
}
