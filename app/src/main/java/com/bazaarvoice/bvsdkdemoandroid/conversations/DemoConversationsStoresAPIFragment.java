package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionException;
import com.bazaarvoice.bvandroidsdk.StoreNotificationManager;
import com.bazaarvoice.bvandroidsdk.StoreReviewSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.StoreReviewSubmissionResponse;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.stores.DemoStoresActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

///import static com.iovation.mobile.android.DevicePrint.getBlackbox;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoConversationsStoresAPIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoConversationsStoresAPIFragment extends Fragment {

    private ProgressDialog progress;
    private static final String TEST_PRODUCT_ID = "1"; // This product ID must be in the catalog for the provided Conversations API key.

    private List<String> TEST_BULK_PRODUCT_IDS = Arrays.asList("1", "2", "3", "4");

    @Inject DemoClient demoClient;
    @Inject BVConversationsClient client;

    public static DemoConversationsStoresAPIFragment newInstance() {
        DemoConversationsStoresAPIFragment fragment = new DemoConversationsStoresAPIFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApp.getAppComponent(getContext()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_conversations_stores_demo, container, false);
        progress = new ProgressDialog(getContext());

        setUpDisplayActionButtons(view);
        setUpUGCSubmisionActionButtons(view);

        return view;
    }

    private void setUpDisplayActionButtons(View view){
        // Read reviews for product id
        Button readReviewsButton = (Button) view.findViewById(R.id.displayStoreReviewsBtn);
        readReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    DemoMainActivity activity = (DemoMainActivity) getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        activity.transitionToStoreReviewsActivity(TEST_PRODUCT_ID);
                    }
                }
            }
        });

        // Display Bulk Stores with known store id(s)
        Button buldStoresById = (Button) view.findViewById(R.id.displayStoresByIds);
        buldStoresById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    DemoMainActivity activity = (DemoMainActivity) getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        ArrayList<String> productIds = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
                        DemoStoresActivity.transitionToWithProductIds(activity, productIds);
                    }
                }
            }
        });

        // Display Bulk Stores with Ratings from Limit and Offset
        Button bulkStoresByLimitOffset = (Button) view.findViewById(R.id.displayStoresWithLimitOffset);
        bulkStoresByLimitOffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    DemoMainActivity activity = (DemoMainActivity) getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        DemoStoresActivity.transitionToWithLimitAndOffset(activity, 20, 0);
                    }
                }
            }
        });

        Button queueStoreNotificationReview = (Button) view.findViewById(R.id.queueStoreNotificationReview);
        queueStoreNotificationReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    StoreNotificationManager storeNotificationManager = StoreNotificationManager.getInstance();
                    String storeId = "1";
                    long dwellTimeMillis = 5500l;
                    storeNotificationManager.scheduleNotification(storeId, dwellTimeMillis);
                }
            }
        });

    }

    private void setUpUGCSubmisionActionButtons(View view){

        //////////////////////////////
        // Submit Example Buttons
        //////////////////////////////
        // Submit Review w/ Photo
        Button feedBtn = (Button) view.findViewById(R.id.submitStoreReviewWithPhoto);
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    // For clients in US, iovation SDK is required!
//                    String blackbox = getBlackbox(getContext().getApplicationContext());

                    progress.setTitle("Submitting Review...");
                    progress.show();

                    File localImageFile = null;

                    try {
                        InputStream stream = getContext().getAssets().open("bike_shop_photo.png");
                        localImageFile = createFileFromInputStream(stream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StoreReviewSubmissionRequest submission = new StoreReviewSubmissionRequest.Builder(Action.Preview, TEST_PRODUCT_ID)
//                            .fingerPrint(blackbox)  // uncomment me when using iovation SDK
                            .userNickname("shazbat")
                            .user("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                            .userEmail("foo@bar.com")
                            .rating(5)
                            .title("Review title")
                            .reviewText("This is the review text the user adds about how great the product is!")
                            .sendEmailAlertWhenCommented(true)
                            .sendEmailAlertWhenPublished(true)
                            .agreedToTermsAndConditions(true)
                            .addPhoto(localImageFile, "What a cute pupper!")
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<StoreReviewSubmissionResponse>() {
                        @Override
                        public void onSuccess(@NonNull StoreReviewSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful review submission.");
                        }

                        @Override
                        public void onFailure(@NonNull ConversationsSubmissionException exception) {
                            progress.dismiss();
                            showDialogWithMessage(exception.getMessage());
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        String conversationsStoresKey = demoClient.getApiKeyConversationsStores();
        String displayName = demoClient.getDisplayName();

        if (!DemoConstants.isSet(conversationsStoresKey)) {
            String errorMessage = String.format(
                    getString(R.string.view_demo_error_message),
                    displayName, getString(R.string.demo_conversations_stores)
            );
            showDialogWithMessage(errorMessage);
            return false;
        }

        return true;

    }

    private void showDialogWithMessage(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = File.createTempFile("tmp", "png");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;

        }catch (IOException e) {
            //Logging exception
            e.printStackTrace();
        }

        return null;
    }


}
