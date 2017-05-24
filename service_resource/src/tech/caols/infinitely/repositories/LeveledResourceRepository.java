package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.LeveledResourceData;
import tech.caols.infinitely.db.Repository;

import java.util.List;
import java.util.StringJoiner;

public class LeveledResourceRepository extends Repository<LeveledResourceData, Long> {

    public LeveledResourceRepository() {
        super(LeveledResourceData.class);
    }

    public boolean deleteInBatch(List<Long> ids) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        ids.stream().map(id -> id + "").forEach(stringJoiner::add);
        return super.remove(String.format("Delete From LeveledResourceData a Where a.id in %s", stringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
