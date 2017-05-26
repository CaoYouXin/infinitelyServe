package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.CategoryService;
import tech.caols.infinitely.services.PostService;
import tech.caols.infinitely.services.impl.CategoryServiceImpl;
import tech.caols.infinitely.services.impl.PostServiceImpl;
import tech.caols.infinitely.viewmodels.CategoryView;
import tech.caols.infinitely.viewmodels.Deletion;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.List;

@Rest
public class PostController {

    private PostService postService = new PostServiceImpl();

    @RestAPI(name = "list_all_post", url = "/post/list", target = RestTarget.GET)
    public List<PostView> list() {
        return this.postService.list();
    }

    @RestAPI(name = "save_post", url = "/post/save", target = RestTarget.POST)
    public List<PostView> save(PostView postView, HttpResponse response) {
        return this.postService.save(postView, response);
    }

    @RestAPI(name = "delete_post", url = "/post/delete", target = RestTarget.POST)
    public List<PostView> save(Deletion deletion, HttpResponse response) {
        return this.postService.delete(deletion.getIds(), response);
    }

}
