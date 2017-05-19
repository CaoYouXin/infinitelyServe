package tech.caols.infinitely.viewmodels;

public class UserLoginView {

    private UserView userView;
    private String token;

    public UserView getUserView() {
        return userView;
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserLoginView{" +
                "userView=" + userView +
                ", token='" + token + '\'' +
                '}';
    }
}
