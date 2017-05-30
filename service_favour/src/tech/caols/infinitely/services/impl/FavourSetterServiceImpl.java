package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.FavourData;
import tech.caols.infinitely.repositories.FavourRepository;
import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.FavourSetterService;

public class FavourSetterServiceImpl implements FavourSetterService {

    private FavourRepository favourRepository = new FavourRepository();

    @Override
    public synchronized Boolean setFavourValue(Long userId, int value) {
        FavourData favourData = this.favourRepository.findByUserId(userId);
        if (null == favourData) {
            favourData = new FavourData();
            favourData.setUserId(userId);
        }
        favourData.setValue(value);

        return this.favourRepository.save(favourData);
    }

    @Override
    public synchronized Integer addFavourValue(Long userId, int value, HttpResponse response) {
        FavourData favourData = this.favourRepository.findByUserId(userId);
        if (null == favourData) {
            favourData = new FavourData();
            favourData.setUserId(userId);
            favourData.setValue(0);
        }

        int newValue = favourData.getValue() + value;
        favourData.setValue(newValue);
        if (!this.favourRepository.save(favourData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("更新好感度失败！"));
            return null;
        }

        return newValue;
    }

}
