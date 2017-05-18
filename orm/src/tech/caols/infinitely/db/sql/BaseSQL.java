package tech.caols.infinitely.db.sql;

import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.db.mapping.ColumnMapping;

import java.util.*;

public class BaseSQL {

    private String[] packageNames;
    private Map<String, Class<?>> aliasMapping;
    private Map<String, Class<?>> mapping;
    private Map<Class<?>, List<ColumnMapping>> columnsMapping;

    public BaseSQL() {
        this.mapping = new HashMap<>();
        this.aliasMapping = new HashMap<>();
        this.columnsMapping = new HashMap<>();
    }

    public BaseSQL(String[] packageNames) {
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

    public String allAlias() {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String key : this.aliasMapping.keySet()) {
            stringJoiner.add(key);
        }
        return stringJoiner.toString();
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

    protected String toString(Map map) {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        for (Object key : map.keySet()) {
            stringJoiner.add(key + ": " + map.get(key));
        }
        return stringJoiner.toString();
    }

    @Override
    public String toString() {
        return "BaseSQL{" +
                "\n   packageNames=" + Arrays.toString(packageNames) +
                ",\n   aliasMapping=" + this.toString(aliasMapping) +
                ",\n   mapping=" + this.toString(mapping) +
//                ",\n   columnsMapping=" + this.toString(columnsMapping) +
                "\n }";
    }
}
