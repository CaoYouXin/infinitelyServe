package tech.caols.infinitely.service;

import tech.caols.infinitely.services.UserService;
import tech.caols.infinitely.services.impl.UserServiceImpl;

public class UserServiceTest1 {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.listAllUsers().forEach(System.out::println);
    }

}
