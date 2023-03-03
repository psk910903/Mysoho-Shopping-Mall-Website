<<<<<<< HEAD
package com.study.springboot.service;

import com.study.springboot.dto.BoardResponseDto;
import com.study.springboot.dto.ReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.entity.inquiry.InReplyEntity;
import com.study.springboot.repository.InReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InReplyService {
    private final InReplyRepository inReplyRepository;

//    @Transactional(readOnly = true)
//    public List<InReplyResponseDto> findAllByReplyInquiryNo(Long replyInquiryNo){
//        List<InReplyEntity> list = inReplyRepository.findAllByReplyInquiryNo(replyInquiryNo);
//        return list.stream().map(InReplyResponseDto::new).collect(Collectors.toList());
//    }

    public boolean save(final InReplySaveResponseDto dto){
        try{
            inReplyRepository.save( dto.toEntity() );
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }
        return true;
    }

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

}//class
=======
package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.repository.InReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InReplyService {
    private final InReplyRepository inReplyRepository;

//    @Transactional(readOnly = true)
//    public List<InReplyResponseDto> findAllByReplyInquiryNo(Long replyInquiryNo){
//        List<InReplyEntity> list = inReplyRepository.findAllByReplyInquiryNo(replyInquiryNo);
//        return list.stream().map(InReplyResponseDto::new).collect(Collectors.toList());
//    }

    public boolean save(final InReplySaveResponseDto dto){
        try{
            inReplyRepository.save( dto.toEntity() );
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }
        return true;
    }

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

}//class
>>>>>>> main
