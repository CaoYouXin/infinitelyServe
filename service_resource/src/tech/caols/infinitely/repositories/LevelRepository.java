package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.LevelData;
import tech.caols.infinitely.db.Repository;

import java.util.List;
import java.util.StringJoiner;

public class LevelRepository extends Repository<LevelData, Long> {

    public LevelRepository() {
        super(LevelData.class);
    }

    public boolean deleteInBatch(List<Long> ids) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        ids.stream().map(id -> id + "").forEach(stringJoiner::add);
        return super.remove(String.format("Delete From LevelData a Where a.id in %s", stringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
