package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.SearchService;
import tech.caols.infinitely.services.impl.SearchServiceImpl;
import tech.caols.infinitely.viewmodels.*;

import java.util.List;

@Rest
public class SearchController {

    private SearchService searchService = new SearchServiceImpl();

    @RestAPI(name = "search_category", url = "/search/category", target = RestTarget.POST)
    public List<CategoryView> search4Category(CategorySearch categorySearch) {
        return this.searchService.search4Categories(categorySearch.getStart(), categorySearch.getEnd(), categorySearch.getKeywords());
    }

    @RestAPI(name = "search_post", url = "/search/post", target = RestTarget.POST)
    public List<PostView> search4Post(PostSearch postSearch) {
        return this.searchService.search4Post(postSearch.getStart(), postSearch.getEnd(), postSearch.getKeywords(), postSearch.getPlatform());
    }

    @RestAPI(name = "search_post_with_category", url = "/search/post_with_category", target = RestTarget.POST)
    public List<PostView> search4Post(PostSearchWithCategory postSearchWithCategory) {
        return this.searchService.search4Post(postSearchWithCategory.getCategoryStart(), postSearchWithCategory.getCategoryEnd(),
                postSearchWithCategory.getCategoryKeywords(), postSearchWithCategory.getPostStart(), postSearchWithCategory.getPostEnd(),
                postSearchWithCategory.getPostKeywords(), postSearchWithCategory.getPlatform());
    }

}
