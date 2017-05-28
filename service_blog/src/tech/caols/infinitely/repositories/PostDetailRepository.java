package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.PostDetailData;
import tech.caols.infinitely.db.Repository;

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
                        " a.platform PostDetailData.platform From PostData a, CategoryData b Where a.categoryId = b.id",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

    public List<PostDetailData> findAllByCategoryAndPlatform(String category, String platform) {
        StringJoiner stringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : platform.split(",")) {
            stringJoiner.add(s);
        }
        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform From PostData a, CategoryData b Where a.categoryId = b.id" +
                        " and a.platform in %s and b.name = ?", stringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, category);
    }

    public PostDetailData findByName(String name) {
        List<PostDetailData> postDetailDataList = super.query("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform From PostData a, CategoryData b" +
                        " Where a.categoryId = b.id and a.name = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, name);

        if (postDetailDataList.size() > 0) {
            return postDetailDataList.get(0);
        }
        return null;
    }

    public List<PostDetailData> search(Date start, Date end, List<String> keywords, String platforms) {
        StringJoiner platformsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : platforms.split(",")) {
            platformsStringJoiner.add(s);
        }

        StringJoiner keywordsStringJoiner = new StringJoiner(" and ");
        keywords.forEach(keyword -> {
            keywordsStringJoiner.add(String.format("a.name like '%%%s%%'", keyword));
        });

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform From PostData a, CategoryData b" +
                        " Where a.categoryId = b.id and a.update > ? and a.update < ? and" +
                        " a.platform in %s and %s", platformsStringJoiner.toString(), keywordsStringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, start, end);
    }

    public List<PostDetailData> searchWithCategory(Date categoryStart, Date categoryEnd, List<String> categoryKeywords,
                                                   Date postStart, Date postEnd, List<String> postKeywords, String platforms) {
        StringJoiner platformsStringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : platforms.split(",")) {
            platformsStringJoiner.add(s);
        }

        StringJoiner categoryKeywordsStringJoiner = new StringJoiner(" and ");
        categoryKeywords.forEach(keyword -> {
            categoryKeywordsStringJoiner.add(String.format("b.name like '%%%s%%'", keyword));
        });

        StringJoiner postKeywordsStringJoiner = new StringJoiner(" and ");
        postKeywords.forEach(keyword -> {
            postKeywordsStringJoiner.add(String.format("a.name like '%%%s%%'", keyword));
        });

        return super.query(String.format("Select a.id PostDetailData.id, a.name PostDetailData.name," +
                        " a.create PostDetailData.create, a.update PostDetailData.update," +
                        " a.url PostDetailData.url, a.categoryId PostDetailData.categoryId," +
                        " b.name PostDetailData.categoryName, a.type PostDetailData.type," +
                        " a.script PostDetailData.script, a.screenshot PostDetailData.screenshot," +
                        " a.brief PostDetailData.brief, a.like PostDetailData.like," +
                        " a.platform PostDetailData.platform From PostData a, CategoryData b" +
                        " Where a.categoryId = b.id and b.update > ? and b.update < ? and" +
                        " %s and a.update > ? and a.update < ? and a.platform in %s and %s",
                categoryKeywordsStringJoiner.toString(), platformsStringJoiner.toString(), postKeywordsStringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."}, categoryStart, categoryEnd, postStart, postEnd);
    }

}
