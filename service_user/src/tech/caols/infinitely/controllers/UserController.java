package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.CaptchaService;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.services.impl.CaptchaServiceImpl;
import tech.caols.infinitely.services.impl.UserServiceImpl;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserNameAndPwd;
import tech.caols.infinitely.viewmodels.UserRegisterView;
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

    @RestAPI(name = "login", url = "/user/login", target = RestTarget.POST)
    public UserLoginView login(UserNameAndPwd userNameAndPwd, HttpResponse response) {
        return this.userService.login(userNameAndPwd.getUserName(), userNameAndPwd.getPassword(), response);
    }

    @RestAPI(name = "check_username", url="/username/check", target = RestTarget.GET)
    public boolean checkUserName(Map<String, String> parameters) {
        return this.userService.userNameCheck(parameters.get("username"));
    }

    @RestAPI(name = "register", url = "/user/register", target = RestTarget.POST)
    public UserLoginView register(UserRegisterView userRegisterView, HttpResponse response) {
        return this.userService.register(userRegisterView, response);
    }

    @RestAPI(name = "find_password", url = "/password/find", target = RestTarget.GET)
    public UserView findPassword(Map<String, String> parameter, HttpResponse response) {
        return this.userService.findPassword(parameter.get("phone"), response);
    }

    @RestAPI(name = "reset_password", url = "/password/reset", target = RestTarget.POST)
    public UserLoginView resetPassword(UserRegisterView userRegisterView, HttpResponse response) {
        return this.userService.resetPassword(userRegisterView, response);
    }

}
