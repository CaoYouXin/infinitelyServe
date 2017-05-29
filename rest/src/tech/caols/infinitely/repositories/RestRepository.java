package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.RestRecord;
import tech.caols.infinitely.db.Repository;

public class RestRepository extends Repository<RestRecord, Long> {

    public RestRepository() {
        super(RestRecord.class);
    }

}
