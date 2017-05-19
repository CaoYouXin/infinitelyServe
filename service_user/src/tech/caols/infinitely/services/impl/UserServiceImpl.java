package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepository();

    @Override
    public List<UserView> listAllUsers() {
        return this.userRepository.findAll().stream().map(userData -> {
            UserView userView = new UserView();
            BeanUtils.copyBean(userData, userView);
            return userView;
        }).collect(Collectors.toList());
    }

    @Override
    public UserLoginView login(String userName, String password) {
        return null;
    }

    @Override
    public UserLoginView register(UserLoginView userLoginView) {
        return null;
    }

    @Override
    public String captcha(String phone) {
        return null;
    }

    @Override
    public UserLoginView findPassword(UserLoginView userLoginView) {
        return null;
    }

    @Override
    public UserLoginView resetPassword(UserLoginView userLoginView) {
        return null;
    }
}
