package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewSummary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ReviewSummary;
import com.bazaarvoice.bvandroidsdk.ReviewSummaryResponse;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DemoReviewSummaryActivity extends AppCompatActivity implements DemoReviewSummaryDetailContract.View {
    private String productId;
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private DemoReviewSummaryDetailContract.UserActionsListener reviewSummaryListener;
    @Inject
    BVConversationsClient bvConversationsClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_review_summary);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        reviewSummaryListener = new DemoReviewSummaryPresenter(this, productId, bvConversationsClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewSummaryListener.loadReviewSummary(false);
    }

    public static void transitionTo(Context fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoReviewSummaryActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }
    @Override
    public void showReviewSummary(ReviewSummary reviewSummary) {
        LinearLayout reviewSummaryLayout = findViewById(R.id.reviewSummaryLayout);
        reviewSummaryLayout.removeAllViews(); // Clear any previous content

        // Layout parameters for spacing
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 10);

        // Title
        TextView titleView = new TextView(this);
        titleView.setText(reviewSummary.getTitle());
        titleView.setTextSize(22);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(Color.parseColor("#336470"));
        titleView.setLayoutParams(layoutParams);
        reviewSummaryLayout.addView(titleView);

        // Summary content
        TextView summaryView = new TextView(this);
        summaryView.setText(reviewSummary.getSummary());
        summaryView.setTextSize(16);
        summaryView.setTextColor(Color.DKGRAY);
        summaryView.setLayoutParams(layoutParams);
        reviewSummaryLayout.addView(summaryView);

        // Optional Disclaimer
        if (reviewSummary.getDisclaimer() != null && !reviewSummary.getDisclaimer().isEmpty()) {
            TextView disclaimerView = new TextView(this);
            disclaimerView.setText(reviewSummary.getDisclaimer());
            disclaimerView.setTextSize(12);
            disclaimerView.setTextColor(Color.GRAY);
            disclaimerView.setLayoutParams(layoutParams);
            disclaimerView.setPadding(0, 20, 0, 0);
            reviewSummaryLayout.addView(disclaimerView);
        }
    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

}

