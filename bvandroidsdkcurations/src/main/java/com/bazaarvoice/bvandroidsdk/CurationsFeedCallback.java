package com.bazaarvoice.bvandroidsdk;

import java.util.List;

/**
 * Created by Bazaarvoice on 4/1/16.
 */

/**
 * Callback used to handle successful and unsuccessful Curations feed requests
 */
public interface CurationsFeedCallback {

    /**
     * Called whenever a curations feed is successfully loaded.
     * @param feedItems The resulting CurationsFeedItems.
     */
    public void onSuccess(List<CurationsFeedItem> feedItems);

    /**
     * Called whenever a curations feed in unsuccessfully loaded.
     * @param throwable Description of the error that occurred.
     */
    public void onFailure(Throwable throwable);
}
