package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "captcha")
public class Captcha {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "captcha", nullable = false, length = 5)
    private String captcha;

    @Column(name = "until")
    private Date until;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", captcha='" + captcha + '\'' +
                ", until=" + until +
                '}';
    }

}
