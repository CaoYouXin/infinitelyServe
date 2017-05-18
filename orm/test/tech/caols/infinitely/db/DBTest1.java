package tech.caols.infinitely.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.TimeZone;

public class DBTest1 {

    private static final Logger logger = LogManager.getLogger(DBTest1.class);

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+8"));
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("Select * from test where idtest = ?");
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StringJoiner sj = new StringJoiner(", ", "{", "}");
                sj.add(resultSet.getInt("idtest") + "");
                sj.add(resultSet.getInt("tinyint") + "");
                sj.add(resultSet.getInt("smallint") + "");
                sj.add(resultSet.getInt("mediumint") + "");
                sj.add(resultSet.getLong("bigint") + "");
                sj.add(resultSet.getFloat("float") + "");
                sj.add(resultSet.getDouble("double") + "");
                sj.add(resultSet.getBigDecimal("decimal").toPlainString());
                sj.add(dateTimeInstance.format(resultSet.getTimestamp("datetime", calendar)));
                sj.add(resultSet.getString("string"));

                String line;
//                Reader text = resultSet.getCharacterStream("text");
//                BufferedReader bufferedReader = new BufferedReader(text);
//                StringBuilder sb = new StringBuilder();
//                while ((line = bufferedReader.readLine()) != null) {
//                    sb.append(line);
//                }
//                sj.add(sb.toString());
                sj.add(resultSet.getString("text"));

//                InputStream blob = resultSet.getBinaryStream("blob");
//                BufferedReader br = new BufferedReader(new InputStreamReader(blob));
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//                sj.add(stringBuilder.toString());
                sj.add(resultSet.getString("blob"));

                System.out.println(sj.toString());
            }
        } catch (Exception e) {
            logger.catching(e);
        }
    }

}
