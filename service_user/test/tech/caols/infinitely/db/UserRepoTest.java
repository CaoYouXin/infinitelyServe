package tech.caols.infinitely.db;

import tech.caols.infinitely.repositories.UserRepository;

public class UserRepoTest {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        userRepository.findAll().forEach(System.out::println);
    }

}
