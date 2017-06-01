package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.CommentView;

import java.util.List;

public interface FeedbackService {

    Integer like(Long postId, HttpResponse response);

    List<CommentView> listComments(Long postId, HttpResponse response);

    CommentView commentPost(Long postId, String userName, String atUserName, String content, HttpResponse response);

    CommentView commentComment(Long commentId, String userName, String atUserName, String content, HttpResponse response);

}
