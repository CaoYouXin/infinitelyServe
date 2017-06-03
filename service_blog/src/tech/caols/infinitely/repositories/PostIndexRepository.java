package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.PostIndexData;
import tech.caols.infinitely.db.Repository;

import java.util.List;
import java.util.StringJoiner;

public class PostIndexRepository extends Repository<PostIndexData, Long> {

    public PostIndexRepository() {
        super(PostIndexData.class);
    }

    public boolean softRemove(List<Long> postIds) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        postIds.forEach(postId -> stringJoiner.add(postId + ""));

        return super.update(String.format("Update PostIndexData a Set a.disabled = 1 Where a.postId in %s", stringJoiner.toString()),
                new String[]{"tech.caols.infinitely.datamodels."});
    }

}
