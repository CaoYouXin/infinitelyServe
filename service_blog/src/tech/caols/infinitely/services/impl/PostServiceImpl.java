package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.datamodels.PostDetailData;
import tech.caols.infinitely.datamodels.PostIndexData;
import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.repositories.PostDetailRepository;
import tech.caols.infinitely.base.BaseServiceImpl;
import tech.caols.infinitely.repositories.PostIndexRepository;
import tech.caols.infinitely.repositories.PostRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.PostService;
import tech.caols.infinitely.utils.UserLevelUtil;
import tech.caols.infinitely.viewmodels.PostView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PostServiceImpl extends BaseServiceImpl<PostData, PostView> implements PostService {

    private PostRepository postRepository = new PostRepository();
    private PostDetailRepository postDetailRepository = new PostDetailRepository();
    private PostIndexRepository postIndexRepository = new PostIndexRepository();

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

        PostData postData = new PostData();
        BeanUtils.copyBean(postView, postData);

        if (!this.postRepository.save(postData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(String.format("保存Post失败! ")));
            return null;
        }

        Date update = postData.getUpdate();
        String format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(update);
        String year = null, month = null, day = null;
        int indexOf = format.indexOf('-');
        year = format.substring(0, indexOf);
        indexOf = format.indexOf('-', indexOf + 1);
        month = format.substring(year.length() + 1, indexOf);
        day = format.substring(indexOf + 1);

        PostIndexData postIndexData = this.postIndexRepository.findByPostId(postData.getId());
        if (null == postIndexData) {
            postIndexData = new PostIndexData();
        }
        postIndexData.setDisabled((byte) 0);
        postIndexData.setPostId(postData.getId());
        postIndexData.setYear(Integer.parseInt(year));
        postIndexData.setMonth(Integer.parseInt(month));
        postIndexData.setDay(Integer.parseInt(day));
        if (!this.postIndexRepository.save(postIndexData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(String.format("保存Post Index失败! ")));
            return null;
        }

        return this.list();
    }

    @Override
    public List<PostView> softDelete(List<Long> ids, HttpResponse response) {
        if (!this.postIndexRepository.softRemove(ids)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("删除Post Index失败！"));
            return null;
        }

        return super.softDelete(ids, response);
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

    @Override
    public PostView previous(Date date, HttpResponse response, HttpContext context) {
        PostDetailData sibling = this.postDetailRepository.sibling(date, false, UserLevelUtil.getUserLevels(context));
        if (null == sibling) {
            PostView ret = new PostView();
            ret.setName("没有上一篇了");
            return ret;
        }

        PostView ret = new PostView();
        BeanUtils.copyBean(sibling, ret);
        return ret;
    }

    @Override
    public PostView next(Date date, HttpResponse response, HttpContext context) {
        PostDetailData sibling = this.postDetailRepository.sibling(date, true, UserLevelUtil.getUserLevels(context));
        if (null == sibling) {
            PostView ret = new PostView();
            ret.setName("没有下一篇了");
            return ret;
        }

        PostView ret = new PostView();
        BeanUtils.copyBean(sibling, ret);
        return ret;
    }

    @Override
    public List<PostView> top5(HttpContext context) {
        return this.postDetailRepository.top(5, UserLevelUtil.getUserLevels(context))
                .stream().map(postDetailData -> {
                    PostView postView = new PostView();
                    BeanUtils.copyBean(postDetailData, postView);
                    return postView;
                }).collect(Collectors.toList());
    }
}
