package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.repository.InReplyRepository;
import com.study.springboot.entity.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InReplyRepository inReplyRepository;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> getPage(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("inquiryNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<InquiryEntity> list = inquiryRepository.findAll(pageable);
        return list.map(InquiryResponseDto::new);
    }
    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> findByDate(String start, String end, int page) throws ParseException {
        //기간 검색
        Page<InquiryEntity> list;
        Pageable pageable = PageRequest.of(page, 10);

        String[] date = orderService.dateSetting(start, end);
        String dateStartStr = date[0];
        String dateEndStr = date[1];

        list = inquiryRepository.findByReviewNoContaining(dateStartStr, dateEndStr, pageable);

        return list.map(InquiryResponseDto::new);
    }
    public Page<InquiryResponseDto> findByDate(String mode, int page) throws ParseException {
        String[] date = orderService.dateSetting(mode);
        String dateStartStr = date[0];
        String dateEndStr = date[1];
        return findByDate(dateStartStr, dateEndStr, page);
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> findByKeyword(String findBy, String keyword, int page){
        Page<InquiryEntity> list;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("inquiryNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));//검색조건에 맞는 페이지 최신순으로 검색

        if(findBy.contains("memberId")){
            list =  inquiryRepository.findByMemberIdContaining(keyword, pageable);
        }
        else {
            list = inquiryRepository.findByInquiryContentContaining(keyword, pageable);
        }
        return  list.map(InquiryResponseDto::new);
    }

    @Transactional(readOnly = true)
    public InquiryResponseDto findById(Long inquiryNo){
        InquiryEntity entity = inquiryRepository.findById(inquiryNo)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+inquiryNo));
        return new InquiryResponseDto(entity);
    }

    public List<InquiryResponseDto> findAll() {
        List<InquiryEntity> list = inquiryRepository.findAll();
        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }

    public boolean delete(Long id) {

        Optional<InquiryEntity> optional = inquiryRepository.findById(id);
        if (!optional.isPresent()) {
            return false;
        }
        InquiryEntity entity = optional.get();
        List<InReplyEntity> replyList = inReplyRepository.findAllByReplyInquiryNo(id);

        try {
            inquiryRepository.delete(entity);
            for (InReplyEntity temp : replyList) {
                inReplyRepository.delete(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //상품문의 아이디 마스킹
    public List<String> inquiryMaskingId(List<InquiryResponseDto> list) {
        List<String> nameList = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){

            String qnaName = list.get(i).getMemberId();
            if(qnaName == null){
                qnaName = list.get(i).getInquiryNickname();
            }
            String qnaHiddenName;
            if (qnaName.length() == 2){
                qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
            }else if(qnaName.length() == 1){
                qnaHiddenName = qnaName;
            }
            else{
                qnaHiddenName = qnaName.substring(0,2);
                for (int j=0; j<qnaName.length()-2; j++){
                    qnaHiddenName += "*";
                }
            }
            nameList.add(qnaHiddenName);
        }
        return nameList;
    }

    // 나의 문의 내역 (상품문의) ----------------------------------------
    @Transactional(readOnly = true)
    public List<InquiryResponseDto> findByMemberId(String memberId) {

        List<InquiryEntity> list = inquiryRepository.findByMemberId(memberId);
        return list.stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countByInquiryNo(Long inquiryNo) {
        Long count = inReplyRepository.countByInquiryNo(inquiryNo);
        return count;
    }

    @Transactional
    public boolean inquiryDelete(Long inquiryNo) {

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

    public long findByItemNo(long id) {
        Optional<InquiryEntity> byId = inquiryRepository.findById(id);
        InquiryEntity inquiryEntity = byId.get();
        return inquiryEntity.getItemNo();
    }
    @Transactional
    public boolean inquiryPwCheck(InquiryResponseDto inquiryResponseDto){

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
    public List<InquiryResponseDto> findByItemNoList(Long itemNo) {
        return inquiryRepository.findByItemNo(itemNo).stream().map(InquiryResponseDto::new).collect(Collectors.toList());
    }

    public String maskingId(String memberName) {
        String memberHiddenName;
        if (memberName.length() <= 2){
            memberHiddenName = memberName;
        }
        else{
            memberHiddenName = memberName.substring(0,2);
            for (int i=0; i<memberName.length()-2; i++) memberHiddenName += "*";
        }
        return memberHiddenName;
    }

}//class