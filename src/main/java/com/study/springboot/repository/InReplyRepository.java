package com.study.springboot.repository;

import com.study.springboot.entity.InReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InReplyRepository extends JpaRepository<InReplyEntity,Long> {
    List<InReplyEntity> findAllByReplyInquiryNo (Long replyInquiryNo);
}
