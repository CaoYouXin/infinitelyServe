package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.base.BaseService;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.Date;
import java.util.List;

public interface PostService extends BaseService<PostData, PostView> {

    List<PostView> list(String category, String platform, HttpContext context);

    PostView fetch(String name, HttpResponse response, HttpContext context);

    PostView previous(Date date, HttpResponse response, HttpContext context);

    PostView next(Date date, HttpResponse response, HttpContext context);

    List<PostView> top5(HttpContext context);

}