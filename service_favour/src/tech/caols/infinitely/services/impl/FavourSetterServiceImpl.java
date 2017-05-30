package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.services.FavourSetterService;

public class FavourSetterServiceImpl implements FavourSetterService {

    @Override
    public Boolean setFavourValue(Long userId, int value) {
            
        return false;
    }

    @Override
    public Integer addFavourValue(Long userId, int value) {
        return 0;
    }

}
