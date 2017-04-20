package com.bazaarvoice.bvsdkdemoandroid.curations.detail;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvandroidsdk.CurationsProduct;
import com.bazaarvoice.bvandroidsdk.CurationsVideo;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoFancyProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoCurationsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoCurationsDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ITEM_PARAM = "param1";
    private static final String IDX_PARAM = "param2";
    private static final String ARG_SHOW_LEFT_ARROW = "arg_show_left_arrow";
    private static final String ARG_SHOW_RIGHT_ARROW = "arg_show_right_arrow";

    private CurationsFeedItem feedItem;
    private DemoCurationsProductsAdapter adapter;
    private int itemIDX;
    private boolean showLeftArrow, showRightArrow;

    @Inject Picasso picasso;
    @Inject Gson gson;

    public static DemoCurationsDetailFragment newInstance(Gson gson, CurationsFeedItem update, int itemIDX, boolean showLeftArrow, boolean showRightArrow) {
        DemoCurationsDetailFragment fragment = new DemoCurationsDetailFragment();
        Bundle args = new Bundle();
        args.putInt(IDX_PARAM, itemIDX);
        args.putString(ITEM_PARAM, gson.toJson(update));
        args.putBoolean(ARG_SHOW_LEFT_ARROW, showLeftArrow);
        args.putBoolean(ARG_SHOW_RIGHT_ARROW, showRightArrow);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApp.getAppComponent(getContext()).inject(this);
        if (getArguments() != null) {
            feedItem = gson.fromJson(getArguments().getString(ITEM_PARAM), CurationsFeedItem.class);
            itemIDX = getArguments().getInt(IDX_PARAM);
            showLeftArrow = getArguments().getBoolean(ARG_SHOW_LEFT_ARROW);
            showRightArrow = getArguments().getBoolean(ARG_SHOW_RIGHT_ARROW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.frag_curations_detail, container, false);


        setupSocialPostPreview(view);

        ImageButton lBtn = (ImageButton) view.findViewById(R.id.leftBtn);
        if (showLeftArrow) {
            lBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBackUpdate();
                }
            });
            lBtn.setVisibility(View.VISIBLE);
        } else {
            lBtn.setVisibility(View.GONE);
        }

        ImageButton rBtn = (ImageButton) view.findViewById(R.id.rightBtn);
        if (showRightArrow){
            rBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goForwardUpdate();
                }
            });
            rBtn.setVisibility(View.VISIBLE);
        } else {
            rBtn.setVisibility(View.GONE);
        }

        ImageView profPic = (ImageView) view.findViewById(R.id.profPic);
        picasso.load(feedItem.getAuthor().getAvatarUrl())
                .placeholder(R.drawable.placeholderimg)
                .resizeDimen(R.dimen.side_not_set, R.dimen.product_image_height_detail)
                .into(profPic);

        TextView name = (TextView) view.findViewById(R.id.curations_name_field);
        name.setText(feedItem.getAuthor().getAlias());

        PrettyTime prettyTime = new PrettyTime();
        TextView time = (TextView) view.findViewById(R.id.curations_last_updated_field);
        time.setText(prettyTime.format(new Date(System.currentTimeMillis() - feedItem.getTimestamp())));

        TextView uName = (TextView) view.findViewById(R.id.user_name_field);
        if (feedItem.getAuthor().getUsername() != null) {
            uName.setText(Html.fromHtml("<a href='" + feedItem.getAuthor().getProfileUrl() + "'>" + feedItem.getAuthor().getUsername() + "</a>"));
            uName.setClickable(true);
            uName.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            uName.setText("");
        }

        setupHyperlinkedDescription(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.curations_related_products_list);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setFocusable(false);
        adapter = new DemoCurationsProductsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setProducts(feedItem.getProducts());
        adapter.setListener(new DemoCurationsProductsAdapter.CurationsProductsOnClickListener() {
            @Override
            public void onCurationsProductClicked(CurationsProduct product) {
                if (!getActivity().isFinishing() && product!=null && !TextUtils.isEmpty(product.getId())) {
                    boolean productIsCached = DemoDisplayableProductsCache.getInstance().getDataItem(product.getId()) != null;
                    if (productIsCached) {
                        DemoFancyProductDetailActivity.transitionTo(getActivity(), product.getId());
                    } else {
                        DemoFancyProductDetailActivity.transitionTo(getActivity(), product.getId(), product.getDisplayName(), product.getDisplayImageUrl());
                    }
                }
            }
        });

        LinearLayout shopNowContent = (LinearLayout) view.findViewById(R.id.shopNowContent);
        if (feedItem.getProducts().size() == 0){
            shopNowContent.setVisibility(View.GONE);
        }else {
            shopNowContent.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private String formatTextWithAnchorTags(String original, Pattern pattern, String anchorUrlformat){

        if (pattern == null){
            return "";
        }

        String formattedString = "";
        Matcher matcher = pattern.matcher(original);
        int current = 0;
        while (matcher.find()){
            formattedString += original.substring(current, matcher.start());
            String href = String.format(anchorUrlformat,matcher.group(2));
            formattedString += "<a href = '" + href + "'>"+matcher.group(0)+ "</a>";
            current = matcher.end();
        }
        formattedString += original.substring(current, original.length());
        return formattedString;
    }

    private void goBackUpdate(){
        DemoCurationsDetailActivity demoCurationsDetailActivity = (DemoCurationsDetailActivity) getActivity();
        if (!demoCurationsDetailActivity.isFinishing()) {
            ((DemoCurationsDetailActivity) getActivity()).goBackUpdate();
        }
    }

    private void goForwardUpdate(){
        DemoCurationsDetailActivity demoCurationsDetailActivity = (DemoCurationsDetailActivity) getActivity();
        if (!demoCurationsDetailActivity.isFinishing()) {
            ((DemoCurationsDetailActivity) getActivity()).goForwardUpdate();
        }
    }

    private void setupSocialPostPreview(final View view){
        final ImageView imageView = (ImageView) view.findViewById(R.id.socialPic);
        final DemoCustomVideoView videoView = (DemoCustomVideoView) view.findViewById(R.id.socialVid);
        final WebView webView = (WebView) view.findViewById(R.id.socialWeb);

        if (feedItem.getPhotos().size() > 0) {
            CurationsPhoto photo = feedItem.getPhotos().get(0);
            String curationsPhotoUrl = photo.getImageServiceUrl() + "&width=" + (DemoUtils.MAX_IMAGE_WIDTH/4) + "&height=" + (DemoUtils.MAX_IMAGE_HEIGHT/4);
            picasso.load(curationsPhotoUrl)
                    .placeholder(R.drawable.placeholderimg)
                    .resizeDimen(R.dimen.side_not_set, R.dimen.product_image_height_detail)
                    .into(imageView);
            videoView.setVisibility(View.GONE);
        }else if (feedItem.getVideos().size() > 0) {

            imageView.setVisibility(View.GONE);
            final CurationsVideo video = feedItem.getVideos().get(0);

            if (video.getOrigin().equalsIgnoreCase("youtube")) {
                videoView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.getSettings().setJavaScriptEnabled(true);

                final ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                        Pattern dimensions = Pattern.compile("(width=\"|height=\")([0-9]+)\"");
                        Matcher matcher = dimensions.matcher(video.getCode());

                        int h = 0;
                        int w = 0;

                        while (matcher.find()) {
                            if (matcher.group(1).contains("width")){
                                w = Integer.parseInt(matcher.group(2));
                            }else{
                                h = Integer.parseInt(matcher.group(2));
                            }
                        }

                        int newW = (int) ((webView.getWidth()/ displayMetrics.density) - 10);
                        int newH = (h * newW) / w;

                        String html = "<iframe class=\"video-player youtube-player\" type=\"text/html\" width=\""+ newW +"\" height = \"" + newH + "\" src=\"https://www.youtube.com/embed/" + video.getToken() +
                                "\"  frameborder=\"0\" > </iframe> ";
                        webView.loadData(html, "text/html", null);

                        if (Build.VERSION.SDK_INT < 16) {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                };

                view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
            } else {
                webView.setVisibility(View.GONE);
                MediaController mediaController = new MediaController(videoView.getContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);

                String tmp;

                if (video.getRemoteUrl() != null) {
                    tmp = video.getRemoteUrl();
                } else {
                    tmp = video.getPermalink();
                }

                final String url = tmp;
                videoView.setVideoURI(Uri.parse(url));
                videoView.start();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        int width = videoView.getWidth();
                        int height = (mp.getVideoHeight() * width) / mp.getVideoWidth();
                        videoView.setDimensions(width, height);
                    }
                });
            }
        }
    }

    private void setupHyperlinkedDescription(final View view){

        String origin = null;
        if (feedItem.getPhotos().size() > 0) {
            CurationsPhoto photo = feedItem.getPhotos().get(0);
            origin = photo.getOrigin();
        }else if (feedItem.getVideos().size() > 0) {

            final CurationsVideo video = feedItem.getVideos().get(0);
            origin = video.getOrigin();
        }

        String baseSocialTagUrl = "";
        String baseSocialProfileUrl = "";

        Pattern hashTags;
        Pattern atProfiles = null;

        hashTags = Pattern.compile("(\\s|\\A)*#(\\w+)?");

        if (origin.equalsIgnoreCase("instagram")){

            baseSocialProfileUrl = "https://instagram.com/%s/";
            baseSocialTagUrl = "https://instagram.com/explore/tags/%s/";
            atProfiles = Pattern.compile("(\\s|\\A)*@(\\w+)?");

        }else if (origin.equalsIgnoreCase("twitter")){

            baseSocialProfileUrl = "https://twitter.com/%s";
            baseSocialTagUrl = "https://twitter.com/hashtag/%s?src=hash";
            atProfiles = Pattern.compile("(\\s|\\A)*@(\\w+)?");

        }else if (origin.equalsIgnoreCase("google-plus")){

            baseSocialTagUrl = "https://plus.google.com/s/%%23%s/top";
            baseSocialProfileUrl = "https://plus.google.com/+%s";
            atProfiles = Pattern.compile("(\\s|\\A)*\\+(\\w+)?");

        }else if (origin.equalsIgnoreCase("youtube")) {
            atProfiles = Pattern.compile("(\\s|\\A)*@(\\w+)?");
            baseSocialProfileUrl = "https://youtube.com";
            baseSocialTagUrl = "https://youtube.com/";
        }

        String profileFormatted = formatTextWithAnchorTags(feedItem.getText(), atProfiles, baseSocialProfileUrl);
        String fullFormat = formatTextWithAnchorTags(profileFormatted, hashTags, baseSocialTagUrl);

        String[] components = fullFormat.split(" ");
        fullFormat = fullFormat.replace(components[components.length -1],  "<a href='https://" + components[components.length - 1] + "'> " + components[components.length - 1] + "</a>");

        TextView desc = (TextView) view.findViewById(R.id.curations_image_desc_field);
        desc.setText(Html.fromHtml(fullFormat));
        desc.setClickable(true);
        desc.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
