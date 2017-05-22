package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserRegisterView;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;

public interface UserService {

    List<UserView> listAllUsers();

    UserLoginView login(String userName, String password, HttpResponse response);

    UserLoginView register(UserRegisterView userRegisterView, HttpResponse response);

    UserView findPassword(String phone, HttpResponse response);

    UserLoginView resetPassword(UserRegisterView userRegisterView, HttpResponse response);

    boolean userNameCheck(String userName);

    boolean phoneCheck(String phone);
}
