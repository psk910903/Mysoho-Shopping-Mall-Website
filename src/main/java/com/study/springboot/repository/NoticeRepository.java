package com.study.springboot.repository;

import com.study.springboot.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    List<NoticeEntity> findByNoticeTitleContaining(String keyword, Sort sort);

    List<NoticeEntity> findByNoticeContentContaining(String keyword, Sort sort);

    List<NoticeEntity> findByNoticeType(String keyword, Sort sort);

}
