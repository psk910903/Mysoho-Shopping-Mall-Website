package com.study.springboot.entity.repository;

import com.study.springboot.entity.qna.AdminReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminReplyRepository extends JpaRepository<AdminReplyEntity, Long> {
}
