package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;

public interface UserService {

    List<UserView> listAllUsers();

    UserLoginView login(String userName, String password, HttpResponse response);

    UserLoginView register(UserView userView);

    String captcha(String phone);

    UserLoginView findPassword(UserView userView);

    UserLoginView resetPassword(UserView userView);

}
