package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.inquiry.InquirySaveResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.InReplyRepository;
import com.study.springboot.repository.InquiryRepository;
import com.study.springboot.repository.MemberRepository;
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
    private final InReplyRepository inReplyRepository;
    private final MemberRepository memberRepository;

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

//    public List<InquiryResponseDto> findAll() {
//        Sort sort = Sort.by(Sort.Direction.DESC, "inquiryNo");
//        List<InquiryEntity> list = inquiryRepository.findAll(sort);
//        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());
//    }

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

    @Transactional(readOnly = true)
    public Long countByInquiryNo(Long inquiryNo) {
        Long count = inReplyRepository.countByInquiryNo(inquiryNo);
        return count;
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findByMemberIdMember(String memberId){ // 이름 바꿔야함
        Optional<MemberEntity> entity = memberRepository.findByMemberId(memberId);
        if (!entity.isPresent()){
            return null;
        }
        return new MemberResponseDto(entity.get());
    };

    public List<InquiryResponseDto> findByItemNoList(Long itemNo) {
        List<InquiryEntity> list = inquiryRepository.findByItemNo(itemNo);
        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }
}




