package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class ConfigsRepository extends Repository<Configs, Long> {

    public ConfigsRepository() {
        super(Configs.class);
    }

    public Configs findByKey(String key) {
        List<Configs> configsList = super.query("Select a From Configs a Where a.key = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, key);

        if (configsList.size() > 0) {
            return configsList.get(0);
        }
        return null;
    }

}
