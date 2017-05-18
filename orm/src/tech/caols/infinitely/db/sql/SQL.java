package tech.caols.infinitely.db.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.db.mapping.ColumnMapping;

import java.util.*;

public class SQL {

    private static final Logger logger = LogManager.getLogger(SQL.class);

    private String select;
    private String from;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;
    private String[] packageNames;
    private Class<?> resultClass;
    private List<String> resultList;
    private Map<String, Class<?>> aliasMapping;
    private Map<String, Class<?>> mapping;
    private Map<Class<?>, List<ColumnMapping>> columnsMapping;

    public SQL() {
        this.mapping = new HashMap<>();
        this.aliasMapping = new HashMap<>();
        this.columnsMapping = new HashMap<>();
        this.resultList = new ArrayList<>();
    }

    public SQL(String[] packageNames) {
        this();
        this.packageNames = packageNames;
    }

    public void addMapping(String className) {
        Class<?> mapping = this.getMapping(className);
        if (mapping != null) {
            return;
        }

        this.addMapping(className, this.getClazz(0, className));
    }

    private void addMapping(String string, Class<?> clazz) {
        this.mapping.put(string, clazz);
    }

    private Class<?> getClazz(int index, String className) {
        if (index >= this.packageNames.length) {
            throw new RuntimeException("no class found");
        }

        try {
            return Class.forName(this.packageNames[index] + className);
        } catch (ClassNotFoundException e) {
            return getClazz(++index, className);
        }
    }

    public Class<?> getMapping(String string) {
        return this.mapping.get(string);
    }

    public void setAlias(String alias, Class<?> clazz) {
        this.aliasMapping.put(alias, clazz);
    }

    public Class<?> getAlias(String alias) {
        return this.aliasMapping.get(alias);
    }

    public List<ColumnMapping> columns(Class<?> clazz) {
        List<ColumnMapping> columnMappingList = this.columnsMapping.get(clazz);
        if (columnMappingList != null) {
            return columnMappingList;
        }

        columnMappingList = DBHelper.columns(clazz);
        this.columnsMapping.put(clazz, columnMappingList);
        return columnMappingList;
    }

    public ColumnMapping column(Class<?> clazz, String name) {
        for (ColumnMapping columnMapping : this.columns(clazz)) {
            if (columnMapping.getField().getName().equals(name)) {
                return columnMapping;
            }
        }
        return null;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSql() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (where != null) {
            stringBuilder.append(" Where ").append(where);
        }

        if (groupBy != null) {
            stringBuilder.append(" Group By ").append(groupBy);
            if (having != null) {
                stringBuilder.append(" Having ").append(having);
            }
        }

        if (orderBy != null) {
            stringBuilder.append(" Order By ").append(orderBy);
        }

        return String.format("Select %s From %s %s", select, from, stringBuilder.toString());
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class<?> resultClass) {
        if (null != this.resultClass) {
            if (this.resultClass != resultClass) {
                throw new RuntimeException("jpql error, multi result class");
            }
        }
        this.resultClass = resultClass;
    }

    public void addResult(String string) {
        this.resultList.add(string);
    }

    private String toString(Map map) {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        for (Object key : map.keySet()) {
            stringJoiner.add(key + ": " + map.get(key));
        }
        return stringJoiner.toString();
    }

    public List<String> resultColumns() {
        return this.resultList;
    }

    @Override
    public String toString() {
        return "SQL{" +
                "\n sql=" + this.getSql() +
                ",\n packageNames=" + Arrays.toString(packageNames) +
                ",\n resultClass=" + resultClass +
                ",\n resultList=" + Arrays.toString(resultList.toArray()) +
                ",\n aliasMapping=" + this.toString(aliasMapping) +
                ",\n mapping=" + this.toString(mapping) +
//                ",\n columnsMapping=" + this.toString(columnsMapping) +
                '}';
    }

    public String allAlias() {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String key : this.aliasMapping.keySet()) {
            stringJoiner.add(key);
        }
        return stringJoiner.toString();
    }
}
