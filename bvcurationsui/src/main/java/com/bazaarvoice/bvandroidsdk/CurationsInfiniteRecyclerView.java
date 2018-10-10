package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.bazaarvoice.bvandroidsdk_curationsui.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Helper View class to manage fetching and displaying a Grid of {@link CurationsFeedItem}s
 * <br/><br/>
 * Make sure to implement your own {@link CurationsImageLoader} to manage the fetching and
 * displaying of images. We use this interface to be able to tie into whatever existing image
 * fetching/caching/manipulating library you are already using.
 * <br/><br/>
 * <h3>Example</h3>
 * <h4>In your layout add</h4>
 * <pre>{@code
 * <com.bazaarvoice.bvandroidsdk.CurationsInfiniteRecyclerView
 *  android:id="@+id/curations_recycler_view"
 *  android:layout_width="match_parent"
 *  android:layout_height="@dimen/curations_height"
 *  app:curationSpanCount="3"
 *  app:curationCellWidthRatio="4"
 *  app:curationCellHeightRatio="3"
 *  app:curationPageSize="20"
 *  app:curationOrientation="horizontal"/>}</pre>
 * <br/>
 * <h4>In your {@link android.app.Activity#onCreate(Bundle)} after inflating your layout, add</h4>
 * <pre>{@code
 * final CurationsFeedRequest request = new CurationsFeedRequest
 *  .Builder(Arrays.asList("__foo__", "__bar__"))
 *  .build();
 * curationsRecyclerView
 *  .setRequest(request)
 *  .setImageLoader(curationsImageLoader)
 *  .setOnPageLoadListener(new CurationsInfiniteRecyclerView.OnPageLoadListener() {
 *    @Override
 *    public void onPageLoadSuccess(int pageIndex, int pageSize) {
 *      dismissLoading();
 *    }
 *    @Override
 *    public void onPageLoadFailure(int pageIndex, Throwable throwable) {
 *      throwable.printStackTrace();
 *      dismissLoading();
 *    }
 *  .load();
 * }
 */
public class CurationsInfiniteRecyclerView extends BVRecyclerView {
  // region Properties

  private static final String WIDGET_ID = "CurationsInfiniteRecyclerView";
  private static final int DEFAULT_MAX_FEED_ITEM_COUNT = 360430;
  private static final int DEFAULT_PAGE_SIZE = 100;
  private static final int DEFAULT_WIDTH_RATIO = 1;
  private static final int DEFAULT_HEIGHT_RATIO = 1;
  private static final int DEFAULT_SPAN_COUNT = 1;
  private static final int DEFAULT_ORIENTATION = LinearLayoutManager.VERTICAL;
  private static final boolean DEFAULT_REVERSE_LAYOUT = false;
  private CurationsInfiniteAdapter curationsInfiniteAdapter;
  private BVCurations curations;
  private CurationsViewDelegate viewDelegate;
  private CurationsViewPropDelegate viewPropDelegate;
  private OnFeedItemClickListener feedItemClickListener;
  private CurationsInfiniteContract.Presenter presenter;
  private CurationsImageLoader imageLoader;
  private CurationsFeedRequest request;
  private OnPageLoadListener pageLoadListener;
  private CurationsAnalyticsManager curationsAnalyticsManager;

  // endregion

  // region Constructors

  public CurationsInfiniteRecyclerView(Context context) {
    super(context);
  }

  public CurationsInfiniteRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CurationsInfiniteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void init(Context context, @Nullable AttributeSet attr) {
    super.init(context, attr);
    curationsAnalyticsManager = new CurationsAnalyticsManager(BVSDK.getInstance());
    curations = new BVCurations();
    setHasFixedSize(true);
    viewDelegate = new CurationsViewDelegate();
    viewPropDelegate = new CurationsViewPropDelegate();
    viewPropDelegate.setScreenDimens(getScreenDimensions(getContext()));
    parseAttributes(context, attr);
  }

  private void parseAttributes(final Context context, @Nullable final AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurationsInfiniteRecyclerView);
    int curationPageSize = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationPageSize, DEFAULT_PAGE_SIZE);
    int cellWidthRatio = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationCellWidthRatio, DEFAULT_WIDTH_RATIO);
    int cellHeightRatio = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationCellHeightRatio, DEFAULT_HEIGHT_RATIO);
    int curationSpanCount = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationSpanCount, DEFAULT_SPAN_COUNT);
    int curationOrientation = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationOrientation, DEFAULT_ORIENTATION);
    boolean curationReverseLayout = a.getBoolean(R.styleable.CurationsInfiniteRecyclerView_curationReverseLayout, DEFAULT_REVERSE_LAYOUT);
    int curationMaxItemCount = a.getInteger(R.styleable.CurationsInfiniteRecyclerView_curationMaxItemCount, DEFAULT_MAX_FEED_ITEM_COUNT);
    viewPropDelegate.setCurationPageSize(curationPageSize);
    viewPropDelegate.setCurationCellWidthRatio(cellWidthRatio);
    viewPropDelegate.setCurationCellHeightRatio(cellHeightRatio);
    viewPropDelegate.setCurationSpanCount(curationSpanCount);
    viewPropDelegate.setCurationOrientation(curationOrientation);
    viewPropDelegate.setCurationReverseLayout(curationReverseLayout);
    viewPropDelegate.setCurationMaxFeedItemCount(curationMaxItemCount);
    a.recycle();
  }

  // endregion

  // region Public API

  /**
   * @param request Your {@link CurationsFeedRequest}. This class will manage the paging.
   * @return
   */
  public CurationsInfiniteRecyclerView setRequest(@NonNull final CurationsFeedRequest request) {
    this.request = request;
    return this;
  }

  /**
   * @param pageLoadListener Your implementation of the {@link OnPageLoadListener} interface
   * @return this class
   */
  public CurationsInfiniteRecyclerView setOnPageLoadListener(final OnPageLoadListener pageLoadListener) {
    this.pageLoadListener = pageLoadListener;
    return this;
  }

  /**
   * @param onFeedItemClickListener Your implementation of the {@link OnFeedItemClickListener} interface
   * @return this class
   */
  public CurationsInfiniteRecyclerView setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
    this.feedItemClickListener = onFeedItemClickListener;
    return this;
  }

  /**
   * @param imageLoader Your implementation of the {@link CurationsImageLoader} interface
   * @return this class
   */
  public CurationsInfiniteRecyclerView setImageLoader(@NonNull CurationsImageLoader imageLoader) {
    this.imageLoader = imageLoader;
    return this;
  }

  /**
   * Optional field to increase the maximum number of {@link CurationsFeedItem}s
   * that this View will associate with the Adapter. Default is 360,430 which should
   * be sufficient as is. Increasing this number, increases the amount of heap that
   * is retained by the adapter.
   *
   * @param maxFeedItemCount Max number of items associated with Adapter
   * @return this class
   */
  public CurationsInfiniteRecyclerView setMaxFeedItemCount(int maxFeedItemCount) {
    viewPropDelegate.setCurationMaxFeedItemCount(maxFeedItemCount);
    return this;
  }

  /**
   * @param cellWidthRatio Used with {@link #setCellHeightRatio(int)} to set the aspect ratio
   *                       that this cell will be rendered at.
   * @return this class
   */
  public CurationsInfiniteRecyclerView setCellWidthRatio(int cellWidthRatio) {
    viewPropDelegate.setCurationCellWidthRatio(cellWidthRatio);
    return this;
  }

  /**
   * @param cellHeightRatio Used with {@link #setCellWidthRatio(int)} to set the aspect ratio
   *                       that this cell will be rendered at.
   * @return this class
   */
  public CurationsInfiniteRecyclerView setCellHeightRatio(int cellHeightRatio) {
    viewPropDelegate.setCurationCellHeightRatio(cellHeightRatio);
    return this;
  }

  /**
   * @param spanCount Number of columns if orientation is {@link LinearLayoutManager#VERTICAL} and
   *                  number of rows if orientation is {@link LinearLayoutManager#HORIZONTAL}
   * @return this class
   */
  public CurationsInfiniteRecyclerView setSpanCount(int spanCount) {
    viewPropDelegate.setCurationSpanCount(spanCount);
    return this;
  }

  /**
   * @param orientation Orientation for the list/grid. Can be {@link LinearLayoutManager#VERTICAL} or
   *                    {@link LinearLayoutManager#HORIZONTAL}
   * @return this class
   */
  public CurationsInfiniteRecyclerView setOrientation(int orientation) {
    viewPropDelegate.setCurationOrientation(orientation);
    return this;
  }

  /**
   * Listener to be notified when paging requests succeeds or fails
   */
  public interface OnPageLoadListener {
    /**
     * @param pageIndex Index of the page loaded. Zero-based indexing
     * @param pageSize Number of items returned for this page. Will be less than or
     *                 equal to the passed in page size depending on where we are.
     */
    void onPageLoadSuccess(int pageIndex, int pageSize);

    /**
     * @param pageIndex Index of the page loaded. Zero-based indexing
     * @param throwable Error for loading the page
     */
    void onPageLoadFailure(int pageIndex, Throwable throwable);
  }

  /**
   * Listener to be notified when a {@link CurationsFeedItem} cell is tapped
   */
  public interface OnFeedItemClickListener {
    void onClick(CurationsFeedItem curationsFeedItem);
  }

  /**
   * Starts the fetching of the data for the grid with all of the provided options
   */
  public void load() {
    if (request == null) {
      throw new IllegalStateException("Request must not be null");
    }
    if (imageLoader == null) {
      throw new IllegalStateException("Must call recyclerView.setImageLoader(...) first");
    }
    if (getLayoutManager() == null) {
      int curationSpanCount = viewPropDelegate.getCurationSpanCount();
      int curationOrientation = viewPropDelegate.getCurationOrientation();
      boolean curationReverseLayout = viewPropDelegate.isCurationReverseLayout();
      setLayoutManager(new GridLayoutManager(getContext(), curationSpanCount, curationOrientation, curationReverseLayout));
    }
    if (getAdapter() == null) {
      setAdapter(new CurationsInfiniteAdapter(viewPropDelegate, imageLoader, feedItemClickListener));
    }
    presenter = new CurationsInfinitePresenter(viewDelegate, viewPropDelegate, curations, pageLoadListener, request);
    LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
    CurationsScrollListener scrollListener = new CurationsScrollListener(layoutManager, presenter);
    addOnScrollListener(scrollListener);

    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        CurationsInfiniteRecyclerView view = CurationsInfiniteRecyclerView.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
          view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        viewPropDelegate.setViewDimens(new Point(viewWidth, viewHeight));
        presenter.start();
      }
    });
  }

  @Override
  public void setAdapter(Adapter adapter) {
    if (!(adapter instanceof CurationsInfiniteAdapter)) {
      throw new IllegalStateException("Adapter must be a CurationsInfiniteAdapter");
    }
    curationsInfiniteAdapter = (CurationsInfiniteAdapter) adapter;
    viewDelegate.setCurationsInfiniteAdapter(curationsInfiniteAdapter);
    super.setAdapter(adapter);
  }

  @Override
  public void setLayoutManager(LayoutManager layoutManager) {
    if (!(layoutManager instanceof LinearLayoutManager)) {
      throw new IllegalStateException("LayoutManager must be of type LinearLayoutManager or extend it (e.g. GridLayoutManager)");
    }
    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
    int numSpans = 1;
    if (linearLayoutManager instanceof GridLayoutManager) {
      GridLayoutManager gridLayoutManager = (GridLayoutManager) linearLayoutManager;
      numSpans = gridLayoutManager.getSpanCount();
    }
    viewPropDelegate.setCurationSpanCount(numSpans);
    viewPropDelegate.setCurationOrientation(linearLayoutManager.getOrientation());
    viewPropDelegate.setCurationReverseLayout(linearLayoutManager.getReverseLayout());
    super.setLayoutManager(layoutManager);
  }

  // endregion

  // region Internal API

  private static final class CurationsViewDelegate implements CurationsInfiniteContract.View {
    private CurationsInfiniteAdapter curationsInfiniteAdapter;

    @Override
    public void addFeedItems(List<CurationsFeedItem> feedItems) {
      curationsInfiniteAdapter.update(feedItems);
    }

    public void setCurationsInfiniteAdapter(CurationsInfiniteAdapter curationsInfiniteAdapter) {
      this.curationsInfiniteAdapter = curationsInfiniteAdapter;
    }
  }

  private static class CurationsViewPropDelegate implements CurationsInfiniteContract.ViewProps {
    private Point screenDimens;
    private Point viewDimens;
    private int curationPageSize;
    private int curationSpanCount;
    private boolean curationReverseLayout;
    private int curationOrientation;
    private int curationCellWidthRatio;
    private int curationCellHeightRatio;
    private int curationMaxFeedItemCount;

    @Override
    public Point getScreenDimens() {
      return screenDimens;
    }

    @Override
    public Point getViewDimens() {
      return viewDimens;
    }

    @Override
    public int getCurationPageSize() {
      return curationPageSize;
    }

    @Override
    public int getCurationSpanCount() {
      return curationSpanCount;
    }

    @Override
    public boolean isCurationReverseLayout() {
      return curationReverseLayout;
    }

    @Override
    public int getCurationOrientation() {
      return curationOrientation;
    }

    @Override
    public int getCurationCellWidthRatio() {
      return curationCellWidthRatio;
    }

    @Override
    public int getCurationCellHeightRatio() {
      return curationCellHeightRatio;
    }

    @Override
    public int getCurationMaxFeedItemCount() {
      return curationMaxFeedItemCount;
    }

    public void setCurationPageSize(int curationPageSize) {
      this.curationPageSize = curationPageSize;
    }

    public void setCurationSpanCount(int spanCount) {
      this.curationSpanCount = spanCount;
    }

    public void setCurationReverseLayout(boolean curationReverseLayout) {
      this.curationReverseLayout = curationReverseLayout;
    }

    public void setCurationOrientation(int curationOrientation) {
      this.curationOrientation = curationOrientation;
    }

    public void setCurationCellWidthRatio(int curationCellWidthRatio) {
      this.curationCellWidthRatio = curationCellWidthRatio;
    }

    public void setCurationCellHeightRatio(int curationCellHeightRatio) {
      this.curationCellHeightRatio = curationCellHeightRatio;
    }

    public void setCurationMaxFeedItemCount(int curationMaxFeedItemCount) {
      this.curationMaxFeedItemCount = curationMaxFeedItemCount;
    }

    public void setScreenDimens(Point screenDimens) {
      this.screenDimens = screenDimens;
    }

    public void setViewDimens(Point viewDimens) {
      this.viewDimens = viewDimens;
    }
  }

  /**
   * Manager that decides when the presenter should load more data
   * to be associated with the adapter
   */
  private static class CurationsScrollListener extends OnScrollListener {
    private static final String TAG = "CurScrollListener";
    private final LinearLayoutManager linearLayoutManager;
    private final CurationsInfiniteContract.Presenter presenter;

    CurationsScrollListener(final LinearLayoutManager linearLayoutManager, final CurationsInfiniteContract.Presenter presenter) {
      this.linearLayoutManager = linearLayoutManager;
      this.presenter = presenter;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      int orientation = linearLayoutManager.getOrientation();
      int delta = orientation == LinearLayoutManager.VERTICAL ? dy : dx;

      int lastVisibleFeedItemIndex = linearLayoutManager.findLastVisibleItemPosition();
      presenter.onScroll(delta, lastVisibleFeedItemIndex);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }
  }

  private static Point getScreenDimensions(@NonNull final Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size;
  }

  static ImageSize getImageSize(@NonNull final CurationsInfiniteContract.ViewProps viewProps) {
    Point viewDimens = viewProps.getViewDimens();
    int viewWidth = viewDimens.x;
    int viewHeight = viewDimens.y;
    boolean isVertical = isVertical(viewProps);
    int numSpans = viewProps.getCurationSpanCount();
    int distance = isVertical ? viewWidth : viewHeight;
    if (distance == 0) {
      throw new IllegalStateException(String.format("Must define the %s dimension", isVertical ? "horizontal" : "vertical"));
    }
    int spanDistance = distance / numSpans;
    int widthRatio = viewProps.getCurationCellWidthRatio();
    int heightRatio = viewProps.getCurationCellHeightRatio();
    ImageSize imageSize;
    if (isVertical) {
      int relativeHeight = spanDistance * heightRatio / widthRatio;
      imageSize = new ImageSize(spanDistance, relativeHeight);
    } else {
      int relativeWidth = spanDistance * widthRatio / heightRatio;
      imageSize = new ImageSize(relativeWidth, spanDistance);
    }

    return imageSize;
  }

  static final class ImageSize {
    private final int widthPixels, heightPixels;

    ImageSize(final int widthPixels, final int heightPixels) {
      this.widthPixels = widthPixels;
      this.heightPixels = heightPixels;
    }

    int getWidthPixels() {
      return widthPixels;
    }

    int getHeightPixels() {
      return heightPixels;
    }
  }

  static boolean isVertical(@NonNull final CurationsInfiniteContract.ViewProps viewProps) {
    return viewProps.getCurationOrientation() == LinearLayoutManager.VERTICAL;
  }

  // endregion

  // region Analytics

  @NonNull
  private String getExternalId() {
    return request == null ? "" : request.externalId;
  }

  @Override
  public String getProductId() {
    return getExternalId();
  }

  @Override
  public void onViewGroupInteractedWith() {
    if (isNestedScrollingEnabled()) {
      curationsAnalyticsManager.sendUsedFeatureEventScrolled(getExternalId());
    }
  }

  @Override
  public void onAddedToViewHierarchy() {
    curationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(WIDGET_ID, ReportingGroup.RECYCLERVIEW);
  }

  @Override
  public boolean startNestedScroll(int axes) {
    if (!isNestedScrollingEnabled()) {
      curationsAnalyticsManager.sendUsedFeatureEventScrolled(getExternalId());
    }
    return super.startNestedScroll(axes);
  }

  // endregion
}
