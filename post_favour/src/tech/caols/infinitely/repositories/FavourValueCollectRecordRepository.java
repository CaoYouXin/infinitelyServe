package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.FavourValueCollectRecord;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class FavourValueCollectRecordRepository extends Repository<FavourValueCollectRecord, Long> {

    public FavourValueCollectRecordRepository() {
        super(FavourValueCollectRecord.class);
    }

    public int findByUserIdAndUrl(Long userId, String url) {
        List<FavourValueCollectRecord> favourValueCollectRecordList = super.query("Select a From FavourValueCollectRecord a Where a.userId = ? and a.url = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, userId, url);

        int value = 0;
        for (FavourValueCollectRecord favourValueCollectRecord : favourValueCollectRecordList) {
            value += favourValueCollectRecord.getValue();
        }
        return value;
    }

}
