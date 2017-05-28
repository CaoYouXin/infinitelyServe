package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.CategoryData;
import tech.caols.infinitely.db.Repository;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class CategoryRepository extends Repository<CategoryData, Long> {

    public CategoryRepository() {
        super(CategoryData.class);
    }

    public CategoryData findByName(String name) {
        List<CategoryData> categoryDataList = super.query("Select a From CategoryData a Where a.name = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, name);

        if (categoryDataList.size() > 0) {
            return categoryDataList.get(0);
        }
        return null;
    }

    public List<CategoryData> search(Date start, Date end, List<String> keywords) {
        StringJoiner stringJoiner = new StringJoiner(" and ");
        keywords.forEach(keyword -> {
            stringJoiner.add(String.format("a.name like '%%%s%%'", keyword));
        });
        return super.query(String.format("Select a From CategoryData a Where a.update > ? and a.update < ? and %s", stringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, start, end);
    }

}
