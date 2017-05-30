package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;

public interface FavourSetterService {

    Boolean setFavourValue(Long userId, int value);

    Integer addFavourValue(Long userId, int value, HttpResponse response);

}
