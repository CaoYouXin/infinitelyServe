package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.server.PreReq;
import tech.caols.infinitely.server.PreRes;
import tech.caols.infinitely.services.UserFavourLevelService;
import tech.caols.infinitely.services.impl.UserFavourLevelServiceImpl;

@Rest
public class UserFavourLevelController {

    private UserFavourLevelService userFavourLevelService = new UserFavourLevelServiceImpl();

    @RestAPI(name = "set_user_level", url = "/favour/level/set", target = RestTarget.POST)
    public PreRes setUserLevel(PreReq preReq, HttpResponse response) {
        return this.userFavourLevelService.userFavourLevel(preReq, response);
    }

}
