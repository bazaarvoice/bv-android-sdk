package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DemoBVPersistableProductDao {

    @Query("SELECT * FROM reviewable_products_table")
    List<DemoBVPersistableProductContent> getAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DemoBVPersistableProductContent productContent);

    @Query("DELETE FROM reviewable_products_table WHERE :productId = id")
    void delete(String productId);
}
