package com.study.springboot.repository;

import com.study.springboot.entity.QnaCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QnaCommentRepository extends JpaRepository<QnaCommentEntity,Long> {
    @Query(value = "SELECT * FROM qnaComment WHERE comment_qna_id = :idx", nativeQuery = true)
    List<QnaCommentEntity> findByCommentQnaId_nativeQuery(@Param("idx") Long idx);

//    @Query(value = "SELECT * FROM reply WHERE reply_board_idx = :replyBoardIdx", nativeQuery = true)
//    List<Reply> findByreplyBoardIdx_nativeQuery(Long replyBoardIdx);
}
