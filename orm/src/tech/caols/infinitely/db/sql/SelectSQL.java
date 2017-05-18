package tech.caols.infinitely.db.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.db.mapping.ColumnMapping;

import java.util.*;

public class SelectSQL extends BaseSQL implements SQL {

    private static final Logger logger = LogManager.getLogger(SelectSQL.class);

    private String select;
    private String from;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;

    private Class<?> resultClass;
    private List<String> resultList;

    public SelectSQL(String[] packageNames) {
        super(packageNames);
        this.resultList = new ArrayList<>();
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

    @Override
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

    public List<String> resultColumns() {
        return this.resultList;
    }

    @Override
    public String toString() {
        return "SelectSQL{" +
                "\n sql=" + this.getSql() +
                ",\n resultClass=" + resultClass +
                ",\n resultList=" + Arrays.toString(resultList.toArray()) +
                ",\n " + super.toString() +
                "\n}";
    }
}
