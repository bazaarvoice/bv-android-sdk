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
 * Container for the different metadata around where an image
 * is hosted.
 */
public class CurationsPhoto {

    public CurationsPhoto(String remoteUrl){
        this.remote_url = remoteUrl;
    }

    protected Integer id;
    protected String origin;
    protected String permalink;
    protected String token;
    protected String display_url;
    protected String image_service_url;
    protected String role;
    protected String url;
    protected String local_url;

    protected String remote_url;

    public Integer getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getToken() {
        return token;
    }

    public String getDisplayUrl() {
        return display_url;
    }

    public String getImageServiceUrl() {
        return image_service_url;
    }

    public String getRole() {
        return role;
    }

    public String getUrl() {
        return url;
    }

    public String getLocalUrl() {
        return local_url;
    }

    public String getRemote_url() {
        return remote_url;
    }

}
