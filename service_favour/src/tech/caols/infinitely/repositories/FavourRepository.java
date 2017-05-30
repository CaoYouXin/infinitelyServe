package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.FavourData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class FavourRepository extends Repository<FavourData, Long> {

    public FavourRepository() {
        super(FavourData.class);
    }

    public FavourData findByUserId(Long userId) {
        List<FavourData> favourDataList = super.query("Select a From FavourData a Where a.userId = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, userId);

        if (!favourDataList.isEmpty()) {
            return favourDataList.get(0);
        }
        return null;
    }

}
