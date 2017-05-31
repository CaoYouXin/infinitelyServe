package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.server.PostReq;
import tech.caols.infinitely.services.FavourValueCollectorService;
import tech.caols.infinitely.services.impl.FavourValueCollectorServiceImpl;

@Rest
public class FavourValueCollectorController {

    private FavourValueCollectorService favourValueCollectorService = new FavourValueCollectorServiceImpl();

    @RestAPI(name = "collect_1", url = "/collect/1", target = RestTarget.POST)
    public Object collect1(PostReq postReq) {
        this.favourValueCollectorService.addFavourValue1(postReq);
        return 1;
    }

}
