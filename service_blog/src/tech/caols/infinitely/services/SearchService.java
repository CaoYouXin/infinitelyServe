package tech.caols.infinitely.services;

import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.viewmodels.*;

import java.util.Date;
import java.util.List;

public interface SearchService {

    List<CategoryView> search4Categories(CategorySearch categorySearch, HttpContext context);

    List<PostView> search4Post(PostSearch postSearch, HttpContext context);

    List<PostView> search4Post(PostSearchWithCategory postSearchWithCategory, HttpContext context);

}
