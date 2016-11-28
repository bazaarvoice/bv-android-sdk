package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface for common properties that are used to display bv product content
 */

public interface BVDisplayableProductContent {

    @NonNull
    String getId();

    @Nullable
    String getDisplayName();

    @Nullable
    String getDisplayImageUrl();

    float getAverageRating();
}
