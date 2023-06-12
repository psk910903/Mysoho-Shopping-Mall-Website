package com.study.springboot.entity.repository;

import com.study.springboot.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    // notice_title에 keyword가 포함되있는 페이지 반환
    Page<NoticeEntity> findByNoticeTitleContaining(String keyword, Pageable sort);

    // notice_content에 keyword가 포함되있는 페이지 반환
    @Query(value = "SELECT * FROM `notice` WHERE REGEXP_REPLACE(notice_content, '<[^>]*>|\\&([^;])*;', '') LIKE CONCAT('%',:keyword,'%') order BY notice_no desc", nativeQuery = true)
    Page<NoticeEntity> findByNoticeContentContaining(@Param(value="keyword") String keyword, Pageable sort);

    // notice_type이 keyword인 페이지 반환
    Page<NoticeEntity> findByNoticeType(String keyword, Pageable sort);

    ////////////////////////////////////////////////////////////////////////////////////////////// 희진 추가
    List<NoticeEntity> findByNoticeTitleContaining(String keyword);

    @Query(value = "SELECT notice_title FROM notice ORDER BY notice_datetime DESC LIMIT 1;", nativeQuery = true)
    String findLatestNotice();

}
