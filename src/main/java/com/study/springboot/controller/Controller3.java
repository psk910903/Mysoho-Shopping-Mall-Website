package com.study.springboot.controller;

import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.review.ReviewEntity;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/review/")
public class Controller3 {
    private final ReviewService reviewService;


    @RequestMapping("/")
    public String reviewMain(){
        return "redirect:/admin/review/listForm";
    }

//    @RequestMapping("/test")
//    public String test(){
//    }

//    원본
//    @RequestMapping("/listForm")
//    public String reviewListForm(Model model) {
//        //정렬해서 검색하면 되는지 보기
//        List<ReviewResponseDto> list = reviewService.findAll();
//        model.addAttribute("list", list);
//        model.addAttribute("listcount", list.size());
//        return "/admin/review/list";
//    }
    //강사님 정렬
    @RequestMapping("/listForm")
    public String listForm( Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page) {
        //실제론 값이 안 들어와 page=0이 됨. 매개변수로 0에 해당되는 10개의 데이타만 옴
        Page<ReviewEntity> paging = reviewService.getList(page);
        model.addAttribute("paging", paging);
        List<ReviewResponseDto> list = new ArrayList<>();
        for (ReviewEntity entity : paging) {
            list.add(new ReviewResponseDto(entity));
        }
        model.addAttribute("list", list);
        model.addAttribute("listcount", list.size());
        return "/admin/review/list";
    }

    //Page<ReviewEntity> findByMemberIdContaining(String keyword, Pageable sort);
    //단건조회
    @RequestMapping("/modify/{reviewNo}")
    public String reviewModify(@PathVariable("reviewNo") int reviewNo,
                         Model model) {
        ReviewResponseDto review = reviewService.findById((long) reviewNo);
        model.addAttribute("review", review);
        return "/admin/review/modify";
    }
    @RequestMapping("modifyAction")
    @ResponseBody
    public String reviewModifyAction(ReviewSaveResponseDto dto,
                               @RequestParam("reviewNo") int reviewNo
                               ){
        ReviewEntity entity = reviewService.update((long)reviewNo,dto);
        if(entity.getReviewNo() == (long)reviewNo){
            return "<script>alert('수정 성공');location.href='/admin/review/listForm';</script>";
        }else {
            return "<script>alert('수정 실패');history.back();</script>";
        }
    }
//    @RequestMapping("/update")
//    public String reviewUpdate (@RequestParam("reviewNo") String reviewNo,
//                          Model model){
//
//
//
//        List <ReviewResponseDto> list = new ArrayList<>();
//        String[] arrIdx = reviewNo.split(",");
//        for (int i=0; i<arrIdx.length; i++) {
//
//            System.out.println(arrIdx[i]);
//            //수정할 목록 찾아옴
//            Optional<ReviewEntity> optional = reviewService.findById((long)Integer.parseInt(arrIdx[i]));
//            System.out.println(optional.get());
//            ReviewResponseDto dto = new ReviewResponseDto(optional.get());
//            list.add(dto);
//        }
////        List<MemberEntity> list = memberRepository.findAll();//엔티티로 전부 출력
////        model.addAttribute("list",list);
//        System.out.println("1111");
//        model.addAttribute("list",list);
//        model.addAttribute("listcount",list.size());
//        return "/admin/review/test" ;
//    }

    @RequestMapping("/deleteAction")
    @ResponseBody
    public String reviewDeleteAction (@RequestParam("reviewNo") int reviewNo){
        boolean result = reviewService.delete((long)reviewNo);
        System.out.println(result);
        if(!result) {
            return "<script>alert('리뷰삭제 실패');history.back();</script>";
        }
        return "<script>alert('리뷰삭제 성공');location.href='/admin/review/listForm';</script>";
    }

    @RequestMapping ("/delete")
    @ResponseBody
    public String reviewDelete (@RequestParam("reviewNo") String reviewNo){
        System.out.println(reviewNo);
        boolean result = reviewService.deleteAll(reviewNo);
        if(!result) {
            return "<script>alert('리뷰삭제 실패');history.back();</script>";
        }
        return "<script>alert('글삭제 성공!'); location.href='/admin/review/listForm';</script>";
    }



}//class