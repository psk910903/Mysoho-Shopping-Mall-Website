package com.study.springboot.service;


import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.entity.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;

//   기본 출력
   @Transactional(readOnly = true)
        public List<ReviewResponseDto> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC,"reviewNo");
        List<ReviewEntity> list = reviewRepository.findAll(sort);
        return list.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    //리스트만 사용
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getPageList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("reviewNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<ReviewEntity> list = reviewRepository.findAll(pageable);

        return list.map(ReviewResponseDto::new);
    }
    @Transactional(readOnly = true)
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
    public Page<ReviewResponseDto> findByDate(String start, String end, int page) throws ParseException {
        //기간 검색
        Page<ReviewEntity> list;
        Pageable pageable = PageRequest.of(page, 10);

        String[] date = orderService.dateSetting(start, end);
        String dateStartStr = date[0];
        String dateEndStr = date[1];

        list = reviewRepository.findByReviewNoContaining(dateStartStr, dateEndStr, pageable);
        return list.map(ReviewResponseDto::new);
    }
    public Page<ReviewResponseDto> findByDate(String mode, int page) throws ParseException {
        String[] date = orderService.dateSetting(mode);
        String dateStartStr = date[0];
        String dateEndStr = date[1];
        return findByDate(dateStartStr, dateEndStr, page);
    }


    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findByKeyword(String findBy, String keyword, int page){
        Page<ReviewEntity> list;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("reviewNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));//검색조건에 맞는 페이지 최신순으로 검색

        if(findBy.contains("memberId")){
            list =  reviewRepository.findByMemberIdContaining(keyword, pageable);
        }
        else {
            list = reviewRepository.findByItemNoContaining(keyword, pageable);
        }
        return  list.map(ReviewResponseDto::new);
    }


    public boolean save(ReviewSaveResponseDto dto){
       try {
           ReviewEntity entity = dto.toSaveEntity();
           reviewRepository.save(entity);
           return true;
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }

    }

    @Transactional
    public ReviewEntity findById (final Long reviewNo){
        Optional<ReviewEntity> optional = reviewRepository.findById(reviewNo);
        ReviewEntity entity = optional.get();
        return entity;
    }

    public boolean update(ReviewSaveResponseDto reviewSaveResponseDto){
        try {
            ReviewEntity entity = reviewSaveResponseDto.toUpdateEntity();
            reviewRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Transactional
    public void delete (final String reviewNo){
        String[] arrIdx = reviewNo.split(",");
        for (int i=0; i<arrIdx.length; i++) {
            Optional<ReviewEntity> optional = reviewRepository.findById(  (long) Integer.parseInt(arrIdx[i])  );
            reviewRepository.delete(optional.get());
        }
    }

    @Transactional
    public boolean statusModify(Long id, String reviewState) {
        try {
            ReviewEntity entity = findById(id);
            ReviewResponseDto dto = new ReviewResponseDto(entity);
            dto.setReviewExpo(reviewState);
            reviewRepository.save(dto.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<ReviewResponseDto> findByMemberId(final String memberId){

        List<ReviewEntity> list =  reviewRepository.findByMemberIdContaining(memberId);

        return list.stream().map(ReviewResponseDto::new).toList();
    }
    @Transactional(readOnly = true)
    public String findByUrl(Long id) {
        ReviewEntity entity = reviewRepository.findById(id).get();
        ReviewResponseDto dto = new ReviewResponseDto(entity);
        return dto.getReviewImgUrl();
    }

    //리뷰 아이디 마스킹
    public List<String> maskingId(List<ReviewResponseDto> list) {
        List<String> photoReviewIdList = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){

            String reviewId = list.get(i).getMemberId();

            String reviewHiddenId;
            if (reviewId.length() == 2){
                reviewHiddenId = reviewId.replace(reviewId.charAt(1), '*');
            }else if(reviewId.length() == 1){
                reviewHiddenId = reviewId;
            }
            else{
                reviewHiddenId = reviewId.substring(0,2);
                for (int j=0; j<reviewId.length()-2; j++){
                    reviewHiddenId += "*";
                }
            }
            photoReviewIdList.add(reviewHiddenId);
        }
        return photoReviewIdList;
    }

    //평점
    public Double avgStar(List<ReviewResponseDto> reviewList) {
        int size = reviewList.size();  // 3. 상품 리뷰 갯수

        byte sum = 0;       // 4. 상품별점 평균
        for(int i=0; i<size; i++){
            byte reviewStar = reviewList.get(i).getReviewStar();
            sum += reviewStar;
        }
        double avg1 = sum / Double.valueOf(size);
        double avg2 = Math.round(avg1*10);
        double avgStar = avg2 / 10;
        return avgStar;
    }

    @Transactional
    public List<ReviewResponseDto> findByReview(String id ){
        List<ReviewEntity> entityList =  reviewRepository.findByReview(id);
        return entityList.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponseDto> findByImgReview(String id){
        List<ReviewEntity> entityList = reviewRepository.findByImgReview(id);  //경빈 Repository수정으로 새로만듬
        return entityList.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

}//class
