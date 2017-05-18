package tech.caols.infinitely.db.sql;

public class DeleteSQL extends BaseSQL implements SQL {

    private String delete;
    private String where;

    public DeleteSQL(String[] packageNames) {
        super(packageNames);
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public String getSql() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.where != null) {
            stringBuilder.append("Where ").append(this.where);
        }

        return String.format("Delete From %s %s", this.delete, stringBuilder.toString());
    }

    @Override
    public String toString() {
        return "DeleteSQL{" +
                "\n sql=" + this.getSql() +
                ",\n " + super.toString() +
                "\n}";
    }
}
