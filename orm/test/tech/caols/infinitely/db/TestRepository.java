package tech.caols.infinitely.db;

import java.util.List;

public class TestRepository extends Repository<Test, Integer> {

    public TestRepository() {
        super(Test.class);
    }

    public List<Test> getFloatGreaterThanHalf() {
        return super.query("Select SUM(a.idtest) Test.idtest From Test a Group By a.smallint Having SUM(a.idtest) > 5",
                new String[]{"tech.caols.infinitely.db."});
    }

}
