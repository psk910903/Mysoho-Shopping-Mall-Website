package com.study.springboot.repository;

import com.study.springboot.entity.review.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {

//    Page<ReviewEntity> findByMemberIdContaining(String keyword, Pageable sort);
//
//    Page<ReviewEntity> findByItemNameContaining(String keyword, Pageable sort);


}
