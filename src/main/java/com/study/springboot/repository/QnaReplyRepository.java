package com.study.springboot.repository;

import com.study.springboot.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaReplyRepository extends JpaRepository <QnaReplyEntity, Long>{



}
