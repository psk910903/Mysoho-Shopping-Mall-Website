package com.study.springboot.controller;

import com.study.springboot.entity.review.Review;
import com.study.springboot.repository.ReviewRopository;
import com.study.springboot.service.ReviewService;
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
    private final ReviewService reviewService;
    private final ReviewRopository reviewRopository;
    //url:http://localhost:8080/review/
    @RequestMapping("/")
    public String main(){
        return "redirect:/review/listForm";
    }
    @RequestMapping("/listForm")
    public String list(Model model){
        List <Review> list = reviewRopository.findAll();
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "review1";
    }
    @RequestMapping("/modify")
    public String modify(@RequestParam("reviewNo") int no,
                         Model model){
        Optional<Review> optional = reviewRopository.findById((long)no);
        Review review = optional.get();
        model.addAttribute("review",review);
        return "review2";
    }
    @RequestMapping("modifyAction")
    public String modifyAction(){

        return "redirect:/review/listForm";
    }
}
