package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.datamodels.Captcha;
import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.repositories.CaptchaRepository;
import tech.caols.infinitely.repositories.TokenRepository;
import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.viewmodels.UserLoginView;
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

        token.setToken(SimpleUtils.getMD5(userData.getUserName() + new Date().toString()).toUpperCase());
        this.tokenRepository.save(token);

        UserView userView = new UserView();
        BeanUtils.copyBean(userData, userView);

        UserLoginView userLoginView = new UserLoginView();
        userLoginView.setUserView(userView);
        userLoginView.setToken(token.getToken());
        return userLoginView;
    }

    @Override
    public String captcha(String phone) {
        String captcha = SimpleUtils.getMD5(phone + new Date().toString()).toUpperCase().substring(0, 5);

        Captcha captchaObject = new Captcha();
        captchaObject.setPhone(phone);
        captchaObject.setCaptcha(captcha);

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MINUTE, 30);
        captchaObject.setUntil(c.getTime());

        this.captchaRepository.save(captchaObject);
        logger.info(phone + "'s captcha is " + captcha);
        return captcha;
    }

    @Override
    public UserLoginView register(UserView userView) {
        return null;
    }

    @Override
    public UserLoginView findPassword(UserView userView) {
        return null;
    }

    @Override
    public UserLoginView resetPassword(UserView userView) {
        return null;
    }
}
