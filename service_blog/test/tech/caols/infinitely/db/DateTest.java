package tech.caols.infinitely.db;

import tech.caols.infinitely.repositories.CategoryRepository;

public class DateTest {

    public static void main(String[] args) {
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.findAllNotDisabled().forEach(System.out::println);
    }

}
