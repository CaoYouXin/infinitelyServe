package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.datamodels.FavourData;
import tech.caols.infinitely.datamodels.FavourValueCollectRecord;
import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.repositories.FavourRepository;
import tech.caols.infinitely.repositories.FavourValueCollectRecordRepository;
import tech.caols.infinitely.repositories.TokenRepository;
import tech.caols.infinitely.server.PostReq;
import tech.caols.infinitely.services.FavourValueCollectorService;

public class FavourValueCollectorServiceImpl implements FavourValueCollectorService {

    private FavourRepository favourRepository = new FavourRepository();
    private TokenRepository tokenRepository = new TokenRepository();
    private FavourValueCollectRecordRepository favourValueCollectRecordRepository = new FavourValueCollectRecordRepository();

    @Override
    public void addFavourValue1(PostReq postReq) {

        add(postReq, 1);

    }

    @Override
    public void addFavourValue2(PostReq postReq) {

        add(postReq, 2);

    }

    private void add(PostReq postReq, int add) {
        Long userId = this.getUserId(postReq);
        if (null == userId) {
            return;
        }

        int byUserIdAndUrl = this.favourValueCollectRecordRepository.findByUserIdAndUrl(userId, postReq.getUrl());
        if (byUserIdAndUrl > 0) {
            return;
        }

        if (this.addFavourValue(userId, add)) {
            FavourValueCollectRecord favourValueCollectRecord = new FavourValueCollectRecord();
            favourValueCollectRecord.setUserId(userId);
            favourValueCollectRecord.setUrl(postReq.getUrl());
            favourValueCollectRecord.setValue(add);
            this.favourValueCollectRecordRepository.save(favourValueCollectRecord);
        }
    }

    private Long getUserId(PostReq postReq) {
        String userToken = postReq.getParameters().get("user_token");
        if (null == userToken) {
            return null;
        }

        Token tokenByToken = this.tokenRepository.findTokenByToken(userToken);
        if (null == tokenByToken) {
            return null;
        }

        return tokenByToken.getUserId();
    }

    private boolean addFavourValue(Long userId, int value) {
        FavourData favourData = this.favourRepository.findByUserId(userId);
        if (null == favourData) {
            favourData = new FavourData();
            favourData.setUserId(userId);
            favourData.setValue(0);
        }

        int newValue = favourData.getValue() + value;
        favourData.setValue(newValue);
        return this.favourRepository.save(favourData);
    }

}
