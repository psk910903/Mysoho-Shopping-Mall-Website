package com.study.springboot.controller;

import com.study.springboot.dto.review.ReviewSaveDto;
import com.study.springboot.entity.review.Review;
import com.study.springboot.repository.ReviewRopository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class Controller3 {
    //private final ReviewService reviewService;
    private final ReviewRopository reviewRopository;
    //url:http://localhost:8080/review/
    @RequestMapping("/")
    public String main(){
        return "redirect:/review/listForm";
    }
    @RequestMapping("/listForm")
    public String list(Model model){
        //정렬해서 검색하면 되는지 보기
        List <Review> list = reviewRopository.findAll();
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "/admin/review/test";
    }
    @RequestMapping("/modify")
    public String modify(@RequestParam("reviewNo") int no,
                         Model model){
        Optional<Review> optional = reviewRopository.findById((long)no);
        Review review = optional.get();
        model.addAttribute("review",review);
        return "/admin/review/review2";
    }
    @RequestMapping("modifyAction")
    @ResponseBody
    public String modifyAction(ReviewSaveDto dto) {
        try{
            Review review = dto.toUpdateEntity();
            reviewRopository.save(review);
        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('리뷰수정 실패');history.back();</script>";
        }
        return "<script>alert('리뷰수정 성공');location.href='/review/listForm';</script>";
    }
//    throws Exception
//    return "/admin/review/review2";
    @RequestMapping("search")
    public String search(){
        //findByName
        //findByMemberId
        return "/admin/review/test";

    }

}
