package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.CommentData;
import tech.caols.infinitely.datamodels.CommentDetailData;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class CommentDetailRepository extends Repository<CommentDetailData, Long> {

    public CommentDetailRepository() {
        super(CommentDetailData.class);
    }

    public List<CommentDetailData> findAllByPostId(Long postId) {
        return super.query("Select a.id CommentDetailData.id, a.postId CommentDetailData.postId," +
                        " a.commentId CommentDetailData.commentId, a.userId CommentDetailData.userId," +
                        " b.userName CommentDetailData.userName, a.atUserId CommentDetailData.atUserId," +
                        " c.userName CommentDetailData.atUserName, a.content CommentDetailData.content," +
                        " a.create CommentDetailData.create From CommentData a, UserData b, UserData c Where a.userId = b.id" +
                        " and a.atUserId = c.id and a.disabled = 0 and a.postId = ? Order By a.create asc",
                new String[]{"tech.caols.infinitely.datamodels."}, postId);
    }

    public CommentDetailData findById(Long id) {
        List<CommentDetailData> commentDetailDataList = super.query(
                "Select a.id CommentDetailData.id, a.postId CommentDetailData.postId," +
                        " a.commentId CommentDetailData.commentId, a.userId CommentDetailData.userId," +
                        " b.userName CommentDetailData.userName, a.atUserId CommentDetailData.atUserId," +
                        " c.userName CommentDetailData.atUserName, a.content CommentDetailData.content," +
                        " a.create CommentDetailData.create From CommentData a, UserData b, UserData c Where a.userId = b.id" +
                        " and a.atUserId = c.id and a.disabled = 0 and a.id = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, id);

        if (commentDetailDataList.size() > 0) {
            return commentDetailDataList.get(0);
        }
        return null;
    }

    public List<CommentDetailData> findAllByCommentId(Long commentId) {
        List<CommentDetailData> commentDetailDataList1 = super.query(
                "Select a.id CommentDetailData.id, a.postId CommentDetailData.postId," +
                        " a.commentId CommentDetailData.commentId, a.userId CommentDetailData.userId," +
                        " b.userName CommentDetailData.userName, a.atUserId CommentDetailData.atUserId," +
                        " c.userName CommentDetailData.atUserName, a.content CommentDetailData.content," +
                        " a.create CommentDetailData.create From CommentData a, UserData b, UserData c Where a.userId = b.id" +
                        " and a.atUserId = c.id and a.disabled = 0 and a.id = ?",
                new String[]{"tech.caols.infinitely.datamodels."}, commentId);

        List<CommentDetailData> commentDetailDataList2 = super.query(
                "Select a.id CommentDetailData.id, a.postId CommentDetailData.postId," +
                        " a.commentId CommentDetailData.commentId, a.userId CommentDetailData.userId," +
                        " b.userName CommentDetailData.userName, a.atUserId CommentDetailData.atUserId," +
                        " c.userName CommentDetailData.atUserName, a.content CommentDetailData.content," +
                        " a.create CommentDetailData.create From CommentData a, UserData b, UserData c Where a.userId = b.id" +
                        " and a.atUserId = c.id and a.disabled = 0 and a.commentId = ? Order By a.create asc",
                new String[]{"tech.caols.infinitely.datamodels."}, commentId);
        commentDetailDataList1.addAll(commentDetailDataList2);

        return commentDetailDataList1;
    }

}
