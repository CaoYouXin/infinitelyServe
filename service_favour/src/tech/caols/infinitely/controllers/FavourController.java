package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.FavourSetterService;
import tech.caols.infinitely.services.impl.FavourSetterServiceImpl;
import tech.caols.infinitely.viewmodels.UserFavourSetReq;
import tech.caols.infinitely.viewmodels.UserFavourView;

import java.util.List;
import java.util.Map;

@Rest
public class FavourController {

    private FavourSetterService favourSetterService = new FavourSetterServiceImpl();

    @RestAPI(name = "set_favour_value", url = "/value/set", target = RestTarget.POST)
    public List<UserFavourView> setFavourValue(UserFavourSetReq userFavourSetReq, HttpResponse response) {
        return this.favourSetterService.setFavourValue(userFavourSetReq.getUserId(), userFavourSetReq.getValue(), response);
    }

    @RestAPI(name = "list_favour_value", url = "/value/list", target = RestTarget.GET)
    public List<UserFavourView> listFavourValue() {
        return this.favourSetterService.listFavourValue();
    }

}
