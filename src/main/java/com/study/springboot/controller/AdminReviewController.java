package com.study.springboot.controller;

import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {
    final ReviewService reviewService;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;

    // 관리자페이지 리뷰 상세 정보 admin/review/content?reviewNo=12
    @RequestMapping("/admin/review/content/{reviewNo}")
    public String adminReviewContent(Model model, @PathVariable(value = "reviewNo") Long reviewNo){
        ReviewEntity review = reviewService.findById(reviewNo);

        model.addAttribute("review", review);
        return "/admin/review/content";
    }

    @RequestMapping("/admin/review")
    public String main() {
        return "redirect:/admin/review/list";
    }

    //관리자 후기 목록
    @RequestMapping("/admin/review/list")
    public String listForm(Model model,
                           @RequestParam(value = "dateStart", required = false) String dateStart,
                           @RequestParam(value = "dateEnd", required = false) String dateEnd,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "findBy", required = false) String findBy) throws ParseException {

        Page<ReviewResponseDto> list = null;
        int totalPage;
        List<Integer> pageList;
        if ((findBy == null) && (keyword == null) && (dateStart == null) && (dateEnd == null)
                || (dateStart.equals("null")) && (dateEnd.equals("null")) && (keyword.equals("null"))
                || (dateStart.equals("")) && (dateEnd.equals("")) && (keyword.equals(""))) {
            //페이징된 리스트 가져오기
            list = reviewService.getPageList(page);
        } else {
            if ((!dateStart.equals("")) && (dateEnd.equals(""))) {//dateStart 값만 있을때
                list = reviewService.findByDate(dateStart, page);
            } else if ((!dateStart.equals("")) && (!dateEnd.equals(""))) {//
                list = reviewService.findByDate(dateStart, dateEnd, page);
            } else {
                list = reviewService.findByKeyword(findBy, keyword, page);
            }
        }

        totalPage = list.getTotalPages();
        pageList = reviewService.getPageList(totalPage, page);

        List<String> itemList = new ArrayList<>();
        for (ReviewResponseDto dto : list) {
            String itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(Long.parseLong(itemNo));
            itemList.add(itemDto.getItemName());//item이름을 itemList에 넣어줌
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        //검색 상품 개수
        long listCount = reviewRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/review/list";
    }

    //관리자 후기 삭제하기
    @RequestMapping("/admin/review/delete")
    public String delete(@RequestParam("reviewNo") String reviewNo) {
        reviewService.delete(reviewNo);
        return "redirect:/admin/review/list";
    }

    //관리자 후기 수정
    @ResponseBody
    @RequestMapping("admin/review/status/modify")
    public String reviewStatusModify(ReviewSaveResponseDto dto) {
        Long reviewNo = dto.getReviewNo();
        String reviewExpo = dto.getReviewExpo();
        boolean result = reviewService.statusModify(dto.getReviewNo(), dto.getReviewExpo());
        if (!result) {
            return "<script>alert('노출상태 변경 실패');location.href='/admin/review/list/';</script>";
        }
        return "<script>alert('노출상태 변경 완료');location.href='/admin/review/list/';</script>";
    }
}
