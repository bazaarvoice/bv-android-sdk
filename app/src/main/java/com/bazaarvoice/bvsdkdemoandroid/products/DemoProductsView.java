package com.bazaarvoice.bvsdkdemoandroid.products;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoProductsView extends FrameLayout implements DemoProductsContract.View {
  private DemoProductsContract.Presenter presenter;
  private DemoProductsAdapter productsAdapter;
  private DemoProductsLayoutManager productsLayoutManager;
  private int orientation;
  private int spanCount;
  private boolean productsNestedScrollingEnabled;

  @Inject Picasso picasso;

  @BindView(R.id.productsRecyclerView) RecyclerView productsRecyclerView;
  @BindView(R.id.productsProgressBar) ProgressBar productsProgressBar;
  @BindView(R.id.productsEmptyMessage) TextView productsEmptyMessage;

  public DemoProductsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.view_products, this, true);
    setOptions(context, attrs);
    init();
  }

  private void init() {
    if (isInEditMode()) {
      return;
    }
    DemoApp.getAppComponent(getContext())
        .inject(this);
    productsAdapter = new DemoProductsAdapter(picasso);
  }

  private void setOptions(Context context, AttributeSet attrs) {
    TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.DemoProductsView,
        0, 0);

    try {
      orientation = a.getInt(R.styleable.DemoProductsView_productsOrientation, LinearLayoutManager.VERTICAL);
      spanCount = a.getInt(R.styleable.DemoProductsView_productsSpanCount, 2);
      productsNestedScrollingEnabled = a.getBoolean(R.styleable.DemoProductsView_productsNestedScrollingEnabled, true);
    } finally {
      a.recycle();
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    productsRecyclerView.setAdapter(productsAdapter);
    productsLayoutManager = new DemoProductsLayoutManager(getContext(), spanCount, orientation, false);
    productsLayoutManager.setScrollEnabled(productsNestedScrollingEnabled);
    productsRecyclerView.setLayoutManager(productsLayoutManager);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.start();
  }

  @SuppressWarnings("unchecked")
  public <ProductType extends BVDisplayableProductContent> void updateContent(List<ProductType> products) {
    productsAdapter.updateContent(products);
  }

  @Override
  public void showEmpty(boolean showing) {
    productsEmptyMessage.setVisibility(showing ? VISIBLE : GONE);
  }

  @Override
  public void showEmptyMessage(String message) {
    productsEmptyMessage.setText(message);
  }

  @Override
  public void showLoading(boolean showing) {
    productsProgressBar.setVisibility(showing ? VISIBLE : GONE);
  }

  @Override
  public void setOnItemClickListener(DemoProductsContract.OnItemClickListener onItemClickListener) {
    productsAdapter.setOnItemClickListener(onItemClickListener);
  }

  @Override
  public void showProductTapped(String name) {
    final Snackbar snackbar = Snackbar.make(this, "Tapped " + name, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(getResources().getString(R.string.okay), new OnClickListener() {
      @Override
      public void onClick(View v) {
        snackbar.dismiss();
      }
    });
    snackbar.show();
  }

  @Override
  public void setPresenter(DemoProductsContract.Presenter presenter) {
    this.presenter = presenter;
  }

  private static class DemoProductsLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public DemoProductsLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
      super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean isScrollEnabled) {
      this.isScrollEnabled = isScrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
      //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
      return isScrollEnabled && getOrientation() == VERTICAL && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
      return isScrollEnabled && getOrientation() == HORIZONTAL && super.canScrollHorizontally();
    }
  }
}
