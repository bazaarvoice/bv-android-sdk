/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvsdkdemoandroid.author;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Badge;
import com.bazaarvoice.bvandroidsdk.Pin;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoAuthorView extends ConstraintLayout implements DemoAuthorContract.View {
    private DemoAuthorContract.Presenter presenter;

    @BindView(R.id.authorNicknameTv) TextView authorNicknameTv;
    @BindView(R.id.authorLocationTv) TextView authorLocationTv;
    @BindView(R.id.badgeContainer) LinearLayout badgeContainer;

    @BindView(R.id.reviewBody) TextView recentReviewBody;
    @BindView(R.id.reviewProductImage) ImageView recentReviewImage;
    @BindView(R.id.reviewPostedDateTv) TextView recentReviewPostedDateTv;
    @BindView(R.id.reviewRating) RatingBar recentReviewRating;

    public DemoAuthorView(Context context, AttributeSet attrs) {
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
    public void setPresenter(DemoAuthorContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showAuthorNickname(String authorNickname) {
        authorNicknameTv.setText(authorNickname);
    }

    @Override
    public void showAuthorLocation(String authorLocation) {
        authorLocationTv.setText(authorLocation);
    }

    @Override
    public void showAuthorBadges(List<Badge> badges) {
        for (int i=0; i<badges.size(); i++) {
            Badge currBadge = badges.get(i);
            TextView badgeTv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_badge, badgeContainer, false);
            badgeTv.setText(currBadge.getId());
            badgeContainer.addView(badgeTv);

            if (i<(badges.size()-1)) {
                View spacerView = new View(getContext());
                spacerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                badgeContainer.addView(spacerView);
            }
        }
    }

    @Override
    public void showProductsToReview(List<Pin> pins) {

    }

    @Override
    public void showRecentReviewImage(String imageUrl) {
        Picasso.with(getContext()).load(imageUrl).into(recentReviewImage);
    }

    @Override
    public void showRecentReviewRating(float rating) {
        recentReviewRating.setNumStars(5);
        recentReviewRating.setRating(rating);
    }

    @Override
    public void showRecentReviewBody(String body) {
        recentReviewBody.setText(body);
    }

    @Override
    public void showRecentReviewTimePosted(String timePosted) {
        String formattedStr = String.format("Posted %s", timePosted);
        recentReviewPostedDateTv.setText(formattedStr);
    }

    @OnClick(R.id.allReviewsButton)
    public void onAllReviewButtonTapped() {

    }
}
