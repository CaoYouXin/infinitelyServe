package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.services.impl.UserServiceImpl;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;

@Rest
public class UserController {

    private final UserService userService = new UserServiceImpl();

    @RestAPI(name = "list_all_users", url = "/user/list", target = RestTarget.GET)
    public List<UserView> listAllUsers() {
        return this.userService.listAllUsers();
    }

}
