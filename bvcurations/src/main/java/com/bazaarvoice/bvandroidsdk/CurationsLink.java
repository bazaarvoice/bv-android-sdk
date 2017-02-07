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

/**
 * Link to source of social media content
 */
public class CurationsLink {

    protected String domain;
    protected String display_url;
    protected String short_url;
    protected String favicon;
    protected String url;

    public CurationsLink(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public String getDisplayUrl() {
        return display_url;
    }

    public String getShortUrl() {
        return short_url;
    }

    public String getFavicon() {
        return favicon;
    }

}
