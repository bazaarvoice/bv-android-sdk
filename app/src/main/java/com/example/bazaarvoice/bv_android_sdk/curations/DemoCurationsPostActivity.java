package com.example.bazaarvoice.bv_android_sdk.curations;

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
import com.bazaarvoice.bvandroidsdk.CurationsAuthor;
import com.bazaarvoice.bvandroidsdk.CurationsLink;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvandroidsdk.CurationsPostCallback;
import com.bazaarvoice.bvandroidsdk.CurationsPostRequest;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DemoCurationsPostActivity extends AppCompatActivity implements CurationsPostCallback, Target {
    private static final int SELECT_PICTURE = 1;
    private static final String groupName = "REPLACE WITH YOUR OWN GROUP";

    private TextView commentText;
    private TextView aliasTokenText;
    private ImageView chosenImageView;
    private Bitmap chosenBmp;
    private ProgressBar progressBar;
    private Button postBtn;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_post);
        progressBar = (ProgressBar) findViewById(R.id.curations_post_loading);
        progressBar.setVisibility(View.GONE);
        commentText = (TextView) findViewById(R.id.curationsCommentText);
        aliasTokenText = (TextView) findViewById(R.id.curationsNicknameText);

        postBtn = (Button) findViewById(R.id.curationsPostBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (groupName.equalsIgnoreCase("REPLACE WITH YOUR OWN GROUP")){
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

        List<String> groups = new LinkedList<>();
        groups.add(groupName);

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

        CurationsPostRequest request = new CurationsPostRequest.Builder(author, groups, text, chosenBmp) //required params
                //Optional Params
                .timestampInSeconds(timestampInSeconds)
                //                .place("HUMAN READABLE PLACE HERE such as 'Austin, TX'")
                //                .tags(tags)
                //                .teaser("YOU TEASER HERE")
                //                .permalink("DESIRED PERMALINK HERE")
                //                .links(links)
                .build();

        curations.postContentToCurations(request, this);
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
            chosenImageView = (ImageView) findViewById(R.id.curationsPostImageView);
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
                    Picasso.with(this).load(imageUri).into(this);
                } catch (Exception e) {e.printStackTrace();

                }

            }
        }else{
            finish();
        }
    }
}
