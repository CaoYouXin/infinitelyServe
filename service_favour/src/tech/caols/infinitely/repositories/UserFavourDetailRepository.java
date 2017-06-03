package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.UserFavourDetailData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class UserFavourDetailRepository extends Repository<UserFavourDetailData, Long> {

    public UserFavourDetailRepository() {
        super(UserFavourDetailData.class);
    }

    public List<UserFavourDetailData> findAll() {
        return super.query("Select a.id UserFavourDetailData.id, a.userId UserFavourDetailData.userId," +
                        " a.value UserFavourDetailData.value, b.userName UserFavourDetailData.userName" +
                        " From FavourData a, UserData b Where a.userId = b.id",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
