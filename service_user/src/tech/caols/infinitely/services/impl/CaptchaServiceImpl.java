package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.datamodels.Captcha;
import tech.caols.infinitely.datamodels.ImageCaptcha;
import tech.caols.infinitely.repositories.CaptchaRepository;
import tech.caols.infinitely.repositories.ImageCaptchaRepository;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.CaptchaService;
import tech.caols.infinitely.third.JavaSmsApi;
import tech.caols.infinitely.util.RandomSecurityCode;
import tech.caols.infinitely.util.RandomSecurityImage;

import java.util.Calendar;
import java.util.Date;

public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger logger = LogManager.getLogger(CaptchaServiceImpl.class);

    private CaptchaRepository captchaRepository = new CaptchaRepository();
    private ImageCaptchaRepository imageCaptchaRepository = new ImageCaptchaRepository();

    @Override
    public boolean captcha(String phone) {
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

        logger.info(JavaSmsApi.sendCaptcha(phone, captcha));
        logger.info(phone + "'s captcha is " + captcha);
        return true;
    }

    @Override
    public void captcha(String token, HttpResponse response) {
        char[] securityCode = new RandomSecurityCode().getSecurityCode();

        ImageCaptcha imageCaptcha = new ImageCaptcha();
        imageCaptcha.setCaptcha(new String(securityCode));
        imageCaptcha.setToken(token);

        if (this.imageCaptchaRepository.save(imageCaptcha)) {
            response.setStatusCode(HttpStatus.SC_OK);
            response.setEntity(new ByteArrayEntity(new RandomSecurityImage()
                    .getImage(securityCode, 120, 30, 20),
                    ContentType.create("image/jpg")));
        } else {
            HttpUtils.response(response, JsonRes.getFailJsonRes("创建验证码失败！"));
        }
    }

}
