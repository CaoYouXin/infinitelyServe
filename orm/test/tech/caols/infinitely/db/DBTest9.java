package tech.caols.infinitely.db;

import java.util.StringJoiner;

public class DBTest9 {

    public static void main(String[] args) {
        TestRepository repository = new TestRepository();

        printArray(repository.findAll().toArray());

        System.out.println(repository.deleteBatch());
    }

    public static <T> void printArray(T[] array) {
        StringJoiner stringJoiner = new StringJoiner("\n", "[\n", "\n]");
        for (T t : array) {
            stringJoiner.add(t.toString());
        }
        System.out.println(stringJoiner.toString());
    }

}
