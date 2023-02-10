package com.study.springboot.service;


import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.review.ReviewEntity;
import com.study.springboot.repository.ReviewRopository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRopository reviewRopository;

    public List<ReviewResponseDto> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC,"reviewNo");
        List<ReviewEntity> list = reviewRopository.findAll(sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    public ReviewEntity modify (final Long reviewNo){
        Optional<ReviewEntity> optional = reviewRopository.findById(reviewNo);
        ReviewEntity entity = optional.get();
        return entity;
    }

    public boolean update(ReviewSaveResponseDto reviewSaveResponseDto){
        try {
            ReviewEntity entity = reviewSaveResponseDto.toUpdateEntity();
            reviewRopository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @RequestMapping("/delete")
    public void delete (final String reviewNo){
        String[] arrIdx = reviewNo.split(",");
        for (int i=0; i<arrIdx.length; i++) {
            Optional<ReviewEntity> optional = reviewRopository.findById((long) Integer.parseInt(arrIdx[i]));
            reviewRopository.delete(optional.get());
        }
    }

}//class
