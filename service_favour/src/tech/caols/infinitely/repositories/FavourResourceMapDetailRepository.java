package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.FavourResourceMapDetailData;
import tech.caols.infinitely.db.Repository;
import tech.caols.infinitely.services.FavourResourceMapService;

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

}
