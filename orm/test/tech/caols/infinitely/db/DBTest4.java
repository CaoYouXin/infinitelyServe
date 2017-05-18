package tech.caols.infinitely.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

public class DBTest4 {

    private static final DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        Repository<Test, Integer> repository = new Repository<>(Test.class);

        Test testToSave = new Test();
        testToSave.setTinyint((byte) new Double((1 << 7) * Math.random()).intValue());
        testToSave.setSmallint((short) new Double((1 << 15) * Math.random()).intValue());
        testToSave.setMediumint(new Double((1 << 23) * Math.random()).intValue());
        testToSave.setBigint(2222L);
        testToSave.setaFloat((float) Math.random());
        testToSave.setaDouble(Math.random());
        testToSave.setDecimal(new BigDecimal("3.141592653"));
        testToSave.setDatetime(dateTimeInstance.parse("2017-11-6 7:00:00"));
        testToSave.setString("3.141592653");
        testToSave.setText(new StringReader("hello text from jpa"));
        testToSave.setBlob(new FileInputStream(
                new File("/Users/cls/Dev/Git/personal/infinitely/serve/out/test/orm", "hello_blob_from_file.txt")));
        System.out.println(repository.save(testToSave));
    }

}
