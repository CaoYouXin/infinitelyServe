package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.PostData;
import tech.caols.infinitely.db.Repository;

public class PostRepository extends Repository<PostData, Long> {

    public PostRepository() {
        super(PostData.class);
    }

}
