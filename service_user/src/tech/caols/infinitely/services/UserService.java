package tech.caols.infinitely.services;

import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;

public interface UserService {

    List<UserView> listAllUsers();

    UserLoginView login(String userName, String password);

    UserLoginView register(UserLoginView userLoginView);

    String captcha(String phone);

    UserLoginView findPassword(UserLoginView userLoginView);

    UserLoginView resetPassword(UserLoginView userLoginView);

}
