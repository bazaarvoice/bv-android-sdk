package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bazaarvoice.bvandroidsdk.BestFeature;
import com.bazaarvoice.bvandroidsdk.FeatureSentiment;
import com.bazaarvoice.bvandroidsdk.Quote;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;
import com.bazaarvoice.bvandroidsdk.WorstFeature;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ChipAdapter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ReviewAdapter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ReviewItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.ViewStub;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DemoProductSentimentsView extends ConstraintLayout implements DemoProductSentimentsContract.View {

    @BindView(R.id.textViewFilterTitle)
    TextView filterTitleTextView;


    @BindView(R.id.recyclerViewStub)
    ViewStub recyclerViewStub;

    @BindView(R.id.reviews_loading)
    ProgressBar reviewsLoadingProgressBar;

    @BindView(R.id.empty_message)
    TextView emptyMessageTextView;
    private DemoProductSentimentsContract.Presenter presenter;

    public DemoProductSentimentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }
        presenter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void setPresenter(DemoProductSentimentsContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showSummarisedFeatures(SummarisedFeaturesResponse response) {
        List<ReviewItem> allItems = new ArrayList<>();

        // 1. Build the list of ALL possible items (chips and quotes)
        // Add best features section
        if (response.getBestFeatures() != null && !response.getBestFeatures().isEmpty()) {
            List<String> positiveChips = new ArrayList<>();
            for (BestFeature feature : response.getBestFeatures()) {
                positiveChips.add(feature.getFeature());
            }
            // Using R.drawable.ic_add_circle from our previous step
            allItems.add(new ReviewItem.ChipSection("Pros", positiveChips, R.drawable.ic_add_circle));

            List<String> positiveQuotes = new ArrayList<>();
            for (BestFeature feature : response.getBestFeatures()) {
                if (feature.getEmbedded() != null && feature.getEmbedded().getQuotes() != null) {
                    for (Quote quote : feature.getEmbedded().getQuotes()) {
                        positiveQuotes.add(quote.getText());
                    }
                }
            }
            if (!positiveQuotes.isEmpty()) {
                allItems.add(new ReviewItem.QuoteCard("What people like", positiveQuotes));
            }
        }

        // Add worst features section
        if (response.getWorstFeatures() != null && !response.getWorstFeatures().isEmpty()) {
            List<String> negativeChips = new ArrayList<>();
            for (WorstFeature feature : response.getWorstFeatures()) {
                negativeChips.add(feature.getFeature());
            }
            // Using R.drawable.ic_remove_circle from our previous step
            allItems.add(new ReviewItem.ChipSection("Cons", negativeChips, R.drawable.ic_remove_circle));

            List<String> negativeQuotes = new ArrayList<>();
            for (WorstFeature feature : response.getWorstFeatures()) {
                if (feature.getEmbedded() != null && feature.getEmbedded().getQuotes() != null) {
                    for (Quote quote : feature.getEmbedded().getQuotes()) {
                        negativeQuotes.add(quote.getText());
                    }
                }
            }
            if (!negativeQuotes.isEmpty()) {
                allItems.add(new ReviewItem.QuoteCard("What people dislike", negativeQuotes));
            }
        }

        // If the list has content, set up the adapter
        if (!allItems.isEmpty()) {
            RecyclerView recyclerView = findViewById(R.id.recycler_view_sum_features);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // 2. Give the full list to the stateful adapter.
            //    The adapter will handle the toggle logic internally.
            //    (Ensure you are using the ReviewAdapter from the previous answer)
            ReviewAdapter adapter = new ReviewAdapter(getContext(), allItems);
            recyclerView.setAdapter(adapter);
        }

    }


    @Override
    public void showSummarisedFeaturesError() {
        // TODO: Show error state for summarised features
    }

    @Override
    public void showFeaturesSentiment(FeaturesSentimentResponse response) {
        if (response != null && response.getFeatures() != null) {

            List<String> features = new ArrayList<>();
            for (FeatureSentiment feature : response.getFeatures()) {
                features.add(feature.getFeature());
            }
            // Set up RecyclerView for features chips
            RecyclerView chipsRecyclerView = findViewById(R.id.recyclerViewChips );
            chipsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            ChipAdapter chipAdapter = new ChipAdapter(getContext(), features);
            chipsRecyclerView.setAdapter(chipAdapter);
        }
    }


    @Override
    public void showFeaturesSentimentError() {
        // TODO: Show error state for features sentiment
    }

}