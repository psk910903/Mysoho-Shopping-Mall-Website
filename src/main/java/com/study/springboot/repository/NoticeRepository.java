package com.study.springboot.repository;

import com.study.springboot.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    Page<NoticeEntity> findByNoticeTitleContaining(String keyword, Pageable sort);

    Page<NoticeEntity> findByNoticeContentContaining(String keyword, Pageable sort);

    Page<NoticeEntity> findByNoticeType(String keyword, Pageable sort);

}
