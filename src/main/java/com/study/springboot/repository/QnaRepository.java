package com.study.springboot.repository;

import com.study.springboot.entity.qna.QnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<QnaEntity,Long> {
}
