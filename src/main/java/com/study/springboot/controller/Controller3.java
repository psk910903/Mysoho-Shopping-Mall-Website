package com.study.springboot.controller;

import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.OrderService;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/review")
public class Controller3 {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @RequestMapping("/")
    public String main(){
        return "redirect:/admin/review/list";
    }

    //list만 사용하는 전체 출력
    @RequestMapping("/list")
    public String listForm( Model model,
                            @RequestParam(value = "dateStart", required = false) String dateStart,
                            @RequestParam(value = "dateEnd", required = false) String dateEnd,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "keyword", required = false ) String keyword,
                            @RequestParam(value = "findBy", required = false ) String findBy) throws ParseException {

        Page<ReviewResponseDto> list = null;
        int totalPage;
        List<Integer> pageList;
        if ((findBy == null) && (keyword == null) && (dateStart == null) && (dateEnd == null)) {
            //페이징된 리스트 가져오기
            list = reviewService.getPageList(page);
        }else {
            if((!dateStart.equals("")) && (dateEnd.equals(""))){//dateStart 값만 있을때
                list = reviewService.findByDate(dateStart, page);
            }
            else if((!dateStart.equals("")) && (!dateEnd.equals(""))){//
                list = reviewService.findByDate(dateStart, dateEnd, page);
            }else {
                list = reviewService.findByKeyword(findBy, keyword, page);
            }
        }
        //출력페이지 5개로 고정
        totalPage = list.getTotalPages();
        pageList = reviewService.getPageList(totalPage, page);

        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        //검색 상품 개수
        long listCount = reviewRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/review/list";
    }


    //글쓰기
    @RequestMapping("write")
    public String write(){
        return "/admin/review/writeForm";
    }

    @RequestMapping("writeAction")
    public String writeAction(ReviewSaveResponseDto dto){
        reviewService.save(dto);
        return "redirect:/admin/review/list";
    }

    //단건조회
    @RequestMapping("modify")
    public String modify(@RequestParam("reviewNo") int reviewNo,
                         Model model) {
        ReviewEntity review = reviewService.findById((long) reviewNo);

        model.addAttribute("review", review);
        return "/admin/review/modify";
    }
    //단건수정
    @RequestMapping("modifyAction")
    @ResponseBody
    public String modifyAction(ReviewSaveResponseDto reviewSaveResponseDto){
        boolean result = reviewService.update(reviewSaveResponseDto);
        if(!result){
            return "<script>alert('수정 실패');history.back();</script>";
        }return "<script>alert('수정 완료');location.href='/admin/review/list';</script>";
    }

    //삭제하기
    @RequestMapping("/delete")
    public String delete (@RequestParam("reviewNo") String reviewNo){
        reviewService.delete(reviewNo);
        return "redirect:/admin/review/list";
    }


    @ResponseBody
    @RequestMapping("/status/modify")
    public String reviewStatusModify(ReviewSaveResponseDto dto) {
        Long reviewNo = dto.getReviewNo();
        System.out.println("reviewNo = " + reviewNo);
        String reviewExpo = dto.getReviewExpo();
        System.out.println("reviewExpo = " + reviewExpo);
        boolean result = reviewService.statusModify(dto.getReviewNo(), dto.getReviewExpo());
        if (!result) {
            return "<script>alert('노출상태 변경 실패');location.href='/admin/review/list/';</script>";
        }
        return "<script>alert('노출상태 변경 완료');location.href='/admin/review/list/';</script>";
    }
}//class