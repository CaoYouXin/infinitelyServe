package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.services.impl.UserServiceImpl;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserNameAndPwd;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;
import java.util.Map;

@Rest
public class UserController {

    private final UserService userService = new UserServiceImpl();

    @RestAPI(name = "list_all_users", url = "/user/list", target = RestTarget.GET)
    public List<UserView> listAllUsers() {
        return this.userService.listAllUsers();
    }

    @RestAPI(name = "captcha", url = "/captcha", target = RestTarget.GET)
    public String captcha(Map<String, String> parameters) {
        return this.userService.captcha(parameters.get("phone"));
    }

    @RestAPI(name = "login", url = "/user/login", target = RestTarget.POST)
    public UserLoginView login(UserNameAndPwd userNameAndPwd, HttpResponse response) {
        return this.userService.login(userNameAndPwd.getUserName(), userNameAndPwd.getPassword(), response);
    }

}
