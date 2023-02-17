package com.study.springboot.service;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.entity.inquiry.InquiryEntity;
import com.study.springboot.repository.InquiryRepository;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
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

        //문자열을 날짜형식으로 변환
        //SimpleDateFormat객체를 가져옴
        DateFormat sdFormatStart = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdFormatEnd = new SimpleDateFormat("yyyy-MM-dd");
        //객체에 날짜 값을 넣음 - 1
        Date tempDateStart = sdFormatStart.parse(start);
        Date tempDateEnd = sdFormatEnd.parse(end);
        //캘린더 객체를 가져옴
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //캘린더 객체에 1을 넣음
        cal1.setTime(tempDateStart);
        cal2.setTime(tempDateEnd);
        //SimpleDateFormat객체를 가져옴
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        //cal2.add(필드,amount?);
        cal2.add(Calendar.DATE, +1);

        //날짜형식을 문자열로 변환
        String dateStartStr = df1.format(cal1.getTime())+" 00:00:00";
        String dateEndStr = df2.format(cal2.getTime())+" 00:00:00";

        list = inquiryRepository.findByReviewNoContaining(dateStartStr, dateEndStr, pageable);

        return list.map(InquiryResponseDto::new);
    }
    public Page<InquiryResponseDto> findByDate(String mode, int page) throws ParseException {
        //오늘, 어제, 1주일, 1개월 검색
        //오늘날짜로 date객체 2개 생성 (~부터 ~까지로 검색에 사용목적)
        Date tempDateStart = new Date();
        Date tempDateEnd = new Date();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(tempDateStart);
        cal2.setTime(tempDateEnd);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        //매개변수로 들어온 모드 확인 후 검색기간 수정
        if (mode.equals("today")) {
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("yesterday")) {
            cal1.add(Calendar.DATE, -1);
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("week")) {
            cal1.add(Calendar.DATE, -7);
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("month")) {
            cal1.add(Calendar.MONTH, -1);
            cal2.add(Calendar.DATE, +1);
        }

        String dateStartStr = df1.format(cal1.getTime());
        String dateEndStr = df2.format(cal2.getTime());

        return findByDate(dateStartStr, dateEndStr, page);
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponseDto> findByKeyword(String findBy, String keyword, int page){
        Page<InquiryEntity> list;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("inquiryNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));//검색조건에 맞는 페이지 최신순으로 검색
        System.out.println(findBy);
        if(findBy.contains("memberId")){
            list =  inquiryRepository.findByMemberIdContaining(keyword, pageable);
        }
        else if (findBy.contains("inquiryTitle")) {
            list = inquiryRepository.findByInquiryTitleContaining(keyword, pageable);
        }else {
            list = inquiryRepository.findByInquiryContentContaining(keyword, pageable);
        }
        return  list.map(InquiryResponseDto::new);
    }

    public List<Integer> getPageList(final int totalPage, final int page) {

        List<Integer> pageList = new ArrayList<>();

        if (totalPage <= 5){
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
        }else if(page >= 0 && page <= 2){
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
        }
        else if (page >= totalPage-3 && page <= totalPage-1){
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
        }else{
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
        }

        return pageList;
    }
    @Transactional(readOnly = true)
    public InquiryResponseDto findById(Long inquiryNo){
        InquiryEntity entity = inquiryRepository.findById(inquiryNo)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+inquiryNo));
        return new InquiryResponseDto(entity);
    }

}//class