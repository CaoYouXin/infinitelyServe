package tech.caols.infinitely.db.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.db.mapping.ColumnMapping;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlFormat {

    private static final Logger logger = LogManager.getLogger(SqlFormat.class);

    private static final SqlFormat INSTANCE = new SqlFormat();

    public static SqlFormat getInstance() {
        return INSTANCE;
    }

    private Pattern selectPattern = Pattern.compile("Select\\s+(?<select>.+?)\\s+From\\s+(?<from>.+?)(?:\\s+Where\\s+(?<where>.+?))*(?:\\s+Group By\\s+(?<groupBy>.+?)(?:\\s+Having\\s+(?<having>.+?))*)*(?:\\s+Order By\\s+(?<orderBy>.+?))*");
    private Pattern updatePattern = Pattern.compile("Update\\s+(?<update>.+?)\\s+Set\\s+(?<set>.+?)(?:\\s+Where\\s+(?<where>.+?))*");
    private Pattern deletePattern = Pattern.compile("Delete From\\s+(?<delete>.+?)(?:\\s+Where\\s+(?<where>.+?))*");
    private Pattern asPattern = Pattern.compile("\\s+(as\\s+)?");

    private String matcherGroup(Matcher matcher, String groupName) {
        if (matcher.matches()) {
            try {
                return matcher.group(groupName);
            } catch (Exception e) {
                logger.catching(e);
                return null;
            }
        }
        return null;
    }

    public SelectSQL parseSelect(String sql, String[] packageNames) {
        SelectSQL selectSqlObject = new SelectSQL(packageNames);

        Matcher m = selectPattern.matcher(sql);

        String select = this.matcherGroup(m, "select");
        String from = this.matcherGroup(m, "from");
        String where = this.matcherGroup(m, "where");
        String groupBy = this.matcherGroup(m, "groupBy");
        String having = this.matcherGroup(m, "having");
        String orderBy = this.matcherGroup(m, "orderBy");

        this.parseFrom(from, selectSqlObject);
        this.parseSelect(select, selectSqlObject);

        if (where != null) {
            this.parseWhere(where + " ", selectSqlObject);
        }

        if (groupBy != null) {
            this.parseGroupBy(groupBy + " ", selectSqlObject);
        }

        if (having != null) {
            this.parseHaving(having + " ", selectSqlObject);
        }

        if (orderBy != null) {
            this.parseOrderBy(orderBy + " ", selectSqlObject);
        }

        return selectSqlObject;
    }

    private void parseOrderBy(String orderBy, SelectSQL selectSql) {
        selectSql.setOrderBy(this.parseAlias(orderBy, selectSql, false));
    }

    private void parseHaving(String having, SelectSQL selectSql) {
        selectSql.setHaving(this.parseAlias(having, selectSql, false));
    }

    private void parseGroupBy(String groupBy, SelectSQL selectSql) {
        selectSql.setGroupBy(this.parseAlias(groupBy, selectSql, false));
    }

    private void parseWhere(String where, SelectSQL selectSql) {
        selectSql.setWhere(this.parseAlias(where, selectSql, false));
    }

    private String parseAlias(String clause, BaseSQL baseSQL, boolean isDelete) {
        String regex = String.format("(?<alias>%s)\\.(?<field>\\S+?)(?<after>>|<|\\s|\\)|=)", baseSQL.allAlias());
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(clause);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String alias = m.group("alias");
            String field = m.group("field");
            String after = m.group("after");

            Class<?> clazz = baseSQL.getAlias(alias);
            String name = baseSQL.column(clazz, field).getColumn().name();
            if (isDelete) {
                m.appendReplacement(sb, String.format("`%s`%s", name, after));
                continue;
            }

            m.appendReplacement(sb, String.format("%s.`%s`%s", alias, name, after));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String parseSelectItem(String item, SelectSQL selectSql) {
        Class<?> clazz = selectSql.getAlias(item);
        selectSql.setResultClass(clazz);
        StringJoiner sj = new StringJoiner(", ");
        for (ColumnMapping columnMapping : selectSql.columns(clazz)) {
            String name = columnMapping.getColumn().name();
            selectSql.addResult(name);
            sj.add(String.format("%s.`%s` as `%s`", item, name, name));
        }
        return sj.toString();
    }

    private String parseSelectItemBeforeAs(String item, SelectSQL selectSql, boolean isResult) {
        int indexOfBracket = item.indexOf('(');
        if (indexOfBracket != -1) {
            return this.parseAlias(item, selectSql, false);
        }

        int indexOfDot = item.indexOf('.');
        if (indexOfDot == -1) {
            throw new RuntimeException("jpql error (before as) : " + item);
        }

        String beforeDot = item.substring(0, indexOfDot);
        String afterDot = item.substring(indexOfDot + 1);
        Class<?> clazz = selectSql.getAlias(beforeDot);
        String name = selectSql.column(clazz, afterDot).getColumn().name();
        if (isResult) {
            selectSql.setResultClass(clazz);
            selectSql.addResult(name);
            return String.format("%s.`%s` as `%s`", beforeDot, name, name);
        }
        return String.format("%s.`%s`", beforeDot, name);
    }

    private String parseSelectItemAfterAs(String item, SelectSQL selectSql) {
        int indexOfDot = item.indexOf('.');
        if (indexOfDot == -1) {
            throw new RuntimeException("jpql error (after as): " + item);
        }

        String beforeDot = item.substring(0, indexOfDot);
        String afterDot = item.substring(indexOfDot + 1);
        selectSql.addMapping(beforeDot);
        Class<?> clazz = selectSql.getMapping(beforeDot);
        String name = selectSql.column(clazz, afterDot).getColumn().name();
        selectSql.setResultClass(clazz);
        selectSql.addResult(name);
        return name;
    }

    private void parseSelect(String select, SelectSQL selectSql) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String string : select.split(",")) {
            String trim = string.trim();
            if (trim.indexOf(' ') != -1) {
                Matcher matcher = asPattern.matcher(trim);
                if (matcher.find()) {
                    String splitter = trim.substring(matcher.start(), matcher.end());
                    System.out.println(splitter);
                    stringJoiner.add(String.format("%s as `%s`",
                            this.parseSelectItemBeforeAs(trim.substring(0, matcher.start()), selectSql, false),
                            this.parseSelectItemAfterAs(trim.substring(matcher.end()), selectSql)));
                }
            } else {
                if (trim.indexOf('.') == -1) {
                    stringJoiner.add(this.parseSelectItem(trim, selectSql));
                } else {
                    stringJoiner.add(this.parseSelectItemBeforeAs(trim, selectSql, true));
                }
            }
        }
        selectSql.setSelect(stringJoiner.toString());
    }

    private void parseFrom(String from, SelectSQL selectSql) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String string : from.split(",")) {
            String[] split = string.trim().split("\\s+");
            String className = split[0];
            String alias = split[1];
            selectSql.addMapping(className);
            Class<?> clazz = selectSql.getMapping(className);
            selectSql.setAlias(alias, clazz);
            stringJoiner.add(String.format("`%s` %s", DBHelper.tableName(clazz), alias));
        }
        selectSql.setFrom(stringJoiner.toString());
    }

    public UpdateSQL parseUpdate(String sql, String[] packageNames) {
        UpdateSQL updateSQLObject = new UpdateSQL(packageNames);

        Matcher m = updatePattern.matcher(sql);

        String update = this.matcherGroup(m, "update");
        String set = this.matcherGroup(m, "set");
        String where = this.matcherGroup(m, "where");

        this.parseUpdate(update, updateSQLObject);
        this.parseSet(set, updateSQLObject);

        if (where != null) {
            this.parseWhere(where, updateSQLObject);
        }

        return updateSQLObject;
    }

    private void parseWhere(String where, UpdateSQL updateSQL) {
        updateSQL.setWhere(this.parseAlias(where, updateSQL, false));
    }

    private void parseSet(String set, UpdateSQL updateSQL) {
        updateSQL.setSet(this.parseAlias(set, updateSQL, false));
    }

    private void parseUpdate(String clause, UpdateSQL updateSQL) {
        updateSQL.setUpdate(this.parseTableName(clause, updateSQL, false));
    }

    private String parseTableName(String clause, BaseSQL baseSQL, boolean isDelete) {
        String[] split = clause.trim().split("\\s+");
        String className = split[0];
        String alias = split[1];

        baseSQL.addMapping(className);
        Class<?> clazz = baseSQL.getMapping(className);

        baseSQL.setAlias(alias, clazz);

        if (isDelete) {
            return String.format("`%s`", DBHelper.tableName(clazz));
        }

        return String.format("`%s` %s", DBHelper.tableName(clazz), alias);
    }

    public DeleteSQL parseDelete(String sql, String[] packageNames) {
        DeleteSQL deleteSQLObject = new DeleteSQL(packageNames);

        Matcher m = deletePattern.matcher(sql);

        String delete = this.matcherGroup(m, "delete");
        String where = this.matcherGroup(m, "where");

        this.parseDelete(delete, deleteSQLObject);

        if (where != null) {
            this.parseWhere(where, deleteSQLObject);
        }

        return deleteSQLObject;
    }

    private void parseWhere(String where, DeleteSQL deleteSQL) {
        deleteSQL.setWhere(this.parseAlias(where, deleteSQL, true));
    }

    private void parseDelete(String clause, DeleteSQL deleteSQL) {
        deleteSQL.setDelete(this.parseTableName(clause, deleteSQL, true));
    }
}
