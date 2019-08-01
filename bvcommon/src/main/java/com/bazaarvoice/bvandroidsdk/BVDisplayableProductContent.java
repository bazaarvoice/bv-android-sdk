package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
