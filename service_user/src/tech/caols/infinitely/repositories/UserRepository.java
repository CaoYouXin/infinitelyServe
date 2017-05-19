package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.db.Repository;

public class UserRepository extends Repository<UserData, Long> {

    public UserRepository() {
        super(UserData.class);
    }

}
