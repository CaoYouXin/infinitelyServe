package tech.caols.infinitely.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

public class DBTest2 {

    private static final Logger logger = LogManager.getLogger(DBTest2.class);

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+8"));
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "Insert into `test`(`tinyint`, `smallint`, `mediumint`, `bigint`, `float`, `double`, `decimal`," +
                            " `datetime`, `string`, `text`, `blob`) "
                            + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setByte(1, (byte) new Double((1 << 7) * Math.random()).intValue());
            preparedStatement.setShort(2, (short) new Double((1 << 15) * Math.random()).intValue());
            preparedStatement.setInt(3, new Double((1 << 23) * Math.random()).intValue());
            preparedStatement.setLong(4, 1111L);
            preparedStatement.setFloat(5, (float) Math.random());
            preparedStatement.setDouble(6, Math.random());
            preparedStatement.setBigDecimal(7, new BigDecimal("3.141592653"));
            preparedStatement.setTimestamp(8, new Timestamp(new Date().getTime()), calendar);
            preparedStatement.setString(9, "3.141592653");
            preparedStatement.setCharacterStream(10, new StringReader("hello text from java"));
            preparedStatement.setBinaryStream(11, new FileInputStream(
                    new File("/Users/cls/Dev/Git/personal/infinitely/serve/out/test/orm", "hello_blob_from_file.txt")));

//            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE `test` SET `tinyint`=? WHERE `idtest`=?");
//
//            preparedStatement.setByte(1, (byte) 5);
//            preparedStatement.setInt(2, 1);

            System.out.println(preparedStatement.executeUpdate());
        } catch (Exception e) {
            logger.catching(e);
        }
    }

}
