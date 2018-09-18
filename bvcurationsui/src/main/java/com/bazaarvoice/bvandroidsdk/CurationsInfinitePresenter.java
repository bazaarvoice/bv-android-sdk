package com.bazaarvoice.bvandroidsdk;

import android.graphics.Point;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/**
 * Manages retrieval of data, and business logic of the view
 */
class CurationsInfinitePresenter implements CurationsInfiniteContract.Presenter {
  // region Properties

  private static final String TAG = "CurInfPresenter";
  private static final float LOAD_MORE_THRESHOLD_PERCENTAGE = 0.2f;

  private final CurationsInfiniteContract.View view;
  private final CurationsInfiniteContract.ViewProps viewProps;
  private final BVCurations curations;
  private final CurationsFeedRequest request;
  private final CurationsInfiniteRecyclerView.OnPageLoadListener pageLoadListener;
  private PagingFeedCallback pagingCb;
  private List<CurationsFeedItem> feedItems = new ArrayList<>();
  private int pageIndex = 0;
  private boolean updateInProgress = false;
  private boolean loadedAllFeedItems = false;

  // endregion

  // region Constructor

  CurationsInfinitePresenter(final CurationsInfiniteContract.View view, final CurationsInfiniteContract.ViewProps viewProps, final BVCurations curations, @Nullable final CurationsInfiniteRecyclerView.OnPageLoadListener pageLoadListener, final CurationsFeedRequest request) {
    this.view = view;
    this.viewProps = viewProps;
    this.curations = curations;
    this.pageLoadListener = pageLoadListener;
    this.request = request;
  }

  // endregion

  // region Presenter Implementation

  @Override
  public void start() {
    int initialPageSize = getInitialPageSize(viewProps);
    loadMore(initialPageSize);
  }

  @Override
  public void onScroll(int delta, int lastVisibleFeedItemIndex) {
    if (shouldLoadMore(delta, lastVisibleFeedItemIndex)) {
      loadMore();
    }
  }

  // endregion

  // region Internal API

  /**
   * @param lastVisibleFeedItemIndex Index of the last visible feed item
   * @return False if we have reached the max number of items. True if we
   * have less feed items offscreen than the threshold. False otherwise.
   */
  private boolean shouldLoadMore(int delta, int lastVisibleFeedItemIndex) {
    // Last request returned empty or less than the expected page size,
    // therefore we are done
    if (loadedAllFeedItems) {
      return false;
    }

    // Reached maximum heap size that is allowed
    if (feedItems.size() >= viewProps.getCurationMaxFeedItemCount()) {
      return false;
    }

    // Must be scrolling down or right
    if (delta <= 0) {
      return false;
    }

    // Don't queue up another load for the current position when an update
    // is in progress
    if (updateInProgress) {
      return false;
    }

    // If there are less items off screen then our threshold then return true
    int offScreenFeedItemCount = (feedItems.size()-1) - lastVisibleFeedItemIndex;
    int spanOffScreenCount = normalizeBySpanCount(offScreenFeedItemCount, viewProps.getCurationSpanCount());
    int loadMoreSpanThreshold = normalizeBySpanCount(getLoadMoreThreshold(viewProps), viewProps.getCurationSpanCount());
    return spanOffScreenCount <= loadMoreSpanThreshold;
  }

  /**
   * Use the provided page size
   */
  private void loadMore() {
    loadMore(viewProps.getCurationPageSize());
  }

  /**
   * Override the provided page size for initial load to fill up screen
   *
   * @param pageSize Number of items necessary to fill up the screen
   */
  private void loadMore(int pageSize) {
    updateInProgress = true;

    final CurationsFeedRequest.Builder builder = request.newBuilder()
        .limit(pageSize);

    if (feedItems.size() > 0) {
      final CurationsFeedItem lastFeedItem  = feedItems.get(feedItems.size()-1);
      builder.before(lastFeedItem.getTimestamp());
    }

    builder.hasPhotoOrVideo(true);

    pagingCb = new PagingFeedCallback(pageIndex++, pageLoadListener, this, pageSize);
    curations.getCurationsFeedItems(request, pagingCb);
  }

  private void updateContent(int expectedPageSize, List<CurationsFeedItem> moreFeedItems) {
    feedItems.addAll(moreFeedItems);
    view.addFeedItems(moreFeedItems);

    if (moreFeedItems.size() < expectedPageSize) {
      loadedAllFeedItems = true;
    }

    updateInProgress = false;
  }

  private void failed() {
    updateInProgress = false;
  }

  /**
   * @param cellCount Number of cells containing one CurationFeedItem
   * @param spanCount Number of rows for vertical scrolling, and columns for
   *                  horizontal scrolling
   * @return How many spans are represented by cellCount
   */
  private static int normalizeBySpanCount(int cellCount, int spanCount) {
    return cellCount / spanCount + (cellCount % spanCount != 0 ? 1 : 0);
  }

  private static int getLoadMoreThreshold(CurationsInfiniteContract.ViewProps viewProps) {
    return (int) (viewProps.getCurationPageSize() * LOAD_MORE_THRESHOLD_PERCENTAGE);
  }

  /**
   * @param screenOrientationDimen Dimension for the screen in the orientation direction.
   *                               e.g. screen height when orientation is vertical, and
   *                               screen width when orientation is horizontal
   * @param cellOrientationDimen Dimension for the cell in the orientation direction.
   *                             e.g. cell height when orientation is vertical, and
   *                             cell width when orientation is horizontal
   * @return Number of cells to more than fill up the screen in the given orientation.
   */
  private static int inverseSpanCount(int screenOrientationDimen, int cellOrientationDimen) {
    // +100 to make sure we go over screen size for scrolling to kick in
    return (100 + screenOrientationDimen) / cellOrientationDimen;
  }

  /**
   * @param viewProps
   * @return Maximum of provided page size and the mininum page size needed to display a full page
   */
  private static int getInitialPageSize(CurationsInfiniteContract.ViewProps viewProps) {
    return max(getPageSizeForFullScreen(viewProps), viewProps.getCurationPageSize());
  }

  /**
   * @return Given spanCount, return y such that spanCount*y is the number of cells that it
   * takes to more than fill up the screen in the given orientation. This is the number that
   * we should use for an initial page size (regardless of what's chosen) to ensure that
   * scrolling becomes enabled, and thus more requests can be queued up.
   */
  private static int getPageSizeForFullScreen(CurationsInfiniteContract.ViewProps viewProps) {
    Point screenDimens = viewProps.getScreenDimens();
    CurationsInfiniteRecyclerView.ImageSize imageSize = CurationsInfiniteRecyclerView.getImageSize(viewProps);
    boolean isVertical = CurationsInfiniteRecyclerView.isVertical(viewProps);
    int screenOrientationDimen = isVertical ? screenDimens.y : screenDimens.x;
    int cellOrientationDimen = isVertical ? imageSize.getHeightPixels() : imageSize.getWidthPixels();
    return inverseSpanCount(screenOrientationDimen, cellOrientationDimen) * viewProps.getCurationSpanCount();
  }

  private abstract static class CurationsPagingFeedCallback implements CurationsFeedCallback {
    private final int pageIndex;

    CurationsPagingFeedCallback(int pageIndex) {
      this.pageIndex = pageIndex;
    }

    protected int getPageIndex() {
      return pageIndex;
    }
  }

  private final static class PagingFeedCallback extends CurationsPagingFeedCallback {
    private final CurationsInfiniteRecyclerView.OnPageLoadListener pageLoadListener;
    private final CurationsInfinitePresenter presenter;
    private final int expectedPageSize;

    PagingFeedCallback(int pageIndex, @Nullable final CurationsInfiniteRecyclerView.OnPageLoadListener pageLoadListener, final CurationsInfinitePresenter presenter, int expectedPageSize) {
      super(pageIndex);
      this.pageLoadListener = pageLoadListener;
      this.presenter = presenter;
      this.expectedPageSize = expectedPageSize;
    }

    @Override
    public void onSuccess(List<CurationsFeedItem> feedItems) {
      presenter.updateContent(expectedPageSize, feedItems);
      if (pageLoadListener != null) {
        pageLoadListener.onPageLoadSuccess(getPageIndex(), feedItems.size());
      }
    }

    @Override
    public void onFailure(Throwable throwable) {
      presenter.failed();
      if (pageLoadListener != null) {
        pageLoadListener.onPageLoadFailure(getPageIndex(), throwable);
      }
    }
  }

  // endregion
}
