package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.entity.repository.InReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InReplyService {

    private final InReplyRepository inReplyRepository;
    private final InquiryService inquiryService;
    @Transactional
    public boolean save(final InReplySaveResponseDto dto){
        try{
            inReplyRepository.save( dto.toEntity() );
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }
        return true;
    }
    @Transactional
    public boolean modify(final InReplySaveResponseDto dto, final Long replyNo){
        try {
            Optional<InReplyEntity> optional = inReplyRepository.findById(replyNo);
            InReplyEntity entity = optional.get();
            entity.update( dto.getReplyContent(), dto.getReplyInquiryNo() );
            inReplyRepository.save( entity );
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
    public boolean delete(Long inquiryReplyNo){
        try {
            Optional<InReplyEntity> optional = inReplyRepository.findById(inquiryReplyNo);
            inReplyRepository.delete(optional.get());
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Transactional(readOnly = true)
    public List<Long> inReplyCount(List<InquiryResponseDto> inquiry){
        List<Long> inReplyCount = new ArrayList<>();

        for(int i =0; i< inquiry.size(); i++){
            Long CommentCount = inquiryService.countByInquiryNo(inquiry.get(i).getInquiryNo());
            inReplyCount.add(CommentCount);
        }
        return inReplyCount;
    }

}//class
