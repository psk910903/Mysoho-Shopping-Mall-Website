package com.study.springboot.repository;

import com.study.springboot.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRopository extends JpaRepository<Review,Long> {
}
