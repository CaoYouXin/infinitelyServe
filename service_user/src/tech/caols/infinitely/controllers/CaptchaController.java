package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.CaptchaService;
import tech.caols.infinitely.services.impl.CaptchaServiceImpl;

import java.util.Map;

@Rest
public class CaptchaController {

    private final CaptchaService captchaService = new CaptchaServiceImpl();

    @RestAPI(name = "captcha", url = "/captcha", target = RestTarget.GET)
    public boolean captcha(Map<String, String> parameters) {
        return this.captchaService.captcha(parameters.get("phone"));
    }

    @RestAPI(name = "image_captcha", url = "/captcha/image", target = RestTarget.GET)
    public void imageCaptcha(Map<String, String> parameters, HttpResponse response) {
        this.captchaService.captcha(parameters.get("token"), response);
    }

}
