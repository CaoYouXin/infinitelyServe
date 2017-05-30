package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.FavourSetterService;
import tech.caols.infinitely.services.impl.FavourSetterServiceImpl;

import java.util.Map;

@Rest
public class FavourController {

    private FavourSetterService favourSetterService = new FavourSetterServiceImpl();

    @RestAPI(name = "set_favour_value", url = "/value/set", target = RestTarget.GET)
    public Boolean setFavourValue(Map<String, String> parameters) {
        return this.favourSetterService.setFavourValue(
                Long.parseLong(parameters.get("userId")),
                Integer.parseInt(parameters.get("value"))
        );
    }

}
