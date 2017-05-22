package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.ImageCaptcha;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class ImageCaptchaRepository extends Repository<ImageCaptcha, Long> {

    public ImageCaptchaRepository() {
        super(ImageCaptcha.class);
    }

    public ImageCaptcha findImageCaptchaByToken(String token) {
        List<ImageCaptcha> imageCaptchaList = super.query(
                "Select a From ImageCaptcha a Where a.token = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, token);

        if (imageCaptchaList.size() > 0) {
            return imageCaptchaList.get(0);
        }
        return null;
    }

}
