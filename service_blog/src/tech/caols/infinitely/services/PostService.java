package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.rest.BaseService;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.List;

public interface PostService extends BaseService<PostData, PostView> {

    List<PostView> list(String category, String platform);

    PostView fetch(String name, HttpResponse response);

}