package com.bazaarvoice.bvandroidsdk;

import android.graphics.Point;

import java.util.List;

/**
 * Interface between View displaying a grid of
 * {@link CurationsFeedItem}s and fetching/paging
 */
interface CurationsInfiniteContract {
  interface View {
    void addFeedItems(List<CurationsFeedItem> feedItems);
  }

  interface ViewProps {
    // ui properties
    Point getScreenDimens();
    Point getViewDimens();
    // custom attributes
    int getCurationPageSize();
    int getCurationSpanCount();
    boolean isCurationReverseLayout();
    int getCurationOrientation();
    int getCurationCellWidthRatio();
    int getCurationCellHeightRatio();
    int getCurationMaxFeedItemCount();
  }

  interface Presenter {
    void start();
    void onScroll(int delta, int lastVisibleFeedItemIndex);
  }
}
