package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.CategoryData;
import tech.caols.infinitely.repositories.CategoryRepository;
import tech.caols.infinitely.rest.BaseServiceImpl;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.CategoryService;
import tech.caols.infinitely.viewmodels.CategoryView;

import java.util.Date;
import java.util.List;

public class CategoryServiceImpl extends BaseServiceImpl<CategoryData, CategoryView> implements CategoryService {

    private CategoryRepository categoryRepository = new CategoryRepository();

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

    @Override
    public CategoryView fetch(String name, HttpResponse response) {
        CategoryData categoryData = this.categoryRepository.findByName(name);
        if (null == categoryData) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("没有该名称的分类！"));
            return null;
        }

        CategoryView categoryView = new CategoryView();
        BeanUtils.copyBean(categoryData, categoryView);
        return categoryView;
    }

}
