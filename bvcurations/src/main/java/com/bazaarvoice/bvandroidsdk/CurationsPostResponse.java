/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * TODO: Describe file here.
 */
public class CurationsPostResponse {
    protected Object status;
    protected String detail;
    protected Integer id;
    protected String remote_url;

    public Integer getStatus() {
        return (Integer)status;
    }

    public String getDetail() {
        return detail;
    }

    public Integer getId() {
        return id;
    }

    public String getRemoteUrl() {
        return remote_url;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    protected Map<String, String> options;
}