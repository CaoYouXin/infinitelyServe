package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.datamodels.PostDetailData;
import tech.caols.infinitely.repositories.PostDetailRepository;
import tech.caols.infinitely.base.BaseServiceImpl;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.PostService;
import tech.caols.infinitely.utils.UserLevelUtil;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostServiceImpl extends BaseServiceImpl<PostData, PostView> implements PostService {

    private PostDetailRepository postDetailRepository = new PostDetailRepository();

    public PostServiceImpl() {
        super(PostData.class, PostView.class);
    }

    @Override
    public List<PostView> list() {
        return this.postDetailRepository.findAll().stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PostView> save(PostView postView, HttpResponse response) {
        if (postView.getCreate() == null) {
            postView.setCreate(new Date());
        }
        postView.setUpdate(new Date());

        return super.save(postView, response);
    }

    @Override
    public List<PostView> list(String category, String platform, HttpContext context) {
        return this.postDetailRepository.findAllByCategoryAndPlatform(category, platform, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(Collectors.toList());
    }

    @Override
    public PostView fetch(String name, HttpResponse response, HttpContext context) {
        PostDetailData postDetailData = this.postDetailRepository.findByName(name, UserLevelUtil.getUserLevels(context));
        if (null == postDetailData) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("没有该名称的POST！"));
            return null;
        }

        PostView postView = new PostView();
        BeanUtils.copyBean(postDetailData, postView);
        return postView;
    }
}
