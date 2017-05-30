package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "token", nullable = false, length = 32)
    private String token;

    @Column(name = "until", nullable = false)
    private Date until;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", until=" + until +
                '}';
    }

}
