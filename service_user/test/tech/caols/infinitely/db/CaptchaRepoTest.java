package tech.caols.infinitely.db;

import tech.caols.infinitely.repositories.CaptchaRepository;

public class CaptchaRepoTest {

    public static void main(String[] args) {
        CaptchaRepository captchaRepository = new CaptchaRepository();
        captchaRepository.findAll().forEach(System.out::println);

        System.out.println(captchaRepository.findCaptchaByPhone("18618128264"));

//        Captcha captchaObject = new Captcha();
//        captchaObject.setPhone("18618128264");
//        captchaObject.setCaptcha("13244");
//
//        Date dt = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(dt);
//        c.add(Calendar.MINUTE, 30);
//        captchaObject.setUntil(c.getTime());
//
//        System.out.println(captchaRepository.save(captchaObject));
    }

}
