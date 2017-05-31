package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.FavourResourceMapDetailData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class FavourResourceMapDetailRepository extends Repository<FavourResourceMapDetailData, Long> {

    public FavourResourceMapDetailRepository() {
        super(FavourResourceMapDetailData.class);
    }

    public List<FavourResourceMapDetailData> findAll() {
        return super.query("Select a.id FavourResourceMapDetailData.id," +
                        " a.favourValue FavourResourceMapDetailData.favourValue," +
                        " a.resourceLevelId FavourResourceMapDetailData.resourceLevelId," +
                        " b.name FavourResourceMapDetailData.resourceLevelName" +
                        " From FavourResourceMapData a, LevelData b Where a.resourceLevelId = b.id",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    public List<FavourResourceMapDetailData> findAllLowerThan(int upThreshold) {
        return super.query("Select a.id FavourResourceMapDetailData.id," +
                        " a.favourValue FavourResourceMapDetailData.favourValue," +
                        " a.resourceLevelId FavourResourceMapDetailData.resourceLevelId," +
                        " b.name FavourResourceMapDetailData.resourceLevelName" +
                        " From FavourResourceMapData a, LevelData b Where a.resourceLevelId = b.id" +
                        " and a.favourValue <= ?",
                new String[]{"tech.caols.infinitely.datamodels."}, upThreshold);
    }
}
