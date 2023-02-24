package com.study.springboot.repository;

import com.study.springboot.entity.InReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InReplyRepository extends JpaRepository<InReplyEntity,Long> {
    List<InReplyEntity> findAllByReplyInquiryNo (Long replyInquiryNo);

    // 02-23 희진 -----------------------------------------------------------------
    @Query(value = "SELECT COUNT(*) FROM `inquiry_reply` WHERE inquiry_no = :inquiryNo", nativeQuery = true)
    Long countByInquiryNo(@Param(value="inquiryNo")Long inquiryNo);
}
