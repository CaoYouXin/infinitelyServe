package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.CategoryService;
import tech.caols.infinitely.services.impl.CategoryServiceImpl;
import tech.caols.infinitely.viewmodels.CategoryView;

import java.util.List;

@Rest
public class CategoryController {

    private CategoryService categoryService = new CategoryServiceImpl();

    @RestAPI(name = "list_all_category", url = "/category/list", target = RestTarget.GET)
    public List<CategoryView> list() {
        return this.categoryService.list();
    }

}
