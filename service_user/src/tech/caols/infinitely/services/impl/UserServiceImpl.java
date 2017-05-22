package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.datamodels.Captcha;
import tech.caols.infinitely.datamodels.ImageCaptcha;
import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.repositories.CaptchaRepository;
import tech.caols.infinitely.repositories.ImageCaptchaRepository;
import tech.caols.infinitely.repositories.TokenRepository;
import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.CaptchaService;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.viewmodels.UserLoginView;
import tech.caols.infinitely.viewmodels.UserRegisterView;
import tech.caols.infinitely.viewmodels.UserView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository = new UserRepository();
    private CaptchaRepository captchaRepository = new CaptchaRepository();
    private TokenRepository tokenRepository = new TokenRepository();
    private ImageCaptchaRepository imageCaptchaRepository = new ImageCaptchaRepository();

    private CaptchaService captchaService = new CaptchaServiceImpl();

    @Override
    public List<UserView> listAllUsers() {
        return this.userRepository.findAll().stream().map(userData -> {
            UserView userView = new UserView();
            BeanUtils.copyBean(userData, userView);
            return userView;
        }).collect(Collectors.toList());
    }

    @Override
    public UserLoginView login(String userName, String password, HttpResponse response) {
        UserData userData = this.userRepository.findUserByUserName(userName);
        if (userData == null) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_NO_USER, null));
            return null;
        }

        if (!userData.getPassword().equals(password)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_WRONG_PWD, null));
            return null;
        }

        Token token = this.tokenRepository.findTokenByUserId(userData.getId());
        if (token == null) {
            token = new Token();
            token.setUserId(userData.getId());
        }

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MINUTE, 30);
        token.setUntil(c.getTime());

        token.setToken(SimpleUtils.getMD5(userName + new Date().toString()).toUpperCase());
        this.tokenRepository.save(token);

        UserView userView = new UserView();
        BeanUtils.copyBean(userData, userView);

        UserLoginView userLoginView = new UserLoginView();
        userLoginView.setUserView(userView);
        userLoginView.setToken(token.getToken());
        return userLoginView;
    }

    @Override
    public UserLoginView register(UserRegisterView userRegisterView, HttpResponse response) {
        if (!this.phoneCheck(userRegisterView.getPhone())) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_USED_PHONE, null));
            return null;
        }

//        Captcha captchaByPhone = this.captchaRepository.findCaptchaByPhone(userRegisterView.getPhone());
//
//        if (!captchaByPhone.getCaptcha().equals(userRegisterView.getCaptcha())) {
//            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_WRONG_CAPTCHA, null));
//            return null;
//        }

        ImageCaptcha imageCaptchaByToken = this.imageCaptchaRepository.findImageCaptchaByToken(userRegisterView.getToken());
        if (!imageCaptchaByToken.getCaptcha().equals(userRegisterView.getCaptcha())) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_WRONG_CAPTCHA, null));
            return null;
        }

        UserData userData = new UserData();
        BeanUtils.copyBean(userRegisterView, userData);

        userData.setCreateTime(new Date());
        if (!this.userRepository.save(userData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("注册不成功，请寻找原因。"));
            return null;
        }

        return this.login(userRegisterView.getUserName(), userRegisterView.getPassword(), response);
    }

    @Override
    public UserView findPassword(String phone, HttpResponse response) {
        UserData userByPhone = this.userRepository.findUserByPhone(phone);
        if (userByPhone == null) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_NO_USER, null));
            return null;
        }

        if (this.captchaService.captcha(phone)) {
            UserView userView = new UserView();
            BeanUtils.copyBean(userByPhone, userView);
            return userView;
        }
        HttpUtils.response(response, JsonRes.getFailJsonRes("发送验证码失败！"));
        return null;
    }

    @Override
    public UserLoginView resetPassword(UserRegisterView userRegisterView, HttpResponse response) {
        Captcha captchaByPhone = this.captchaRepository.findCaptchaByPhone(userRegisterView.getPhone());

        if (!captchaByPhone.getCaptcha().equals(userRegisterView.getCaptcha())) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_WRONG_CAPTCHA, null));
            return null;
        }

        if (this.userRepository.resetPasswordByUserNameAndPhone(userRegisterView.getPassword(),
                userRegisterView.getUserName(), userRegisterView.getPhone())) {
            return this.login(userRegisterView.getUserName(), userRegisterView.getPassword(), response);
        }
        HttpUtils.response(response, JsonRes.getFailJsonRes("重置密码失败！"));
        return null;
    }

    @Override
    public boolean userNameCheck(String userName) {
        UserData userByUserName = this.userRepository.findUserByUserName(userName);
        return userByUserName == null;
    }

    @Override
    public boolean phoneCheck(String phone) {
        UserData userData = this.userRepository.findUserByPhone(phone);
        return userData == null;
    }
}
