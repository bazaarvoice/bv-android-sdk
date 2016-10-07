package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.BulkStoreRatingsRequest;
import com.bazaarvoice.bvandroidsdk.BulkStoreRatingsResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvandroidsdk.StoreReviewResponse;
import com.bazaarvoice.bvandroidsdk.StoreReviewSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.StoreReviewSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.StoreReviewsRequest;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

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

    private BVConversationsClient client = new BVConversationsClient();

    public DemoConversationsStoresAPIFragment() {
        // Required empty public constructor
    }

    public static DemoConversationsStoresAPIFragment newInstance() {
        DemoConversationsStoresAPIFragment fragment = new DemoConversationsStoresAPIFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//                if (readyForDemo()) {
//                    ((DemoMainActivity) getActivity()).transitionToReviewsActivity(TEST_PRODUCT_ID);
//                }
                BVConversationsClient client = new BVConversationsClient();
                StoreReviewsRequest reviewsRequest = new StoreReviewsRequest.Builder(TEST_PRODUCT_ID, 20, 0)
                        .addSort(ReviewOptions.Sort.Rating, SortOrder.DESC)
                        .build();
                client.prepareCall(reviewsRequest).loadAsync(new ConversationsCallback<StoreReviewResponse>() {
                    @Override
                    public void onSuccess(StoreReviewResponse response) {
                        //called on Main Thread
                        response.getResults(); //contains reviews
                    }

                    @Override
                    public void onFailure(BazaarException exception) {
                        //called on Main Thread
                        Log.d("Dev", exception.getLocalizedMessage());
                    }
                });

            }
        });


        // Display Bulk Stores with known store id(s)
        Button buldStoresById = (Button) view.findViewById(R.id.displayStoresByIds);
        buldStoresById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (readyForDemo()) {
//                    ((DemoMainActivity) getActivity()).transitionToBulkRatingsActivity(new ArrayList<String>(TEST_BULK_PRODUCT_IDS));
//                }

                BVConversationsClient client = new BVConversationsClient();
                BulkStoreRatingsRequest storesRequest = new BulkStoreRatingsRequest.Builder(TEST_BULK_PRODUCT_IDS)
                        .build();

                client.prepareCall(storesRequest).loadAsync(new ConversationsCallback<BulkStoreRatingsResponse>() {
                    @Override
                    public void onSuccess(BulkStoreRatingsResponse response) {
                        //called on Main Thread
                        response.getResults(); //contains stores
                    }

                    @Override
                    public void onFailure(BazaarException exception) {
                        //called on Main Thread
                        Log.d("Dev", exception.getLocalizedMessage());
                    }
                });

            }
        });

        // Display Bulk Stores with Ratings from Limit and Offset
        Button bulkStoresByLimitOffset = (Button) view.findViewById(R.id.displayStoresWithLimitOffset);
        bulkStoresByLimitOffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (readyForDemo()) {
//                    ((DemoMainActivity) getActivity()).transitionToBulkRatingsActivity(new ArrayList<String>(TEST_BULK_PRODUCT_IDS));
//                }

                BVConversationsClient client = new BVConversationsClient();
                BulkStoreRatingsRequest storesRequest = new BulkStoreRatingsRequest.Builder(20,0)
                        .build();

                client.prepareCall(storesRequest).loadAsync(new ConversationsCallback<BulkStoreRatingsResponse>() {
                    @Override
                    public void onSuccess(BulkStoreRatingsResponse response) {
                        //called on Main Thread
                        response.getResults(); //contains stores
                    }

                    @Override
                    public void onFailure(BazaarException exception) {
                        //called on Main Thread
                        Log.d("Dev", exception.getLocalizedMessage());
                    }
                });

            }
        });

    }

    private void setUpUGCSubmisionActionButtons(View view){

        //////////////////////////////
        // Submit Example Buttons
        //////////////////////////////
        // Submit Store Review w/ Photo
        Button feedBtn = (Button) view.findViewById(R.id.submitStoreReviewWithPhoto);
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {

                    // For clients in US, iovation SDK is required!
//                    String blackbox = getBlackbox(getContext().getApplicationContext());

                    progress.setTitle("Submitting Store Review...");
                    progress.show();

                    File localImageFile = null;

                    try {
                        //InputStream stream = getContext().getAssets().open("puppy.jpg"); // Sample of a photo that is too small and will generate an error message.
                        InputStream stream = getContext().getAssets().open("bike_shop_photo.png");
                        localImageFile = createFileFromInputStream(stream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StoreReviewSubmissionRequest submission = new StoreReviewSubmissionRequest.Builder(Action.Submit, TEST_PRODUCT_ID)
//                            .fingerPrint(blackbox)  // uncomment me when using iovation SDK
                            .userNickname("mcfly")
                            .userEmail("flymcfly@bar.com")
                            .user("mrmcfly" + Math.random())
                            //.userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                            .rating(5)
                            .title("Most excellent!")
                            .reviewText("This is the greatest store that is, has been, or ever will be!")
                            .sendEmailAlertWhenCommented(true)
                            .sendEmailAlertWhenPublished(true)
                            .agreedToTermsAndConditions(true)
                            .addPhoto(localImageFile, "Awesome Photo!")
                            .build();

                    client.prepareCall(submission).loadAsync(new ConversationsCallback<StoreReviewSubmissionResponse>() {

                        @Override
                        public void onSuccess(StoreReviewSubmissionResponse response) {
                            progress.dismiss();
                            showDialogWithMessage("Successful review submission.");
                        }

                        @Override
                        public void onFailure(BazaarException exception) {
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

        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();

        String conversationsStoresKey = currentConfig.apiKeyConversationsStores;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(conversationsStoresKey)) {
            String errorMessage = String.format(
                    getString(R.string.view_demo_error_message),
                    displayName, getString(R.string.demo_conversations)
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
