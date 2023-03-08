package com.study.springboot.repository;

import com.study.springboot.entity.InquiryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity,Long> {

    @Query(value = "SELECT * FROM inquiry WHERE inquiry_date BETWEEN :start AND :end order BY inquiry_date desc", nativeQuery = true)
    Page<InquiryEntity> findByReviewNoContaining(@Param(value="start")String start, @Param(value="end")String end, Pageable sort);
    @Query(value = "SELECT * FROM inquiry WHERE inquiry_date BETWEEN :start AND :end order BY inquiry_date desc", nativeQuery = true)
    List<InquiryEntity> findByReviewNoContaining(@Param(value="start")String start, @Param(value="end")String end);

    Page<InquiryEntity> findByMemberIdContaining(String keyword, Pageable sort);
    List<InquiryEntity> findByMemberIdContaining(String keyword);

    Page<InquiryEntity> findByInquiryContentContaining(String keyword, Pageable sort);
    List<InquiryEntity> findByInquiryContentContaining(String keyword);

    // 2.23 희진 추가 -----------------------------------------------------------------
    List<InquiryEntity> findByMemberId(String memberId);

    //03.08 이준하 추가
    @Query(value = "SELECT COUNT(*) FROM `inquiry` WHERE inquiry_no = :inquiryNo", nativeQuery = true)
    Long countByInquiryNo(@Param(value="inquiryNo")Long inquiryNo);
}