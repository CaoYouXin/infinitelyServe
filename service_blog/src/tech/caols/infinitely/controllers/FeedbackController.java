package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.FeedbackService;
import tech.caols.infinitely.services.impl.FeedbackServiceImpl;
import tech.caols.infinitely.viewmodels.CommentRequest;
import tech.caols.infinitely.viewmodels.CommentView;

import java.util.List;
import java.util.Map;

@Rest
public class FeedbackController {

    private FeedbackService feedbackService = new FeedbackServiceImpl();

    @RestAPI(name = "like_button", url = "/feedback/like", target = RestTarget.GET)
    public Integer like(Map<String, String> parameters, HttpResponse response) {
        return this.feedbackService.like(Long.parseLong(parameters.get("postId")), response);
    }

    @RestAPI(name = "list_comments", url = "/feedback/comment/list", target = RestTarget.GET)
    public List<CommentView> list(Map<String, String> parameters, HttpResponse response) {
        return this.feedbackService.listComments(Long.parseLong(parameters.get("postId")), response);
    }

    @RestAPI(name = "comment_post", url = "/feedback/comment/post", target = RestTarget.POST)
    public CommentView commentPost(CommentRequest commentRequest, HttpResponse response) {
        return this.feedbackService.commentPost(commentRequest.getIdWhatEver(), commentRequest.getUserName(),
                commentRequest.getAtUserName(), commentRequest.getContent(), response);
    }

    @RestAPI(name = "comment_comment", url = "/feedback/comment/comment", target = RestTarget.POST)
    public CommentView commentComment(CommentRequest commentRequest, HttpResponse response) {
        return this.feedbackService.commentComment(commentRequest.getIdWhatEver(), commentRequest.getUserName(),
                commentRequest.getAtUserName(), commentRequest.getContent(), response);
    }

}
