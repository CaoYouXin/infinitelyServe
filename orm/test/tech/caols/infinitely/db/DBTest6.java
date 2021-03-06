package tech.caols.infinitely.db;

import java.util.List;
import java.util.StringJoiner;

public class DBTest6 {

    public static void main(String[] args) {
        TestRepository repository = new TestRepository();

        printArray(repository.findAll().toArray());

        List<Test> floatGreaterThanHalf = repository.getCustomQueryResult();

        printArray(floatGreaterThanHalf.toArray());

        List<Test> customQueryResultWith = repository.getCustomQueryResultWith(4);

        printArray(customQueryResultWith.toArray());
    }

    public static <T> void printArray(T[] array) {
        StringJoiner stringJoiner = new StringJoiner("\n", "[\n", "\n]");
        for (T t : array) {
            stringJoiner.add(t.toString());
        }
        System.out.println(stringJoiner.toString());
    }

}
