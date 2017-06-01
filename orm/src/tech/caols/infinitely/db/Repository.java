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
import java.util.stream.Collectors;

public class Repository<T, ID> {

    private static final Logger logger = LogManager.getLogger(Repository.class);

    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));

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
            logger.info(sql);

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
            logger.info(sql);

            this.setPSbyFieldAtIndex(preparedStatement, 1, id, id.getClass().getTypeName());

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
            this.fill(one, resultSet, column);
        }
    }

    private void fill(T one, ResultSet resultSet, ColumnMapping column) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        String name = column.getField().getName();
        String setterName = getDateManipulationMethodName(name, "set");
        Method setterMethod = this.clazz.getDeclaredMethod(setterName, column.getField().getType());

        String columnName = column.getColumn().name();
        String typeName = column.getField().getGenericType().getTypeName();

        this.fill(typeName, one, setterMethod, resultSet, false, null, columnName);
    }

    private void fill(String typeName, T one, Method setterMethod, ResultSet resultSet, boolean isIntParam, Integer index, String columnName) throws SQLException, InvocationTargetException, IllegalAccessException {
        switch (typeName) {
            case "byte":
            case "java.lang.Byte":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getByte(index));
                } else {
                    setterMethod.invoke(one, resultSet.getByte(columnName));
                }
                break;
            case "short":
            case "java.lang.Short":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getShort(index));
                } else {
                    setterMethod.invoke(one, resultSet.getShort(columnName));
                }
                break;
            case "int":
            case "java.lang.Integer":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getInt(index));
                } else {
                    setterMethod.invoke(one, resultSet.getInt(columnName));
                }
                break;
            case "long":
            case "java.lang.Long":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getLong(index));
                } else {
                    setterMethod.invoke(one, resultSet.getLong(columnName));
                }
                break;
            case "float":
            case "java.lang.Float":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getFloat(index));
                } else {
                    setterMethod.invoke(one, resultSet.getFloat(columnName));
                }
                break;
            case "double":
            case "java.lang.Double":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getDouble(index));
                } else {
                    setterMethod.invoke(one, resultSet.getDouble(columnName));
                }
                break;
            case "java.math.BigDecimal":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getBigDecimal(index));
                } else {
                    setterMethod.invoke(one, resultSet.getBigDecimal(columnName));
                }
                break;
            case "java.util.Date":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getTimestamp(index, calendar));
                } else {
                    setterMethod.invoke(one, resultSet.getTimestamp(columnName, calendar));
                }
                break;
            case "java.lang.String":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getString(index));
                } else {
                    setterMethod.invoke(one, resultSet.getString(columnName));
                }
                break;
            case "java.io.Reader":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getCharacterStream(index));
                } else {
                    setterMethod.invoke(one, resultSet.getCharacterStream(columnName));
                }
                break;
            case "java.io.InputStream":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getBinaryStream(index));
                } else {
                    setterMethod.invoke(one, resultSet.getBinaryStream(columnName));
                }
                break;
            default:
                System.out.println(typeName);
                break;
        }
    }

    private void fill(T one, ResultSet resultSet, ColumnMapping column, int index) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        String name = column.getField().getName();
        String setterName = getDateManipulationMethodName(name, "set");
        Method setterMethod = this.clazz.getDeclaredMethod(setterName, column.getField().getType());

        String typeName = column.getField().getGenericType().getTypeName();

        this.fill(typeName, one, setterMethod, resultSet, true, index, null);
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
            logger.info(sql);

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
        List<ColumnMapping> columns = this.columns().stream().filter(columnMapping -> {
            String name = columnMapping.getField().getName();
            String getterName = getDateManipulationMethodName(name, "get");
            try {
                Method getterMethod = this.clazz.getDeclaredMethod(getterName);
                return getterMethod.invoke(one) != null;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.catching(e);
                return false;
            }
        }).collect(Collectors.toList());

        final StringJoiner columnsSj = new StringJoiner("`, `", "`", "`");
        final StringJoiner sj = new StringJoiner(", ", "", "");
        columns.forEach(col -> {
            if (col.isKey()) {
                return;
            }
            columnsSj.add(col.getColumn().name());
            sj.add("?");
        });
        String sql = String.format("Insert into `%s`(%s) Values (%s)", this.tableName(),
                columnsSj.toString(), sj.toString());

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            logger.info(sql);

            int index = 0;
            for (ColumnMapping column : columns) {
                if (column.isKey()) {
                    continue;
                }

                this.setPSbyFieldAtIndex(one, preparedStatement, ++index, column);
            }

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.fill(one, generatedKeys, this.keyColumn(), 1);
                    return true;
                } else {
                    return false;
                }
            }
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

    private void setPSbyFieldAtIndex(PreparedStatement preparedStatement, int index, Object param, String typeName) throws SQLException {
        switch (typeName) {
            case "byte":
            case "java.lang.Byte":
                preparedStatement.setByte(index, (Byte) param);
                break;
            case "short":
            case "java.lang.Short":
                preparedStatement.setShort(index, (Short) param);
                break;
            case "int":
            case "java.lang.Integer":
                preparedStatement.setInt(index, (Integer) param);
                break;
            case "long":
            case "java.lang.Long":
                preparedStatement.setLong(index, (Long) param);
                break;
            case "float":
            case "java.lang.Float":
                preparedStatement.setFloat(index, (Float) param);
                break;
            case "double":
            case "java.lang.Double":
                preparedStatement.setDouble(index, (Double) param);
                break;
            case "java.math.BigDecimal":
                preparedStatement.setBigDecimal(index, (BigDecimal) param);
                break;
            case "java.util.Date":
                preparedStatement.setTimestamp(index, new Timestamp(((Date) param).getTime()), calendar);
                break;
            case "java.lang.String":
                preparedStatement.setString(index, (String) param);
                break;
            case "java.io.Reader":
                preparedStatement.setCharacterStream(index, (Reader) param);
                break;
            case "java.io.InputStream":
                preparedStatement.setBinaryStream(index, (InputStream) param);
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
            logger.info(selectSql.getSql());

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
            logger.info(sql);

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
                int ret = statement.executeUpdate(sql.getSql());
                logger.info(sql.getSql());
                return ret > 0;
            } else {
                PreparedStatement preparedStatement = conn.prepareStatement(sql.getSql());
                logger.info(sql.getSql());
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

    public boolean removeAll(List<Long> ids) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        ids.stream().map(id -> id + "").forEach(stringJoiner::add);
        String sql = String.format("Delete From `%s` Where `%s` in %s", this.tableName(),
                this.keyColumn().getColumn().name(), stringJoiner.toString());

        return this.execute(sql);
    }

    private boolean execute(String sql) {
        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            Statement statement = conn.createStatement();
            int update = statement.executeUpdate(sql);
            logger.info(sql);

            return update > 0;
        } catch (Exception e) {
            logger.catching(e);
        }

        return false;
    }

    public boolean softRemoveAll(List<ID> ids) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        ids.stream().map(id -> id + "").forEach(stringJoiner::add);
        String sql = String.format("Update `%s` Set `disabled` = 1 Where `%s` in %s", this.tableName(),
                this.keyColumn().getColumn().name(), stringJoiner.toString());

        return this.execute(sql);
    }
}
