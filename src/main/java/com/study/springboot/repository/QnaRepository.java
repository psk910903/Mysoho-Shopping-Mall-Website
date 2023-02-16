package com.study.springboot.repository;

import com.study.springboot.entity.QnaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<QnaEntity,Long> {

    @Modifying
    @Query(value = "update QnaEntity q set q.qnaHit=q.qnaHit+1 where q.qnaId=:id")
    void modifyHits(@Param("id") Long id);

    Page<QnaEntity> findByQnaTitleContaining(String keyword, Pageable sort);

    Page<QnaEntity> findByQnaNameContaining(String keyword, Pageable sort);

    Page<QnaEntity> findByQnaCategoryContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `Qna` WHERE qna_local_date_time BETWEEN :dateStart AND :dateEnd order BY qna_local_date_time desc", nativeQuery = true)
    Page<QnaEntity> findByQnaLocalDateTimeContaining(@Param(value="dateStart")String dateStart, @Param(value="dateEnd")String dateEnd, Pageable sort);
}