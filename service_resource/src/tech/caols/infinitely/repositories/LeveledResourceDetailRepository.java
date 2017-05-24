package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.LeveledResourceDetailData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class LeveledResourceDetailRepository extends Repository<LeveledResourceDetailData, Long> {

    public LeveledResourceDetailRepository() {
        super(LeveledResourceDetailData.class);
    }

    public List<LeveledResourceDetailData> findAll() {
        return super.query("Select lrd.id LeveledResourceDetailData.id, lrd.name LeveledResourceDetailData.name," +
                        " lrd.levelId LeveledResourceDetailData.levelId, ld.name LeveledResourceDetailData.levelName" +
                        " From LevelData ld, LeveledResourceData lrd Where ld.id = lrd.levelId",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
