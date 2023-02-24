package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service2 {

    // 공지사항 --------------------------------------
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public long count() {
        return noticeRepository.count();
    }

    // 나의 문의 내역 (상품문의) ----------------------------------------
    private final InquiryRepository inquiryRepository;

    @Transactional(readOnly = true)
    public List<InquiryResponseDto> findByMemberId(String memberId) {

        List<InquiryEntity> list = inquiryRepository.findByMemberId(memberId);
        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());

    }

    private final InReplyRepository inReplyRepository;

    @Transactional(readOnly = true)
    public Long countByInquiryNo(Long inquiryNo) {
        Long count = inReplyRepository.countByInquiryNo(inquiryNo);
        return count;
    }

    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public String findMemberNameByMemberId(String memberId) {
        String memberName = memberRepository.findMemberNameByMemberId(memberId);
        return memberName;
    }

    @Transactional
    public boolean delete(Long inquiryNo) {

        Optional<InquiryEntity> entity = inquiryRepository.findById(inquiryNo);
        List<InReplyEntity> replyList = inReplyRepository.findAllByReplyInquiryNo(inquiryNo);

        if (!entity.isPresent()){
            return false;
        }
        try{
            inquiryRepository.delete(entity.get());
            for (InReplyEntity replyEntity : replyList) {
                inReplyRepository.delete(replyEntity);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 나의 문의 내역 (qna 문의) ----------------------------------------
    private final QnaRepository qnaRepository;

    @Transactional(readOnly = true)
    public List<QnaResponseDto> findByMemberIdQna(String memberId) { // 이름 중복됨 나중에 service 옮길때 함수 명 변경해야함
        List<QnaEntity> list = qnaRepository.findByMemberId(memberId);
        return list.stream().map(QnaResponseDto::new).collect(Collectors.toList());
    }

    private final QnaCommentRepository qnaCommentRepository;

    @Transactional(readOnly = true)
    public Long countByQnaId(Long qnaId) { // 이름 중복됨 나중에 service 옮길때 함수 명 변경해야함
        Long count = qnaCommentRepository.countByQnaId(qnaId);
        return count;
    }


//    @Transactional
//    public boolean delete(Long inquiryNo) {
//
//        Optional<InquiryEntity> entity = inquiryRepository.findById(inquiryNo);
//        List<InReplyEntity> replyList = inReplyRepository.findAllByReplyInquiryNo(inquiryNo);
//
//        if (!entity.isPresent()){
//            return false;
//        }
//        try{
//            inquiryRepository.delete(entity.get());
//            for (InReplyEntity replyEntity : replyList) {
//                inReplyRepository.delete(replyEntity);
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


}
