package tech.caols.infinitely.db;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

public class DBTest7 {

    private static final DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        Repository<Test, Integer> repository = new Repository<>(Test.class);

        Test testToRemove = repository.find(6);
        if (testToRemove == null) {
            return;
        }

        System.out.println(repository.remove(testToRemove));
    }

}
