package tech.caols.infinitely.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatasourceFactory {

    private static final Logger logger = LogManager.getLogger(DatasourceFactory.class);

    private static final DataSource DATA_SOURCE;

    static {
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL("jdbc:mysql://localhost:3306/infinitely_serve");
        mysqlDS.setUser("root");
        mysqlDS.setPassword("root");
        try {
            mysqlDS.setLoginTimeout(5);
            mysqlDS.setResultSetSizeThreshold(1000);
        } catch (SQLException e) {
            logger.catching(e);
        }
        DATA_SOURCE = mysqlDS;
    }

    public static DataSource getMySQLDataSource() {
        return DATA_SOURCE;
    }

}
