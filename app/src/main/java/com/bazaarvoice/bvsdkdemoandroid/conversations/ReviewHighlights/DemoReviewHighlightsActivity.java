package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewHighlights;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.DemoRevieHighlightDetailContract;
import com.bazaarvoice.bvandroidsdk.ReviewHighlight;
import com.bazaarvoice.bvandroidsdk.ReviewHighlights;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import java.util.List;
import javax.inject.Inject;
import butterknife.ButterKnife;

public class DemoReviewHighlightsActivity extends AppCompatActivity implements DemoRevieHighlightDetailContract.View {
    private String productId;
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    LinearLayout reviewHighlightLayout;
    TextView prosHeading, consHeading;
    private DemoRevieHighlightDetailContract.UserActionsListener reviewHighlightsListener;
    @Inject
    BVConversationsClient bvConversationsClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_review_highlights);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        reviewHighlightsListener = new DemoReviewHighlightsPresenter(this, productId, bvConversationsClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewHighlightsListener.loadReviewHighlights(false);
    }

    @Override
    public void showReviewHighlights(ReviewHighlights reviewHighlight) {
        reviewHighlightLayout = (LinearLayout) findViewById(R.id.reviewHighlightLayout);
        List<ReviewHighlight> positives = reviewHighlight.getPositives();
        List<ReviewHighlight> negetives = reviewHighlight.getNegatives();
        prosHeading = new TextView(this);
        consHeading = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(20, 10, 20, 0);

        if (positives.size() != 0) {
            prosHeading.setText("Pros");
            prosHeading.setTextSize(20);
            prosHeading.setTextColor(Color.parseColor("#336470"));
            reviewHighlightLayout.addView(prosHeading);
            for (ReviewHighlight positive : positives) {
                TextView pro = new TextView(this);
                pro.setLayoutParams(layoutParams);
                pro.setText(positive.title.substring(0, 1).toUpperCase() + positive.title.substring(1) + " (" + positive.mentionsCount + ")");
                pro.setTextSize(16);
                pro.setBackgroundColor(Color.WHITE);
                pro.setWidth(70);
                pro.setHeight(100);
                pro.setPadding(40, 0, 0, 0);
                pro.setTextColor(Color.BLACK);
                reviewHighlightLayout.addView(pro);
            }
        }

        if (negetives.size() != 0) {
            consHeading.setText("Cons");
            consHeading.setTextSize(20);
            consHeading.setTextColor(Color.parseColor("#336470"));
            reviewHighlightLayout.addView(consHeading);
            for (ReviewHighlight negetive : negetives) {
                TextView cons = new TextView(this);
                cons.setLayoutParams(layoutParams);
                cons.setText(negetive.title.substring(0, 1).toUpperCase() + negetive.title.substring(1) + " (" + negetive.mentionsCount + ")");
                cons.setTextSize(16);
                cons.setBackgroundColor(Color.WHITE);
                cons.setWidth(70);
                cons.setHeight(100);
                cons.setPadding(40, 0, 0, 0);
                cons.setTextColor(Color.BLACK);
                reviewHighlightLayout.addView(cons);
            }
        }
    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }
}

