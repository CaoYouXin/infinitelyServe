package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.CategoryData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

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

}
