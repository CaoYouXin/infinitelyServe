package tech.caols.infinitely.db;

import java.util.List;

public class TestRepository extends Repository<Test, Integer> {

    public TestRepository() {
        super(Test.class);
    }

    public List<Test> getCustomQueryResult() {
        return super.query("Select SUM(a.idtest) Test.idtest From Test a Group By a.smallint Having SUM(a.idtest) > 5",
                new String[]{"tech.caols.infinitely.db."});
    }

    public List<Test> getCustomQueryResultWith(int aInt) {
        return super.query("Select SUM(a.idtest) Test.idtest From Test a Group By a.smallint Having SUM(a.idtest) > ?",
                new String[]{"tech.caols.infinitely.db."}, aInt);
    }

    public boolean updateBatch(int set, int where) {
        return super.update("Update Test a Set a.smallint = ? Where a.smallint = ?",
                new String[]{"tech.caols.infinitely.db."}, set, where);
    }

    public boolean deleteBatch() {
        return super.remove("Delete From Test a Where a.smallint = 6",
                new String[]{"tech.caols.infinitely.db."});
    }

}
