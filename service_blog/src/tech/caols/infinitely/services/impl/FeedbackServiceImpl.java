package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.repositories.PostRepository;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService {

    private PostRepository postRepository = new PostRepository();

    @Override
    public synchronized Integer like(Long id, HttpResponse response) {
        PostData postData = this.postRepository.find(id);
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

}
