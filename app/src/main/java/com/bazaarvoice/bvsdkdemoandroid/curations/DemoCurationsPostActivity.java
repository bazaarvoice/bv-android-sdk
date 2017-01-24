package com.bazaarvoice.bvsdkdemoandroid.curations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVCurations;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvandroidsdk.CurationsAuthor;
import com.bazaarvoice.bvandroidsdk.CurationsLink;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvandroidsdk.CurationsPostCallback;
import com.bazaarvoice.bvandroidsdk.CurationsPostRequest;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DemoCurationsPostActivity extends AppCompatActivity implements CurationsPostCallback, Target {
    private static final int SELECT_PICTURE = 1;
    private static final int IMAGE_HEIGHT = DemoUtils.MAX_IMAGE_HEIGHT/3;
    private TextView commentText;
    private TextView aliasTokenText;
    private ImageView chosenImageView;
    private Bitmap chosenBmp;
    private ProgressBar progressBar;
    private Button postBtn;
    private Uri imageUri;

    @Inject DemoConfigUtils demoConfigUtils;
    @Inject DemoDataUtil demoDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_post);
        DemoApp.get(this).getAppComponent().inject(this);
        progressBar = (ProgressBar) findViewById(R.id.curations_post_loading);
        progressBar.setVisibility(View.GONE);
        commentText = (TextView) findViewById(R.id.curationsCommentText);
        aliasTokenText = (TextView) findViewById(R.id.curationsNicknameText);
        chosenImageView = (ImageView) findViewById(R.id.curationsPostImageView);

        postBtn = (Button) findViewById(R.id.curationsPostBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (demoConfigUtils.isDemoClient()) {
                    DemoCurationsPostActivity.this.onSuccess(demoDataUtil.getCurationsPostResponse());
                    return;
                }

                postCurationsContent();
            }
        });

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    private void postCurationsContent(){

        String text = commentText.getText().toString();
        String aliasToken = aliasTokenText.getText().toString();

        Toast toast = null;
        if (DemoConstants.CURATIONS_GROUPS.isEmpty()){
            toast = Toast.makeText(this, "Add a valid group name for your organization in DemoCurationsPostActivity", Toast.LENGTH_LONG);
        }else if (text == null || text.equalsIgnoreCase("")){
            toast = Toast.makeText(this, "You must provide a comment", Toast.LENGTH_LONG);
        }else if ( aliasToken == null || aliasToken.equalsIgnoreCase("")){
            toast = Toast.makeText(this, "You must provide a nickname", Toast.LENGTH_LONG);
        }

        if (toast != null){
            toast.show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        postBtn.setEnabled(false);


        BVCurations curations = new BVCurations();

        CurationsAuthor author = new CurationsAuthor.Builder(aliasToken, aliasToken).build();
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");

        List<CurationsLink> links = new ArrayList<>();
        links.add(new CurationsLink("VALID LINK 1"));
        links.add(new CurationsLink("VALID LINK 2"));

        List<CurationsPhoto> photos = new ArrayList<>();
        photos.add(new CurationsPhoto("VALID PHOTO URL 1"));
        photos.add(new CurationsPhoto("VALID PHOTO URL 2"));

        long timestampInSeconds = System.currentTimeMillis() / 1000;

        if (DemoConstants.ENVIRONMENT == BazaarEnvironment.STAGING) {
            CurationsPostRequest request = new CurationsPostRequest.Builder(author, DemoConstants.CURATIONS_GROUPS, text, chosenBmp) //required params
                    //Optional Params
                    .timestampInSeconds(timestampInSeconds)
                    //                .geoCoordinates(37.123, -97.456)
                    //                .place("HUMAN READABLE PLACE HERE such as 'Austin, TX'")
                    //                .tags(tags)
                    //                .teaser("YOU TEASER HERE")
                    //                .permalink("DESIRED PERMALINK HERE")
                    //                .links(links)
                    .build();

            // curations.postContentToCurations(request, this);
        } else {
            onSuccess(demoDataUtil.getCurationsPostResponse());
        }
    }

    @Override
    public void onSuccess(CurationsPostResponse response) {
        Toast success = Toast.makeText(this, "Successfully posted to Curations and it should be available in your Curations web portal", Toast.LENGTH_LONG);
        success.show();
        finish();
    }

    @Override
    public void onFailure(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        postBtn.setEnabled(true);
        Toast failure = Toast.makeText(this, "Unsuccessfully posted to Curations. " + throwable.getMessage(), Toast.LENGTH_LONG);
        failure.show();
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        chosenBmp = bitmap;

        if (chosenBmp != null){
            chosenImageView.setImageBitmap(chosenBmp);
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                imageUri = selectedImageUri;

                try {
                    Picasso.with(this)
                            .load(imageUri)
                            .resize(0, IMAGE_HEIGHT)
                            .into(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else{
            finish();
        }
    }
}
