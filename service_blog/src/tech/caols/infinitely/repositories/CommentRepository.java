package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.CommentData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class CommentRepository extends Repository<CommentData, Long> {

    public CommentRepository() {
        super(CommentData.class);
    }

}
