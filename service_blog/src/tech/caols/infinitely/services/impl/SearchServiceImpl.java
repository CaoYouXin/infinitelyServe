package tech.caols.infinitely.services.impl;

import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.repositories.CategoryRepository;
import tech.caols.infinitely.repositories.PostDetailRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.services.SearchService;
import tech.caols.infinitely.utils.UserLevelUtil;
import tech.caols.infinitely.viewmodels.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private CategoryRepository categoryRepository = new CategoryRepository();
    private PostDetailRepository postDetailRepository = new PostDetailRepository();

    private BiConsumer<ArrayList<PostView>, ArrayList<PostView>> arrayListArrayListBiConsumer = (one, two) -> {
        two.forEach(item -> {
            for (int i = 0; i < one.size(); i++) {
                if (one.get(i).getId().equals(item.getId())) {
                    return;
                }
            }
            one.add(item);
        });
    };
    private BiConsumer<ArrayList<PostView>, PostView> arrayListPostViewBiConsumer = (list, item) -> {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                return;
            }
        }
        list.add(item);
    };

    @Override
    public List<CategoryView> search4Categories(CategorySearch categorySearch, HttpContext context) {
        return this.postDetailRepository.search(categorySearch, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            CategoryView categoryView = new CategoryView();
            categoryView.setName(postDetailData.getCategoryName());
            return categoryView;
        }).collect(ArrayList::new, (list, item) -> {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(item.getName())) {
                    return;
                }
            }
            list.add(item);
        }, (one, two) -> {
            two.forEach(item -> {
                for (int i = 0; i < one.size(); i++) {
                    if (one.get(i).getName().equals(item.getName())) {
                        return;
                    }
                }
                one.add(item);
            });
        });
    }

    @Override
    public List<PostView> search4Post(PostSearch postSearch, HttpContext context) {
        return this.postDetailRepository.search(postSearch, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(ArrayList::new, this.arrayListPostViewBiConsumer, this.arrayListArrayListBiConsumer);
    }

    @Override
    public List<PostView> search4Post(PostSearchWithCategory postSearchWithCategory, HttpContext context) {

        return this.postDetailRepository.searchWithCategory(postSearchWithCategory, UserLevelUtil.getUserLevels(context)).stream().map(postDetailData -> {
            PostView postView = new PostView();
            BeanUtils.copyBean(postDetailData, postView);
            return postView;
        }).collect(ArrayList::new, this.arrayListPostViewBiConsumer, this.arrayListArrayListBiConsumer);
    }
}
