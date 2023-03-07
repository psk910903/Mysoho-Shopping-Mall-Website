package com.study.springboot.repository;

import com.study.springboot.entity.QnaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<QnaEntity,Long> {

//    @Modifying
//    @Query(value = "update QnaEntity q set q.qnaHit=q.qnaHit+1 where q.qnaId=:id")
//    void modifyHits(@Param("id") Long id);

    Page<QnaEntity> findByQnaContentContaining(String keyword, Pageable sort);

    Page<QnaEntity> findByQnaNameContaining(String keyword, Pageable sort);

    Page<QnaEntity> findByQnaCategoryContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `Qna` WHERE qna_local_date_time BETWEEN :dateStart AND :dateEnd order BY qna_local_date_time desc", nativeQuery = true)
    Page<QnaEntity> findByQnaLocalDateTimeContaining(@Param(value="dateStart")String dateStart, @Param(value="dateEnd")String dateEnd, Pageable sort);

    //----------------------------------------여기서부터 사용자----------------------------------
    @Query(value = "SELECT * FROM `qna` WHERE `qna_content` LIKE CONCAT('%',:keyword,'%') order BY qna_id DESC", nativeQuery = true)
    List<QnaEntity> findByQnaContentContaining(@Param(value="keyword") String keyword);


    // 0224 희진 수정 -----------------------------------------------------------------------
    List<QnaEntity> findByMemberId(String memberId);

}
