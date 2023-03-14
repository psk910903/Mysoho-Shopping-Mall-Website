package com.study.springboot.controller;

import com.study.springboot.dto.product.FileResponse;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.AwsS3Service;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final AwsS3Service awsS3Service;

    //나의 후기 모아보기
    @RequestMapping("/review/myList")
    public String myReview(@AuthenticationPrincipal User user,
                           Model model) {
        String memberId = user.getUsername();

        List<ReviewResponseDto> list = reviewService.findByMemberId(memberId);

        List<String> itemName = new ArrayList<>();
        for (ReviewResponseDto dto : list) {
            String itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(Long.parseLong(itemNo));
            itemName.add(itemDto.getItemName());//item이름을 itemList에 넣어줌
        }
        List<String> itemUrl = new ArrayList<>();
        for (ReviewResponseDto dto : list) {
            String itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(Long.parseLong(itemNo));
            itemUrl.add(itemDto.getItemImageUrl());//item사진을 itemList에 넣어줌
        }
        int listSize = list.size();
        model.addAttribute("listSize",listSize);
        model.addAttribute("list",list);
        model.addAttribute("itemName", itemName);
        model.addAttribute("itemUrl", itemUrl);

        return "user/user/review-mylist";
    }

    //후기 작성하기 폼
    @RequestMapping("/myorder/writeForm")
    public String myReviewWrite(@RequestParam("itemCode") Long itemCode,
                                @AuthenticationPrincipal User user,
                                Model model
    ){
        String memberId = user.getUsername();

        model.addAttribute("memberId",memberId);
        model.addAttribute("itemCode",itemCode);
        model.addAttribute("itemName",productRepository.findById(itemCode).get().getItemName());
        model.addAttribute("itemImageUrl",productRepository.findById(itemCode).get().getItemImageUrl());

        return "user/user/review-writeForm";
    }
    //후기 수정 폼
    @RequestMapping("/review/modify")
    public String reviewModify(@RequestParam("reviewNo") Long reviewNo,
                               Model model){
        ReviewEntity entity = reviewRepository.findById(reviewNo).orElseThrow();
        model.addAttribute("reviewNo",reviewNo);
        model.addAttribute("review",entity);
        model.addAttribute("itemName",productRepository.findById(Long.parseLong(entity.getItemNo())).get().getItemName());
        model.addAttribute("itemImageUrl",productRepository.findById(Long.parseLong(entity.getItemNo())).get().getItemImageUrl());
        return "user/user/review-writeForm";
    }
    //후기 작성하기(글쓰기)
    @RequestMapping("/review/writeAction")
    @ResponseBody
    public String writeAction(@RequestParam MultipartFile uploadfile, ReviewSaveResponseDto dto){
        LocalDateTime today = LocalDateTime.now();
        String url;
        if (uploadfile.getOriginalFilename().equals("")) { //업로드한 이미지가 없을 때
            url = "";
        } else {
            url = awsS3Service.upload(uploadfile);
            new ResponseEntity<>(FileResponse.builder().
                    uploaded(true).
                    url(url).
                    build(), HttpStatus.OK);
        }

        dto.setReviewImgUrl(url);
        dto.setReviewDatetime(today);
        boolean result = reviewService.save(dto);
        if(!result){
            return "<script> alert('후기 작성에 실패했습니다.'); history.back();</script>";
        }else {
            return "<script> alert('후기 작성에 성공했습니다.'); location.href='/review/myList';</script>";
        }
    }

    //후기 삭제
    @RequestMapping("/review/delete")
    @ResponseBody
    public String reviewDelete(@RequestParam("reviewNo") Long reviewNo){
        try {
            ReviewEntity entity = reviewRepository.findById(reviewNo).orElseThrow();
            reviewRepository.delete(entity);
            return "<script>alert('후기가 삭제됬습니다.'); location.href='/review/myList'</script>";
        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('후기 삭제실패'); history.back(); </script>";
        }
    }


    //후기 수정하기
    @RequestMapping("/review/modifyAction")
    @ResponseBody
    public String reviewModifyAction(@RequestParam MultipartFile uploadfile,
                                     @RequestParam("imgDelete") String imgDelete,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")ReviewSaveResponseDto dto

    ){
        String url;
        if (uploadfile.getOriginalFilename().equals("")) { //업로드한 이미지가 없을 때
            url = reviewService.findByUrl(dto.getReviewNo());
            if (imgDelete.equals("imgDelete")) { //기존 이미지 삭제했을 때
                url = "";
            }
        } else {
            url = awsS3Service.upload(uploadfile);
            new ResponseEntity<>(FileResponse.builder().
                    uploaded(true).
                    url(url).
                    build(), HttpStatus.OK);
        }
        dto.setReviewImgUrl(url);

        try {
            ReviewEntity entity = dto.toUpdateEntity();

            reviewRepository.save(entity);
            return "<script>alert('수정 성공했습니다.'); location.href='/review/myList'</script>";
        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('수정 실패했습니다.'); history.back(); </script>";
        }
    }
}
