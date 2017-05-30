package tech.caols.infinitely.services;

public interface FavourSetterService {

    Boolean setFavourValue(Long userId, int value);

    Integer addFavourValue(Long userId, int value);

}
