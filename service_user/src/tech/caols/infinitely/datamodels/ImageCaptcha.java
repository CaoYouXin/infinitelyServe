package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_captcha")
public class ImageCaptcha {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "captcha", nullable = false, length = 4)
    private String captcha;

    @Column(name = "token", nullable = false, length = 32)
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ImageCaptcha{" +
                "id=" + id +
                ", captcha='" + captcha + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

}
