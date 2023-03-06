package com.study.springboot.repository;

import com.study.springboot.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    Page<ReviewEntity> findByMemberIdContaining(String keyword, Pageable sort);

    Page<ReviewEntity> findByItemNoContaining(String keyword, Pageable sort);

    @Query(value = "SELECT * FROM review WHERE review_datetime BETWEEN :start AND :end order BY review_datetime desc", nativeQuery = true)
    Page<ReviewEntity> findByReviewNoContaining(@Param(value="start")String start, @Param(value="end")String end, Pageable sort);

    @Query(value = "SELECT COUNT(*) FROM review WHERE item_no = :item_no", nativeQuery = true)
    int findByItemReview(Long item_no);

    @Query(value = "SELECT AVG(review_star) FROM review WHERE item_no = :item_no", nativeQuery = true)
    Integer findByItemReviewStarAVG(Long item_no);
}
