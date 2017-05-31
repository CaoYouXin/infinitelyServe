package tech.caols.infinitely.services.impl;

import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.repositories.CategoryRepository;
import tech.caols.infinitely.repositories.PostDetailRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.services.SearchService;
import tech.caols.infinitely.utils.UserLevelUtil;
import tech.caols.infinitely.viewmodels.CategoryView;
import tech.caols.infinitely.viewmodels.PostView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private CategoryRepository categoryRepository = new CategoryRepository();
    private PostDetailRepository postDetailRepository = new PostDetailRepository();

    @Override
    public List<CategoryView> search4Categories(Date start, Date end, List<String> keywords) {
        return this.categoryRepository.search(start, end, keywords).stream().map(categoryData -> {
            CategoryView categoryView = new CategoryView();
            BeanUtils.copyBean(categoryData, categoryView);
            return categoryView;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PostView> search4Post(Date start, Date end, List<String> keywords, String platforms, HttpContext context) {
        return this.postDetailRepository.search(start, end, keywords, platforms, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PostView> search4Post(Date categoryStart, Date categoryEnd, List<String> categoryKeywords, Date postStart, Date postEnd, List<String> postKeywords, String platforms, HttpContext context) {
        return this.postDetailRepository.searchWithCategory(categoryStart, categoryEnd, categoryKeywords, postStart, postEnd, postKeywords, platforms, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(Collectors.toList());
    }
}
