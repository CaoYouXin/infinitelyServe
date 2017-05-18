package tech.caols.infinitely.db.sql;

public class UpdateSQL extends BaseSQL implements SQL {

    private String update;
    private String set;
    private String where;

    public UpdateSQL(String[] packageNames) {
        super(packageNames);
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setSet(String set) {
        this.set = set;
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

        return String.format("Update %s Set %s %s", this.update, this.set, stringBuilder.toString());
    }

    @Override
    public String toString() {
        return "UpdateSQL{" +
                "\n sql=" + this.getSql() +
                ",\n " + super.toString() +
                "\n}";
    }
}
