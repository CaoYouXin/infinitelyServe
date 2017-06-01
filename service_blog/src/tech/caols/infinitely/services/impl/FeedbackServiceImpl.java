package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.*;
import tech.caols.infinitely.repositories.*;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.FeedbackService;
import tech.caols.infinitely.viewmodels.CommentView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger logger = LogManager.getLogger(FeedbackServiceImpl.class);

    private PostRepository postRepository = new PostRepository();
    private CommentRepository commentRepository = new CommentRepository();
    private CommentDetailRepository commentDetailRepository = new CommentDetailRepository();
    private UserRepository userRepository = new UserRepository();
    private ConfigsRepository configsRepository = new ConfigsRepository();

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
    public List<CommentView> listComments(Long postId, HttpResponse response) {
        List<CommentDetailData> all = this.commentDetailRepository.findAllByPostId(postId);
        List<CommentView> ret = new ArrayList<>();

        all.forEach(logger::info);

        for (CommentDetailData commentDetailData : all) {
            if (commentDetailData.getCommentId() == 0 || commentDetailData.getCommentId() == null) {
                CommentView commentView = new CommentView();
                BeanUtils.copyBean(commentDetailData, commentView);
                ret.add(0, commentView);
            }
        }

        for (CommentDetailData commentDetailData : all) {
            if (commentDetailData.getCommentId() > 0 && commentDetailData.getCommentId() != null) {
                CommentView commentView = new CommentView();
                BeanUtils.copyBean(commentDetailData, commentView);
                for (CommentView view : ret) {
                    if (view.getId().equals(commentView.getCommentId())) {
                        if (view.getFollows() == null) {
                            view.setFollows(new ArrayList<>());
                        }
                        view.getFollows().add(commentView);
                    }
                }
            }
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

        Configs byKey = this.configsRepository.findByKey(ConfigsKeys.AdminUserId);
        if (byKey == null) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("管理员状态异常！"));
            return null;
        }

        CommentData commentData = new CommentData();
        commentData.setUserId(user.getId());
        commentData.setAtUserId(Long.parseLong(byKey.getValue()));
        commentData.setPostId(postId);
        commentData.setContent(content);
        commentData.setCreate(new Date());
        commentData.setDisabled((byte) 0);
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
    public CommentView commentComment(Long postId, Long commentId, String userName, String atUserName, String content, HttpResponse response) {
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
        commentData.setCommentId(commentId);
        commentData.setContent(content);
        commentData.setCreate(new Date());
        commentData.setDisabled((byte) 0);
        if (!this.commentRepository.save(commentData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存评论失败"));
            return null;
        }

        List<CommentDetailData> all = this.commentDetailRepository.findAllByCommentId(commentId);
        CommentView ret = new CommentView();

        for (CommentDetailData commentDetailData : all) {
            if (commentDetailData.getId().equals(commentId)) {
                CommentView commentView = new CommentView();
                BeanUtils.copyBean(commentDetailData, commentView);
                ret = commentView;
            }
        }

        for (CommentDetailData commentDetailData : all) {
            if (commentDetailData.getCommentId().equals(commentId)) {
                CommentView commentView = new CommentView();
                BeanUtils.copyBean(commentDetailData, commentView);

                if (ret.getFollows() == null) {
                    ret.setFollows(new ArrayList<>());
                }
                ret.getFollows().add(commentView);
            }
        }

        return ret;
    }

}
