package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquirySaveResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSearchDto;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.InquiryRepository;
import com.study.springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Service5 {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public boolean save(final InquirySaveResponseDto dto) {
        try{
            inquiryRepository.save( dto.toEntity() );
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }
        return true;
    }
}




