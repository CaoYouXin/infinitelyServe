package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.base.BaseServiceImpl;
import tech.caols.infinitely.datamodels.FavourResourceMapData;
import tech.caols.infinitely.repositories.FavourResourceMapDetailRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.services.FavourResourceMapService;
import tech.caols.infinitely.viewmodels.FavourResourceMapView;

import java.util.List;
import java.util.stream.Collectors;

public class FavourResourceMapServiceImpl extends BaseServiceImpl<FavourResourceMapData, FavourResourceMapView> implements FavourResourceMapService {

    private FavourResourceMapDetailRepository favourResourceMapDetailRepository = new FavourResourceMapDetailRepository();

    public FavourResourceMapServiceImpl() {
        super(FavourResourceMapData.class, FavourResourceMapView.class);
    }

    @Override
    public List<FavourResourceMapView> list() {
        return this.favourResourceMapDetailRepository.findAll().stream().map(favourResourceMapDetailData -> {
            FavourResourceMapView favourResourceMapView = new FavourResourceMapView();
            BeanUtils.copyBean(favourResourceMapDetailData, favourResourceMapView);
            return favourResourceMapView;
        }).collect(Collectors.toList());
    }
}
