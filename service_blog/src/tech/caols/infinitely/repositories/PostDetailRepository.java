package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.PostDetailData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

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
                        " a.brief PostDetailData.brief From PostData a, CategoryData b Where a.categoryId = b.id",
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
