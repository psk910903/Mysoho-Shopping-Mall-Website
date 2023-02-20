package com.study.springboot.repository;

import com.study.springboot.entity.inquiry.InquiryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity,Long> {

    @Query(value = "SELECT * FROM inquiry WHERE inquiry_date BETWEEN :start AND :end order BY inquiry_date desc", nativeQuery = true)
    Page<InquiryEntity> findByReviewNoContaining(@Param(value="start")String start, @Param(value="end")String end, Pageable sort);

    Page<InquiryEntity> findByMemberIdContaining(String keyword, Pageable sort);
    Page<InquiryEntity> findByInquiryTitleContaining(String keyword, Pageable sort);
    Page<InquiryEntity> findByInquiryContentContaining(String keyword, Pageable sort);

}
