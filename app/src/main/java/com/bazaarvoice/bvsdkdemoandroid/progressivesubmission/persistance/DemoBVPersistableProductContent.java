package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviewable_products_table")
public class DemoBVPersistableProductContent {

    public DemoBVPersistableProductContent(@NonNull String id, String displayName, String displayImageUrl, float averageRating) {
        this.id = id;
        this.displayName = displayName;
        this.displayImageUrl = displayImageUrl;
        this.averageRating = averageRating;
    }

    @PrimaryKey @NonNull
    public String id;
    @ColumnInfo(name = "product_name") public  String displayName;
    @ColumnInfo(name = "image_url") public String displayImageUrl;
    @ColumnInfo(name = "average_rating") public float averageRating;

}