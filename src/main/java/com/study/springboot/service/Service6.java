package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service6 {
    private final MemberRepository memberRepository;


    public boolean pwCheck(MemberSaveRequestDto dto){

        String memberPw = dto.getMemberPw();
        MemberResponseDto responseDto = new MemberResponseDto(memberRepository.findById(dto.getMemberNo()).get());

        if (memberPw.equals(responseDto.getMemberPw())) {
            dto.setMemberPw(dto.getMemberNewPw());
            dto.setMemberJoinDatetime(responseDto.getMemberJoinDatetime());
        } else {
            return false;
        }
        return true;
    }

    private final ReviewRepository reviewRepository;

    @Transactional
    public List<ReviewResponseDto> findByReview(String id ){
        List<ReviewEntity> entityList =  reviewRepository.findByReview(id);
        List<ReviewResponseDto> list = entityList.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
        return list;
    }


//    @Transactional
//    public List<ReviewResponseDto> findByReview(String id){
//        List<ReviewEntity> entityList = reviewRepository.findByReview(id);
//        List<ReviewResponseDto> list = entityList.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
//        return list;
//    }


    //리스트만 사용

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

//    @Transactional(readOnly = true)
//    public Page<ReviewResponseDto> findByKeyword(String findBy, String keyword, int page){
//        Page<ReviewEntity> list;
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("reviewNo"));
//        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));//검색조건에 맞는 페이지 최신순으로 검색
//        System.out.println(findBy);
//        if(findBy.contains("memberId")){
//            list =  reviewRepository.findByMemberIdContaining(keyword, pageable);
//        }
//        else {
//            list = reviewRepository.findByItemNoContaining(keyword, pageable);
//        }
//        return  list.map(ReviewResponseDto::new);
//    }



    public void save(ReviewSaveResponseDto dto){
        ReviewEntity entity = dto.toSaveEntity();
        reviewRepository.save(entity);
    }





}
