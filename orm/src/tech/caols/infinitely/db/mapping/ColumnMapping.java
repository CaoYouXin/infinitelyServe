package tech.caols.infinitely.db.mapping;

import javax.persistence.Column;
import java.lang.reflect.Field;

public class ColumnMapping {

    private Column column;
    private Field  field;
    private boolean isKey;

    public ColumnMapping(Column column, Field field) {
        this.column = column;
        this.field = field;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }
}
