package com.study.springboot.service;


import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.review.ReviewEntity;
import com.study.springboot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<ReviewEntity> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("reviewDatetime")); //최신글을 먼저 보여준다.
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return reviewRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC,"reviewNo");
        List<ReviewEntity> list = reviewRepository.findAll(sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findById (final Long reviewNo){
        ReviewEntity entity= reviewRepository.findById(reviewNo)
                .orElseThrow(()->new IllegalArgumentException(
                        "해당 사용자가 없습니다. board_idx="+reviewNo));
        return new ReviewResponseDto( entity );
    }
    @Transactional(readOnly = true)
    public ReviewEntity update(final Long reviewNo , final ReviewSaveResponseDto dto){

        ReviewEntity entity = reviewRepository.findById(reviewNo)
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+reviewNo));

        entity.update(dto.getMemberId(), dto.getItemNo(),
                dto.getReviewStar(), dto.getReviewContent(),
                dto.getReviewImgUrl(), dto.getReviewExpo() );

        ReviewEntity new_entity = reviewRepository.save( entity );

        return new_entity;
    }

    @Transactional
    public boolean delete(final Long reviewNo) {
        System.out.println(reviewNo);
        try {
            Optional<ReviewEntity> optional = reviewRepository.findById(reviewNo);
            ReviewEntity entity = optional.get();
            reviewRepository.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteAll (final String reviewNo){
        String[] arrIdx = reviewNo.split(",");
        try {
            for (int i=0; i<arrIdx.length; i++) {
                System.out.println(arrIdx[i]);
                Optional<ReviewEntity> optional = reviewRepository.findById((long) Integer.parseInt(arrIdx[i]));
                reviewRepository.delete(optional.get());
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}//class
