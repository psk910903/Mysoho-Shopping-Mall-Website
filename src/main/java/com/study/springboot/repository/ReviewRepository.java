package com.study.springboot.repository;

import com.study.springboot.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<ReviewEntity> findByMemberIdContaining(String MemberId);

    @Query(value = "SELECT * FROM review where item_no LIKE :id order BY review_datetime DESC ", nativeQuery = true)
    List<ReviewEntity> findByReview(@Param(value="id")String id);

    @Query(value = "SELECT * FROM review where review_image_url LIKE 'https%'  order BY review_datetime DESC", nativeQuery = true)
    List<ReviewEntity> findByImgReview(@Param(value = "id")String id);
}
