package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.Constants;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.datamodels.FavourData;
import tech.caols.infinitely.datamodels.FavourResourceMapDetailData;
import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.repositories.ConfigsRepository;
import tech.caols.infinitely.repositories.FavourRepository;
import tech.caols.infinitely.repositories.FavourResourceMapDetailRepository;
import tech.caols.infinitely.repositories.TokenRepository;
import tech.caols.infinitely.server.PreReq;
import tech.caols.infinitely.server.PreRes;
import tech.caols.infinitely.services.UserFavourLevelService;

import java.util.List;
import java.util.StringJoiner;

public class UserFavourLevelServiceImpl implements UserFavourLevelService {

    private ConfigsRepository configsRepository = new ConfigsRepository();
    private TokenRepository tokenRepository = new TokenRepository();
    private FavourRepository favourRepository = new FavourRepository();
    private FavourResourceMapDetailRepository favourResourceMapDetailRepository = new FavourResourceMapDetailRepository();

    @Override
    public PreRes userFavourLevel(PreReq preReq) {
        PreRes preRes = new PreRes();

        String userToken = preReq.getParameters().get("user_token");
        if (null == userToken) {
            Configs byKey = this.configsRepository.findByKey(ConfigsKeys.DefaultUserLevel);
            if (null != byKey) {
                preRes.appendSet(Constants.USER_LEVELS, byKey.getValue());
            }
            return preRes;
        }

        Token tokenByToken = this.tokenRepository.findTokenByToken(userToken);
        if (null == tokenByToken) {
            preRes.setCode(Constants.CODE_INVALID);
            return preRes;
        }

        FavourData byUserId = this.favourRepository.findByUserId(tokenByToken.getUserId());
        if (null == byUserId) {
            preRes.setCode(Constants.CODE_INVALID);
            return preRes;
        }

        List<FavourResourceMapDetailData> allLowerThan = this.favourResourceMapDetailRepository.findAllLowerThan(byUserId.getValue());
        StringJoiner stringJoiner = new StringJoiner(",");
        for (FavourResourceMapDetailData favourResourceMapDetailData : allLowerThan) {
            stringJoiner.add(favourResourceMapDetailData.getResourceLevelName());
        }

        Configs byKey = this.configsRepository.findByKey(ConfigsKeys.DefaultUserLevel);
        if (null != byKey) {
            stringJoiner.add(byKey.getValue());
        }

        preRes.appendSet(Constants.USER_LEVELS, stringJoiner.toString())
                .setCode(Constants.CODE_VALID);
        return preRes;
    }

}
