/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * Response for posting curations content
 * @deprecated
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