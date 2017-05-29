package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.CategoryData;
import tech.caols.infinitely.base.BaseService;
import tech.caols.infinitely.viewmodels.CategoryView;

public interface CategoryService extends BaseService<CategoryData, CategoryView> {

    CategoryView fetch(String name, HttpResponse response);

}
