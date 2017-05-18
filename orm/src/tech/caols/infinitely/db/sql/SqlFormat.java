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
    private Pattern asPattern = Pattern.compile("\\s+(as\\s+)?");

    public String matcherGroup(Matcher matcher, String groupName) {
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

    public SQL parse(String sql, String[] packageNames) {
        SQL sqlObject = new SQL(packageNames);

        Matcher m = selectPattern.matcher(sql);

        String select = this.matcherGroup(m, "select");
        String from = this.matcherGroup(m, "from");
        String where = this.matcherGroup(m, "where");
        String groupBy = this.matcherGroup(m, "groupBy");
        String having = this.matcherGroup(m, "having");
        String orderBy = this.matcherGroup(m, "orderBy");

        this.parseFrom(from, sqlObject);
        this.parseSelect(select, sqlObject);

        if (where != null) {
            this.parseWhere(where + " ", sqlObject);
        }

        if (groupBy != null) {
            this.parseGroupBy(groupBy + " ", sqlObject);
        }

        if (having != null) {
            this.parseHaving(having + " ", sqlObject);
        }

        if (orderBy != null) {
            this.parseOrderBy(orderBy + " ", sqlObject);
        }

        return sqlObject;
    }

    private void parseOrderBy(String orderBy, SQL sql) {
        sql.setOrderBy(this.parseAlias(orderBy, sql));
    }

    private void parseHaving(String having, SQL sql) {
        sql.setHaving(this.parseAlias(having, sql));
    }

    private void parseGroupBy(String groupBy, SQL sql) {
        sql.setGroupBy(this.parseAlias(groupBy, sql));
    }

    private void parseWhere(String where, SQL sql) {
        sql.setWhere(this.parseAlias(where, sql));
    }

    private String parseAlias(String clause, SQL sql) {
        String regex = String.format("(?<alias>%s)\\.(?<field>\\S+?)(?<after>>|<|\\s|\\)|=)", sql.allAlias());
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(clause);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String alias = m.group("alias");
            String field = m.group("field");
            String after = m.group("after");

            Class<?> clazz = sql.getAlias(alias);
            String name = sql.column(clazz, field).getColumn().name();
            m.appendReplacement(sb, String.format("%s.`%s`%s", alias, name, after));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String parseSelectItem(String item, SQL sql) {
        Class<?> clazz = sql.getAlias(item);
        sql.setResultClass(clazz);
        StringJoiner sj = new StringJoiner(", ");
        for (ColumnMapping columnMapping : sql.columns(clazz)) {
            String name = columnMapping.getColumn().name();
            sql.addResult(name);
            sj.add(String.format("%s.`%s` as `%s`", item, name, name));
        }
        return sj.toString();
    }

    private String parseSelectItemBeforeAs(String item, SQL sql, boolean isResult) {
        int indexOfBracket = item.indexOf('(');
        if (indexOfBracket != -1) {
            return this.parseAlias(item, sql);
        }

        int indexOfDot = item.indexOf('.');
        if (indexOfDot == -1) {
            throw new RuntimeException("jpql error (before as) : " + item);
        }

        String beforeDot = item.substring(0, indexOfDot);
        String afterDot = item.substring(indexOfDot + 1);
        Class<?> clazz = sql.getAlias(beforeDot);
        String name = sql.column(clazz, afterDot).getColumn().name();
        if (isResult) {
            sql.setResultClass(clazz);
            sql.addResult(name);
            return String.format("%s.`%s` as `%s`", beforeDot, name, name);
        }
        return String.format("%s.`%s`", beforeDot, name);
    }

    private String parseSelectItemAfterAs(String item, SQL sql) {
        int indexOfDot = item.indexOf('.');
        if (indexOfDot == -1) {
            throw new RuntimeException("jpql error (after as): " + item);
        }

        String beforeDot = item.substring(0, indexOfDot);
        String afterDot = item.substring(indexOfDot + 1);
        sql.addMapping(beforeDot);
        Class<?> clazz = sql.getMapping(beforeDot);
        String name = sql.column(clazz, afterDot).getColumn().name();
        sql.setResultClass(clazz);
        sql.addResult(name);
        return name;
    }

    private void parseSelect(String select, SQL sql) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String string : select.split(",")) {
            String trim = string.trim();
            if (trim.indexOf(' ') != -1) {
                Matcher matcher = asPattern.matcher(trim);
                if (matcher.find()) {
                    String splitter = trim.substring(matcher.start(), matcher.end());
                    System.out.println(splitter);
                    stringJoiner.add(String.format("%s as `%s`",
                            this.parseSelectItemBeforeAs(trim.substring(0, matcher.start()), sql, false),
                            this.parseSelectItemAfterAs(trim.substring(matcher.end()), sql)));
                }
            } else {
                if (trim.indexOf('.') == -1) {
                    stringJoiner.add(this.parseSelectItem(trim, sql));
                } else {
                    stringJoiner.add(this.parseSelectItemBeforeAs(trim, sql, true));
                }
            }
        }
        sql.setSelect(stringJoiner.toString());
    }

    private void parseFrom(String from, SQL sql) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String string : from.split(",")) {
            String[] split = string.trim().split("\\s+");
            String className = split[0];
            String alias = split[1];
            sql.addMapping(className);
            Class<?> clazz = sql.getMapping(className);
            sql.setAlias(alias, clazz);
            stringJoiner.add(String.format("`%s` %s", DBHelper.tableName(clazz), alias));
        }
        sql.setFrom(stringJoiner.toString());
    }

    public String format(SQL sql) {
        return sql.getSql();
    }
}
