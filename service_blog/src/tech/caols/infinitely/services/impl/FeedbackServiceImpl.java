package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.CommentData;
import tech.caols.infinitely.datamodels.CommentDetailData;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.datamodels.UserData;
import tech.caols.infinitely.repositories.CommentDetailRepository;
import tech.caols.infinitely.repositories.CommentRepository;
import tech.caols.infinitely.repositories.PostRepository;
import tech.caols.infinitely.repositories.UserRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.FeedbackService;
import tech.caols.infinitely.viewmodels.CommentView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackServiceImpl implements FeedbackService {

    private PostRepository postRepository = new PostRepository();
    private CommentRepository commentRepository = new CommentRepository();
    private CommentDetailRepository commentDetailRepository = new CommentDetailRepository();
    private UserRepository userRepository = new UserRepository();

    @Override
    public synchronized Integer like(Long postId, HttpResponse response) {
        PostData postData = this.postRepository.find(postId);
        if (null == postData) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("没有相应Post！"));
            return null;
        }

        int ret = postData.getLike() + 1;
        postData.setLike(ret);
        if (!this.postRepository.save(postData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存Post失败！"));
            return null;
        }
        return ret;
    }

    @Override
    public List<List<CommentView>> listComments(Long postId, HttpResponse response) {
        List<CommentDetailData> all = this.commentDetailRepository.findAllByPostId(postId);

        List<List<CommentView>> ret = new ArrayList<>();
        List<Long> commentIds = new ArrayList<>();
        try {
            all.forEach(commentData -> {
                CommentView commentView = new CommentView();
                BeanUtils.copyBean(commentData, commentView);

                if (commentData.getCommentId() == null) {
                    ArrayList<CommentView> group = new ArrayList<>();
                    group.add(commentView);
                    ret.add(group);
                    commentIds.add(commentData.getId());
                } else if (commentIds.contains(commentData.getCommentId())) {
                    ret.forEach(group -> {
                        if (group.get(0).getId().equals(commentData.getCommentId())) {
                            group.add(commentView);
                        }
                    });
                } else {
                    throw new RuntimeException("评论数据有误！");
                }

            });
        } catch (Exception e) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(e.getMessage()));
            return null;
        }

        return ret;
    }

    @Override
    public CommentView commentPost(Long postId, String userName, String atUserName, String content, HttpResponse response) {
        UserData user = this.userRepository.findUserByUserName(userName);
        if (null == user) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("评论用户不存在！"));
            return null;
        }

        UserData atUser = this.userRepository.findUserByUserName(atUserName);
        if (null == atUser) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("被评论用户不存在！"));
            return null;
        }

        CommentData commentData = new CommentData();
        commentData.setUserId(user.getId());
        commentData.setAtUserId(atUser.getId());
        commentData.setPostId(postId);
        commentData.setContent(content);
        commentData.setCreate(new Date());
        if (!this.commentRepository.save(commentData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存评论失败"));
            return null;
        }

        CommentDetailData commentDetailData = this.commentDetailRepository.findById(commentData.getId());
        if (null == commentDetailData) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存评论失败"));
            return null;
        }

        CommentView commentView = new CommentView();
        BeanUtils.copyBean(commentDetailData, commentView);
        return commentView;
    }

    @Override
    public List<CommentView> commentComment(Long commentId, String userName, String atUserName, String content, HttpResponse response) {
        UserData user = this.userRepository.findUserByUserName(userName);
        if (null == user) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("评论用户不存在！"));
            return null;
        }

        UserData atUser = this.userRepository.findUserByUserName(atUserName);
        if (null == atUser) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("被评论用户不存在！"));
            return null;
        }

        CommentData commentData = new CommentData();
        commentData.setUserId(user.getId());
        commentData.setAtUserId(atUser.getId());
        commentData.setCommentId(commentId);
        commentData.setContent(content);
        commentData.setCreate(new Date());
        if (!this.commentRepository.save(commentData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存评论失败"));
            return null;
        }

        return this.commentDetailRepository.findAllByCommentId(commentId).stream().map(commentDetailData -> {
            CommentView commentView = new CommentView();
            BeanUtils.copyBean(commentDetailData, commentView);
            return commentView;
        }).collect(Collectors.toList());
    }

}
