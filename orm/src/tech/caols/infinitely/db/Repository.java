package tech.caols.infinitely.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.db.mapping.ColumnMapping;
import tech.caols.infinitely.db.sql.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Repository<T, ID> {

    private static final Logger logger = LogManager.getLogger(Repository.class);

    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+8"));

    private final Class<T> clazz;
    private List<ColumnMapping> mappings;

    public Repository(Class<T> clazz) {
        this.clazz = clazz;
    }

    private List<ColumnMapping> columns() {
        if (null != this.mappings) {
            return this.mappings;
        }

        return (this.mappings = DBHelper.columns(this.clazz));
    }

    private String tableName() {
        return DBHelper.tableName(this.clazz);
    }

    private ColumnMapping keyColumn() {
        for (ColumnMapping columnMapping : this.columns()) {
            if (columnMapping.isKey()) {
                return columnMapping;
            }
        }
        return null;
    }

    private T buildOne() {
        return DBHelper.buildOne(this.clazz);
    }

    private String columnsString(final boolean excludeIDKey) {
        final StringJoiner sj = new StringJoiner("`, `", "`", "`");
        List<ColumnMapping> columns = this.columns();
        columns.forEach(col -> {
            if (col.isKey() && excludeIDKey) {
                return;
            }
            sj.add(col.getColumn().name());
        });
        return sj.toString();
    }

    public List<T> findAll() {
        String sql = String.format("Select %s From `%s`", this.columnsString(false), this.tableName());

        List<T> ret = new ArrayList<>();
        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                T one = this.buildOne();
                this.fill(one, resultSet);
                ret.add(one);
            }
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.catching(e);
        }

        return ret;
    }

    public T find(ID id) {
        String sql = String.format("Select %s From `%s` Where `%s`=?",
                this.columnsString(false), this.tableName(),
                this.keyColumn().getColumn().name());

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, (Integer) id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            T one = this.buildOne();
            this.fill(one, resultSet);
            return one;
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.catching(e);
            return null;
        }

    }

    private void fill(T one, ResultSet resultSet) throws IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        List<ColumnMapping> columns = this.columns();
        for (ColumnMapping column : columns) {
            fill(one, resultSet, column);
        }
    }

    private void fill(T one, ResultSet resultSet, ColumnMapping column) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        String name = column.getField().getName();
        String setterName = getDateManipulationMethodName(name, "set");
        Method setterMethod = this.clazz.getDeclaredMethod(setterName, column.getField().getType());

        String columnName = column.getColumn().name();
        String typeName = column.getField().getGenericType().getTypeName();

        switch (typeName) {
            case "byte":
            case "java.lang.Byte":
                setterMethod.invoke(one, resultSet.getByte(columnName));
                break;
            case "short":
            case "java.lang.Short":
                setterMethod.invoke(one, resultSet.getShort(columnName));
                break;
            case "int":
            case "java.lang.Integer":
                setterMethod.invoke(one, resultSet.getInt(columnName));
                break;
            case "long":
            case "java.lang.Long":
                setterMethod.invoke(one, resultSet.getLong(columnName));
                break;
            case "float":
            case "java.lang.Float":
                setterMethod.invoke(one, resultSet.getFloat(columnName));
                break;
            case "double":
            case "java.lang.Double":
                setterMethod.invoke(one, resultSet.getDouble(columnName));
                break;
            case "java.math.BigDecimal":
                setterMethod.invoke(one, resultSet.getBigDecimal(columnName));
                break;
            case "java.util.Date":
                setterMethod.invoke(one, resultSet.getTimestamp(columnName, calendar));
                break;
            case "java.lang.String":
                setterMethod.invoke(one, resultSet.getString(columnName));
                break;
            case "java.io.Reader":
                setterMethod.invoke(one, resultSet.getCharacterStream(columnName));
                break;
            case "java.io.InputStream":
                setterMethod.invoke(one, resultSet.getBinaryStream(columnName));
                break;
            default:
                System.out.println(typeName);
                break;
        }
    }

    private String getDateManipulationMethodName(String name, String manipulation) {
        char ch = name.charAt(0);
        String setterName = ch + "";
        if (Character.isLowerCase(ch) && !Character.isUpperCase(name.charAt(1))) {
            setterName = Character.toUpperCase(ch) + "";
        }
        setterName = manipulation + setterName + name.substring(1);
        return setterName;
    }

    public boolean save(T one) {
        Field keyField = this.keyColumn().getField();
        boolean accessible = keyField.isAccessible();
        keyField.setAccessible(true);
        Object key = null;
        try {
            key = keyField.get(one);
        } catch (IllegalAccessException e) {
            logger.catching(e);
        }
        keyField.setAccessible(accessible);

        if (null == key) {
            return this.insert(one);
        } else {
            T existOne = this.find((ID) key);
            if (null == existOne) {
                return this.insert(one);
            } else {
                return this.update(one);
            }
        }
    }

    private boolean update(T one) {
        final StringJoiner sj = new StringJoiner(", ", "", "");
        List<ColumnMapping> columns = this.columns();
        columns.forEach(col -> {
            if (col.isKey()) {
                return;
            }
            sj.add('`' + col.getColumn().name() + "`=?");
        });
        String sql = String.format("Update `%s` Set %s Where `%s`=?", this.tableName(),
                sj.toString(), this.keyColumn().getColumn().name());

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            int index = 0;
            for (ColumnMapping column : columns) {
                if (column.isKey()) {
                    continue;
                }

                this.setPSbyFieldAtIndex(one, preparedStatement, ++index, column);
            }

            this.setPSbyFieldAtIndex(one, preparedStatement, ++index, this.keyColumn());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            logger.catching(e);
        }

        return false;
    }


    private boolean insert(T one) {
        final StringJoiner sj = new StringJoiner(", ", "", "");
        List<ColumnMapping> columns = this.columns();
        columns.forEach(col -> {
            if (col.isKey()) {
                return;
            }
            sj.add("?");
        });
        String sql = String.format("Insert into `%s`(%s) Values (%s)", this.tableName(),
                this.columnsString(true), sj.toString());

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            int index = 0;
            for (ColumnMapping column : columns) {
                if (column.isKey()) {
                    continue;
                }

                this.setPSbyFieldAtIndex(one, preparedStatement, ++index, column);
            }

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            logger.catching(e);
        }

        return false;
    }

    private void setPSbyFieldAtIndex(T one, PreparedStatement preparedStatement, int index, ColumnMapping column) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String name = column.getField().getName();
        String getterName = getDateManipulationMethodName(name, "get");
        Method getterMethod = this.clazz.getDeclaredMethod(getterName);

        Object invokeRet = getterMethod.invoke(one);
        String typeName = column.getField().getGenericType().getTypeName();

        this.setPSbyFieldAtIndex(preparedStatement, index, invokeRet, typeName);
    }

    private void setPSbyFieldAtIndex(PreparedStatement preparedStatement, int index, Object invokeRet, String typeName) throws SQLException {
        switch (typeName) {
            case "byte":
            case "java.lang.Byte":
                preparedStatement.setByte(index, (Byte) invokeRet);
                break;
            case "short":
            case "java.lang.Short":
                preparedStatement.setShort(index, (Short) invokeRet);
                break;
            case "int":
            case "java.lang.Integer":
                preparedStatement.setInt(index, (Integer) invokeRet);
                break;
            case "long":
            case "java.lang.Long":
                preparedStatement.setLong(index, (Long) invokeRet);
                break;
            case "float":
            case "java.lang.Float":
                preparedStatement.setFloat(index, (Float) invokeRet);
                break;
            case "double":
            case "java.lang.Double":
                preparedStatement.setDouble(index, (Double) invokeRet);
                break;
            case "java.math.BigDecimal":
                preparedStatement.setBigDecimal(index, (BigDecimal) invokeRet);
                break;
            case "java.util.Date":
                preparedStatement.setTimestamp(index, new Timestamp(((Date) invokeRet).getTime()), calendar);
                break;
            case "java.lang.String":
                preparedStatement.setString(index, (String) invokeRet);
                break;
            case "java.io.Reader":
                preparedStatement.setCharacterStream(index, (Reader) invokeRet);
                break;
            case "java.io.InputStream":
                preparedStatement.setBinaryStream(index, (InputStream) invokeRet);
                break;
            default:
                System.out.println(typeName);
                break;
        }
    }

    public List<T> query(String s, String[] packageNames, Object... params) {
        SelectSQL selectSql = SqlFormat.getInstance().parseSelect(s, packageNames);

        if (selectSql == null) {
            throw new RuntimeException("impossible");
        }

        if (!this.clazz.equals(selectSql.getResultClass())) {
            throw new RuntimeException("this query should not be contained in this repository");
        }

        List<T> ret = new ArrayList<>();
        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            ResultSet resultSet;

            if (params.length == 0) {
                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery(selectSql.getSql());
            } else {
                PreparedStatement preparedStatement = conn.prepareStatement(selectSql.getSql());
                int index = 0;
                for (Object param : params) {
                    this.setPSbyFieldAtIndex(preparedStatement, ++index, param, param.getClass().getTypeName());
                }
                resultSet = preparedStatement.executeQuery();
            }

            while (resultSet.next()) {
                T one = this.buildOne();
                this.fill(one, resultSet, selectSql.resultColumns());
                ret.add(one);
            }
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.catching(e);
        }

        return ret;
    }

    private void fill(T one, ResultSet resultSet, List<String> resultColumns) throws IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        List<ColumnMapping> columns = this.columns();
        for (ColumnMapping column : columns) {
            if (resultColumns.contains(column.getColumn().name())) {
                fill(one, resultSet, column);
            }
        }
    }

    public boolean remove(T one) {
        String sql = String.format("Delete From `%s` Where `%s`=?", this.tableName(),
                this.keyColumn().getColumn().name());

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            this.setPSbyFieldAtIndex(one, preparedStatement, 1, this.keyColumn());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            logger.catching(e);
        }

        return false;
    }

    public boolean update(String s, String[] packageNames, Object... params) {
        UpdateSQL updateSQL = SqlFormat.getInstance().parseUpdate(s, packageNames);

        return this.executeSQL(updateSQL, params);
    }

    public boolean remove(String s, String[] packageNames, Object... params) {
        DeleteSQL deleteSQL = SqlFormat.getInstance().parseDelete(s, packageNames);

        return this.executeSQL(deleteSQL, params);
    }

    private boolean executeSQL(SQL sql, Object[] params) {
        if (sql == null) {
            throw new RuntimeException("impossible");
        }

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {

            if (params.length == 0) {
                Statement statement = conn.createStatement();
                return statement.executeUpdate(sql.getSql()) > 0;
            } else {
                PreparedStatement preparedStatement = conn.prepareStatement(sql.getSql());
                int index = 0;
                for (Object param : params) {
                    this.setPSbyFieldAtIndex(preparedStatement, ++index, param, param.getClass().getTypeName());
                }
                return preparedStatement.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            logger.catching(e);
        }

        return false;
    }
}
