package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.CategoryData;
import tech.caols.infinitely.rest.BaseServiceImpl;
import tech.caols.infinitely.services.CategoryService;
import tech.caols.infinitely.viewmodels.CategoryView;

import java.util.Date;
import java.util.List;

public class CategoryServiceImpl extends BaseServiceImpl<CategoryData, CategoryView> implements CategoryService {

    public CategoryServiceImpl() {
        super(CategoryData.class, CategoryView.class);
    }

    @Override
    public List<CategoryView> save(CategoryView categoryView, HttpResponse response) {
        if (categoryView.getCreate() == null) {
            categoryView.setCreate(new Date());
        }
        categoryView.setUpdate(new Date());

        return super.save(categoryView, response);
    }

}
