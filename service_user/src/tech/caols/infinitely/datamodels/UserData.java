package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user")
public class UserData {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "username", nullable = false, length = 16)
    private String userName;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "email")
    private String email;

    @Column(name = "sex")
    private byte sex;

    @Column(name = "age")
    private int age;

    @Column(name = "profession")
    private String profession;

    @Column(name = "avatar")
    private String avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
