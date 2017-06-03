package tech.caols.infinitely.viewmodels;

import java.util.Date;

public class UserView {

    private Long id;
    private String userName;
    private String phone;
    private Date createTime;
    private String email;
    private byte sex;
    private int age;
    private String profession;
    private String avatar;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "UserView{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", profession='" + profession + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
