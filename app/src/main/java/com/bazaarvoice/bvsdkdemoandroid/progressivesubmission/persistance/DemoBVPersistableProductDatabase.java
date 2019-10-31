package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = DemoBVPersistableProductContent.class, version = 1)

public abstract class DemoBVPersistableProductDatabase extends RoomDatabase {
    public abstract DemoBVPersistableProductDao demoBVPersistableProductContentDao();
}