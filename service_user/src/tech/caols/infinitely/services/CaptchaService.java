package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;

public interface CaptchaService {

    boolean captcha(String phone);

    void captcha(String token, HttpResponse response);

}
