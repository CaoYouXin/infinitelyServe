package tech.caols.infinitely.model;

import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.viewmodels.UserView;

public class ModelTest {

    public static void main(String[] args) {
        UserData userData = new UserData();
        userData.setAge(100);

        UserView userView = new UserView();
        System.out.println(userView.getAge());

        BeanUtils.copyBean(userData, userView);
        System.out.println(userView.getAge());
    }

}
