package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.inquiry.InquirySaveResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service5 {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public boolean save(final InquiryResponseDto dto) {
        try{
            inquiryRepository.save( dto.toSaveEntity() );
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }
        return true;
    }

    public List<InquiryResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "inquiryNo");
        List<InquiryEntity> list = inquiryRepository.findAll(sort);
        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }

    public boolean inquiryDelete(long id) {
        try {
            inquiryRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public long findByItemNo(long id) {
        Optional<InquiryEntity> byId = inquiryRepository.findById(id);
        InquiryEntity inquiryEntity = byId.get();
        return inquiryEntity.getItemNo();
    }
    @Transactional
    public boolean inquirypwCheck(InquiryResponseDto inquiryResponseDto){

        Optional<InquiryEntity> byId = inquiryRepository.findById(inquiryResponseDto.getInquiryNo());
        if(byId.isPresent()){
            InquiryEntity inquiryEntity = byId.get();
            if(inquiryEntity.getInquiryPassword().equals(inquiryResponseDto.getInquiryPassword())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Transactional
    public InquiryResponseDto findById(long num){
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(num);
        return new InquiryResponseDto(inquiryEntity.get());
    }

    public boolean inquirySave(InquiryEntity inquiryEntity) {
        try {
            inquiryRepository.save(inquiryEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public Long countByInquriyNo(Long inquiryNo) {
        Long count = inquiryRepository.countByInquiryNo(inquiryNo);
        return count;
    }
}




