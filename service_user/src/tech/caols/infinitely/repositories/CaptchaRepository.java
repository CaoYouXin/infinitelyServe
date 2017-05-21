package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.Captcha;
import tech.caols.infinitely.db.Repository;

import java.util.Date;
import java.util.List;

public class CaptchaRepository extends Repository<Captcha, Long> {

    public CaptchaRepository() {
        super(Captcha.class);
    }

    public Captcha findCaptchaByPhone(String phone) {
        List<Captcha> captchaList = super.query("Select a From Captcha a Where a.phone = ? and a.until >= ? Order By a.until desc Limit 1",
                new String[]{"tech.caols.infinitely.datamodels."}, phone, new Date());
        if (captchaList.size() > 0) {
            return captchaList.get(0);
        }
        return null;
    }

}
