package tech.caols.infinitely.db.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.db.mapping.ColumnMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private static final Logger logger = LogManager.getLogger(DBHelper.class);

    public static String tableName(Class<?> clazz) {
        Entity entity = clazz.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("not a entity");
        }

        Table table = clazz.getDeclaredAnnotation(Table.class);
        if (null == table) {
            throw new RuntimeException("not a table");
        }

        return table.name();
    }

    public static <K> K buildOne(Class<K> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            return null;
        }
    }

    public static List<ColumnMapping> columns(Class<?> clazz) {
        Entity entity = clazz.getDeclaredAnnotation(Entity.class);
        if (null == entity) {
            throw new RuntimeException("not a entity");
        }

        List<ColumnMapping> ret = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = field.getDeclaredAnnotation(Column.class);
            ColumnMapping columnMapping = new ColumnMapping(column, field);

            Id id = field.getDeclaredAnnotation(Id.class);
            if (null != id) {
                columnMapping.setKey(true);
            }

            ret.add(columnMapping);
        }

        return ret;
    }

}
