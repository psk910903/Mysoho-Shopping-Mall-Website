package com.study.springboot.controller;

import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.review.ReviewEntity;
import com.study.springboot.repository.ReviewRopository;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class Controller3 {
    private final ReviewService reviewService;
    private final ReviewRopository reviewRopository;

    //url:http://localhost:8080/review/
    @RequestMapping("/")
    public String main(){
        return "redirect:/review/listForm";
    }
    @RequestMapping("listForm")
    public String list(Model model){
        //정렬해서 검색하면 되는지 보기
        List <ReviewResponseDto> list = reviewService.findAll();
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "/admin/review/reviewList";
    }
    //단건조회
    @RequestMapping("modify")
    public String modify(@RequestParam("reviewNo") int reviewNo,

                         Model model) {
        ReviewEntity review = reviewService.modify((long) reviewNo);

        model.addAttribute("review", review);
        return "/admin/review/reviewModify";
    }
    @RequestMapping("modifyAction")
    public String modifyAction(ReviewSaveResponseDto reviewSaveResponseDto){
        reviewService.update(reviewSaveResponseDto);
        return "/admin/review/reviewList";
    }

    @RequestMapping("/update")
    public String update (@RequestParam("reviewNo") String reviewNo,
                          Model model){
        List <ReviewEntity> list = new ArrayList<>();
        String[] arrIdx = reviewNo.split(",");
        for (int i=0; i<arrIdx.length; i++) {
            //수정할 목록 찾아서 목록 만듬
            Optional<ReviewEntity> optional = reviewRopository.findById((long)Integer.parseInt(arrIdx[i]));
            list.add(optional.get());
        }
//        List<MemberEntity> list = memberRepository.findAll();//엔티티로 전부 출력
//        model.addAttribute("list",list);
        System.out.println("배열완료");
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "/admin/review/reviewList" ;
    }

    //선택삭제하기
    @RequestMapping("/delete")
    public String delete (@RequestParam("reviewNo") String reviewNo){
        reviewService.delete(reviewNo);
        return "redirect:/review/listForm";
    }

}//class