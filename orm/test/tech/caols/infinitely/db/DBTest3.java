package tech.caols.infinitely.db;

import java.util.Arrays;
import java.util.List;

public class DBTest3 {

    public static void main(String[] args) {
        Repository<Test, Integer> repository = new Repository<>(Test.class);

        Test test = repository.find(3);

        System.out.println(test);

        List<Test> all = repository.findAll();

        System.out.println(Arrays.toString(all.toArray()));
    }

}
