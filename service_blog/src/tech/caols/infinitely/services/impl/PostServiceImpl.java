package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.repositories.PostDetailRepository;
import tech.caols.infinitely.rest.BaseServiceImpl;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.services.PostService;
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

}
