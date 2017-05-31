package tech.caols.infinitely.services;

import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.viewmodels.CategoryView;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.Date;
import java.util.List;

public interface SearchService {

    List<CategoryView> search4Categories(Date start, Date end, List<String> keywords);

    List<PostView> search4Post(Date start, Date end, List<String> keywords, String platforms, HttpContext context);

    List<PostView> search4Post(Date categoryStart, Date categoryEnd, List<String> categoryKeywords,
                               Date postStart, Date postEnd, List<String> postKeywords, String platforms, HttpContext context);

}
