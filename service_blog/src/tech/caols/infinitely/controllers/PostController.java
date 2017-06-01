package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.PostService;
import tech.caols.infinitely.services.impl.PostServiceImpl;
import tech.caols.infinitely.viewmodels.DateReq;
import tech.caols.infinitely.viewmodels.Deletion;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.List;
import java.util.Map;

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
        return this.postService.softDelete(deletion.getIds(), response);
    }

    @RestAPI(name = "list_post_by_category", url = "/post/list_by_category", target = RestTarget.GET)
    public List<PostView> list(Map<String, String> parameters, HttpContext context) {
        return this.postService.list(parameters.get("category"), parameters.get("platform"), context);
    }

    @RestAPI(name = "fetch_post_by_name", url = "/post/fetch_by_name", target = RestTarget.GET)
    public PostView fetch(Map<String, String> parameters, HttpResponse response, HttpContext context) {
        return this.postService.fetch(parameters.get("name"), response, context);
    }

    @RestAPI(name = "previous_post", url = "/post/previous", target = RestTarget.POST)
    public PostView previous(DateReq dateReq, HttpResponse response, HttpContext context) {
        return this.postService.previous(dateReq.getDate(), response, context);
    }

    @RestAPI(name = "next_post", url = "/post/next", target = RestTarget.POST)
    public PostView next(DateReq dateReq, HttpResponse response, HttpContext context) {
        return this.postService.next(dateReq.getDate(), response, context);
    }

    @RestAPI(name = "top_5_posts", url = "/post/list_top_5", target = RestTarget.GET)
    public List<PostView> top5(HttpContext context) {
        return this.postService.top5(context);
    }

}
