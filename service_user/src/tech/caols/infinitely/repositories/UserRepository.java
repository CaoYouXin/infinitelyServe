package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class UserRepository extends Repository<UserData, Long> {

    public UserRepository() {
        super(UserData.class);
    }

    public UserData findUserByUserNameAndPassword(String userName, String password) {
        List<UserData> userDataList = super.query("Select a From UserData a Where a.userName = ? and a.password = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, userName, password);
        if (userDataList.size() > 0) {
            return userDataList.get(0);
        }
        return null;
    }

    public UserData findUserByUserName(String userName) {
        List<UserData> userDataList = super.query("Select a From UserData a Where a.userName = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, userName);
        if (userDataList.size() > 0) {
            return userDataList.get(0);
        }
        return null;
    }

}
