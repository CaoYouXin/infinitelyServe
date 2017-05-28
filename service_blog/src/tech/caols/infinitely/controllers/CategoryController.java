package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.CategoryService;
import tech.caols.infinitely.services.impl.CategoryServiceImpl;
import tech.caols.infinitely.viewmodels.CategoryView;
import tech.caols.infinitely.viewmodels.Deletion;

import java.util.List;
import java.util.Map;

@Rest
public class CategoryController {

    private CategoryService categoryService = new CategoryServiceImpl();

    @RestAPI(name = "list_all_category", url = "/category/list", target = RestTarget.GET)
    public List<CategoryView> list() {
        return this.categoryService.list();
    }

    @RestAPI(name = "save_category", url = "/category/save", target = RestTarget.POST)
    public List<CategoryView> save(CategoryView categoryView, HttpResponse response) {
        return this.categoryService.save(categoryView, response);
    }

    @RestAPI(name = "delete_category", url = "/category/delete", target = RestTarget.POST)
    public List<CategoryView> save(Deletion deletion, HttpResponse response) {
        return this.categoryService.delete(deletion.getIds(), response);
    }

    @RestAPI(name = "fetch_category_by_name", url = "/category/fetch_by_name", target = RestTarget.GET)
    public CategoryView fetch(Map<String, String> parameters, HttpResponse response) {
        return this.categoryService.fetch(parameters.get("name"), response);
    }

}
