package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.PostDetailData;
import tech.caols.infinitely.db.Repository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.viewmodels.CategorySearch;
import tech.caols.infinitely.viewmodels.PostSearch;
import tech.caols.infinitely.viewmodels.PostSearchWithCategory;
import tech.caols.infinitely.viewmodels.SearchReq;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class PostDetailRepository extends Repository<PostDetailData, Long> {

    public PostDetailRepository() {
        super(PostDetailData.class);
    }

    public List<PostDetailData> findAll() {
        return super.query("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.disabled = 0",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    public List<PostDetailData> findAllByCategoryAndPlatform(String category, String platforms, String resourceLevels) {
        StringJoiner platformsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : platforms.split(",")) {
            platformsStringJoiner.add(s);
        }

        StringJoiner resourceLevelsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(s);
        }

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.disabled = 0 and c.name in %s" +
                        " and a.platform in %s and b.name = ?",
                resourceLevelsStringJoiner.toString(), platformsStringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, category);
    }

    public PostDetailData findByName(String name, String resourceLevels) {
        StringJoiner resourceLevelsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(s);
        }

        List<PostDetailData> postDetailDataList = super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.name = ? and a.disabled = 0 and c.name in %s",
                resourceLevelsStringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, name);

        if (postDetailDataList.size() > 0) {
            return postDetailDataList.get(0);
        }
        return null;
    }

    public List<PostDetailData> search(CategorySearch categorySearch, String resourceLevels) {
        StringJoiner platformsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : categorySearch.getPlatform().split(",")) {
            platformsStringJoiner.add(String.format("a.platform = '%s'", s));
        }

        StringJoiner keywordsStringJoiner = new StringJoiner(" and ");
        categorySearch.getKeywords().forEach(keyword -> {
            keywordsStringJoiner.add(String.format("b.name like '%%%s%%'", keyword));
        });

        StringJoiner resourceLevelsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(String.format("c.name = '%s'", s));
        }

        String timeConditions = timeConditions(categorySearch);

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c, PostIndexData d" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.id = d.postId" +
                        " and %s and %s and a.disabled = 0 and d.disabled = 0 and %s and %s",
                (categorySearch.getPlatform() == null || categorySearch.getPlatform().equals("")) ? "true" : platformsStringJoiner.toString(),
                (categorySearch.getKeywords() == null || categorySearch.getKeywords().isEmpty()) ? "true" : keywordsStringJoiner.toString(),
                (resourceLevels.equals("")) ? "true" : resourceLevelsStringJoiner.toString(),
                (timeConditions.equals("")) ? "true" : timeConditions),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    private String timeConditions(SearchReq searchReq) {
        StringJoiner timeStringJoiner = new StringJoiner(" and ");

        if (searchReq.getYearStart() != null) {
            if (searchReq.getYearEnd() != null) {
                timeStringJoiner.add(String.format("d.year >= %d", searchReq.getYearStart()));
                timeStringJoiner.add(String.format("d.year <= %d", searchReq.getYearEnd()));
            } else {
                timeStringJoiner.add(String.format("d.year = %d", searchReq.getYearStart()));
            }
        }

        if (searchReq.getMonthStart() != null) {
            if (searchReq.getMonthEnd() != null) {
                timeStringJoiner.add(String.format("d.month >= %d", searchReq.getMonthStart()));
                timeStringJoiner.add(String.format("d.month <= %d", searchReq.getMonthEnd()));
            } else {
                timeStringJoiner.add(String.format("d.month = %d", searchReq.getMonthStart()));
            }
        }

        if (searchReq.getDayStart() != null) {
            if (searchReq.getDayEnd() != null) {
                timeStringJoiner.add(String.format("d.day >= %d", searchReq.getDayStart()));
                timeStringJoiner.add(String.format("d.day <= %d", searchReq.getDayEnd()));
            } else {
                timeStringJoiner.add(String.format("d.day = %d", searchReq.getDayStart()));
            }
        }

        return timeStringJoiner.toString();
    }

    public List<PostDetailData> search(PostSearch postSearch, String resourceLevels) {
        StringJoiner platformsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : postSearch.getPlatform().split(",")) {
            platformsStringJoiner.add(String.format("a.platform = '%s'", s));
        }

        StringJoiner keywordsStringJoiner = new StringJoiner(" and ");
        postSearch.getKeywords().forEach(keyword -> {
            keywordsStringJoiner.add(String.format("a.name like '%%%s%%'", keyword));
        });

        StringJoiner resourceLevelsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(String.format("c.name = '%s'", s));
        }

        String timeConditions = timeConditions(postSearch);

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c, PostIndexData d" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.id = d.postId" +
                        " and %s and %s and a.disabled = 0 and d.disabled = 0 and %s and %s",
                (postSearch.getPlatform() == null || postSearch.getPlatform().equals("")) ? "true" : platformsStringJoiner.toString(),
                (postSearch.getKeywords() == null || postSearch.getKeywords().isEmpty()) ? "true" : keywordsStringJoiner.toString(),
                (resourceLevels.equals("")) ? "true" : resourceLevelsStringJoiner.toString(),
                (timeConditions.equals("")) ? "true" : timeConditions),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    public List<PostDetailData> searchWithCategory(PostSearchWithCategory postSearchWithCategory, String resourceLevels) {
        SearchReq searchReq = this.merge(postSearchWithCategory.getCategory(), postSearchWithCategory.getPost());

        StringJoiner platformsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : searchReq.getPlatform().split(",")) {
            platformsStringJoiner.add(String.format("a.platform = '%s'", s));
        }

        StringJoiner categoryKeywordsStringJoiner = new StringJoiner(" and ");
        postSearchWithCategory.getCategory().getKeywords().forEach(keyword -> {
            categoryKeywordsStringJoiner.add(String.format("b.name like '%%%s%%'", keyword));
        });

        StringJoiner postKeywordsStringJoiner = new StringJoiner(" and ");
        postSearchWithCategory.getPost().getKeywords().forEach(keyword -> {
            postKeywordsStringJoiner.add(String.format("a.name like '%%%s%%'", keyword));
        });

        StringJoiner resourceLevelsStringJoiner = new StringJoiner(" or ", "( ", " )");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(String.format("c.name = '%s'", s));
        }

        String timeConditions = timeConditions(searchReq);

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c, PostIndexData d" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.id = d.postId" +
                        " and %s and %s and %s and a.disabled = 0 and d.disabled = 0 and %s and %s",
                (searchReq.getPlatform() == null || searchReq.getPlatform().equals("")) ? "true" : platformsStringJoiner.toString(),
                (postSearchWithCategory.getCategory().getKeywords() == null || postSearchWithCategory.getCategory().getKeywords().isEmpty())
                        ? "true" : categoryKeywordsStringJoiner.toString(),
                (postSearchWithCategory.getPost().getKeywords() == null || postSearchWithCategory.getPost().getKeywords().isEmpty())
                        ? "true" : postKeywordsStringJoiner.toString(),
                (resourceLevels.equals("")) ? "true" : resourceLevelsStringJoiner.toString(),
                (timeConditions.equals("")) ? "true" : timeConditions),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    private SearchReq merge(SearchReq searchReq1, SearchReq searchReq2) {
        if (searchReq1.getPlatform() != null) {
            if (searchReq2.getPlatform() != null) {
                if (!searchReq1.getPlatform().equals(searchReq2.getPlatform())) {
                    throw new RuntimeException("同一请求中要求的Platform不相同");
                }
            }
        } else {
            if (searchReq2.getPlatform() == null) {
                throw new RuntimeException("请求中未包含Platform字段");
            }
            searchReq1.setPlatform(searchReq2.getPlatform());
        }

        searchReq1.setYearStart(Math.min(searchReq1.getYearStart(), searchReq2.getYearStart()));
        searchReq1.setYearEnd(Math.max(searchReq1.getYearEnd(), searchReq2.getYearEnd()));
        searchReq1.setMonthStart(Math.min(searchReq1.getMonthStart(), searchReq2.getMonthStart()));
        searchReq1.setMonthEnd(Math.max(searchReq1.getMonthEnd(), searchReq2.getMonthEnd()));
        searchReq1.setDayStart(Math.min(searchReq1.getDayStart(), searchReq2.getDayStart()));
        searchReq1.setDayEnd(Math.max(searchReq1.getDayEnd(), searchReq2.getDayEnd()));
        return searchReq1;
    }

    public PostDetailData sibling(Date date, boolean greater, String resourceLevels) {
        StringJoiner resourceLevelsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(s);
        }

        List<PostDetailData> postDetailDataList = super.query(String.format(
                "Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.update %s ?" +
                        " and a.type = 'Article'" +
                        " Order By a.update %s Limit 1", greater ? ">" : "<", greater ? "asc" : "desc"
                ),
                new String[]{"tech.caols.infinitely.datamodels."}, date);

        if (postDetailDataList.isEmpty()) {
            return null;
        }
        return postDetailDataList.get(0);
    }

    public List<PostDetailData> top(int count, String resourceLevels) {
        StringJoiner resourceLevelsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : resourceLevels.split(",")) {
            resourceLevelsStringJoiner.add(s);
        }

        return super.query(String.format(
                "Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform, a.resourceLevelId PostDetailData.resourceLevelId," +
                        " c.name PostDetailData.resourceLevelName From PostData a, CategoryData b, LevelData c" +
                        " Where a.categoryId = b.id and a.resourceLevelId = c.id and a.type = 'Article'" +
                        " Order By a.like desc Limit %d", count
                ),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
