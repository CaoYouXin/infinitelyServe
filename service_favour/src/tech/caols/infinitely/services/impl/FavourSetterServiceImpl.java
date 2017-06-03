package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.FavourData;
import tech.caols.infinitely.repositories.FavourRepository;
import tech.caols.infinitely.repositories.UserFavourDetailRepository;
import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.FavourSetterService;
import tech.caols.infinitely.viewmodels.UserFavourView;

import java.util.List;
import java.util.stream.Collectors;

public class FavourSetterServiceImpl implements FavourSetterService {

    private FavourRepository favourRepository = new FavourRepository();
    private UserFavourDetailRepository userFavourDetailRepository = new UserFavourDetailRepository();

    @Override
    public List<UserFavourView> setFavourValue(Long userId, int value, HttpResponse response) {
        FavourData favourData = this.favourRepository.findByUserId(userId);
        if (null == favourData) {
            favourData = new FavourData();
            favourData.setUserId(userId);
        }
        favourData.setValue(value);

        if (!this.favourRepository.save(favourData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存用户好感度失败！"));
            return null;
        }

        return this.listFavourValue();
    }

    @Override
    public List<UserFavourView> listFavourValue() {
        return this.userFavourDetailRepository.findAll().stream()
                .map(userFavourDetailData -> {
                    UserFavourView userFavourView = new UserFavourView();
                    BeanUtils.copyBean(userFavourDetailData, userFavourView);
                    return userFavourView;
                }).collect(Collectors.toList());
    }

}
