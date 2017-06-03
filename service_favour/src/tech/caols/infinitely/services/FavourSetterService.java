package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.UserFavourView;

import java.util.List;

public interface FavourSetterService {

    List<UserFavourView> setFavourValue(Long userId, int value, HttpResponse response);

    List<UserFavourView> listFavourValue();

}
