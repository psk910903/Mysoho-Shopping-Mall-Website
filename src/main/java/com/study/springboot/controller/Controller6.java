package com.study.springboot.controller;


import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.MemberService;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.Service6;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class Controller6 {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final Service6 service6;

    private final ProductService productService;


    @GetMapping("/admin/member")
    public String home() {
        return "redirect:/admin/member/list?page=0";
    }

    @GetMapping("/admin/member/list")
    public String list(@RequestParam(value = "findByType1", required = false) String findByType1,
                       @RequestParam(value = "findByType2", required = false) String findByType2,
                       @RequestParam(value = "keyword", required = false) String keyword, // keyword: 어떤 keyword로 찾을 것인지 결정
                       @RequestParam(value = "page", defaultValue = "0") int page,        // page: 0에서부터 시작
                       Model model) {                                                    // ex) findBy=title, keyword="키워드입니다", page:2면

        Page<MemberResponseDto> list;
        int totalPage;
        List<Integer> pageList;
// 검색어가 아무것도 입력이 안됐을때
        if ((findByType1 == null) && (findByType2 == null) && (keyword == null)
                || (findByType1.equals("null")) && (findByType2.equals("null")) && (keyword.equals("null"))
                || (findByType1.equals("all")) && (findByType2.equals("all")) && (keyword.equals(""))) {

            list = memberService.findAll(page);

        } else {
            list = memberService.findByKeyword(findByType1, findByType2, keyword, page);
        }
        totalPage = list.getTotalPages();
        pageList = memberService.getPageList(totalPage, page);
        model.addAttribute("list", list);
        model.addAttribute("findByType1", findByType1);
        model.addAttribute("findByType2", findByType2);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("totalPage", totalPage);

        //검색 회원 명수
        long listCount = memberRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/member/list"; //listForm.html로 응답
    }

    @GetMapping("/admin/member/content/{id}")
//                            @파람detail?id=
    public String content(@PathVariable("id") long id, Model model) {
        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "/admin/member/content";
    }



    @PostMapping("/admin/member/content/modify")  //수정
    @ResponseBody
    public String contentModify(MemberSaveRequestDto dto) {


        boolean result = memberService.modify(dto);

        if (result) {
            return "<script>alert('회원정보수정 완료'); location.href='/admin/member/list';</script>";
        } else {
            return "<script>alert('회원정보수정 실패'); history.back();</script>";
        }
    }

    @RequestMapping("/admin/member/delete/check")
    public String deleteCheck(@RequestParam("memberNo") String memberNo) {
        memberService.deleteCheck(memberNo);
        return "redirect:/admin/member/list";
    }


    //  유저-------------------------------------------------------------------------------------------------------
    // 마이페이지
    @GetMapping("user/user-myInfo")
    public String myInfo() {
        return "/user/user/user-myInfo";
    }
    @GetMapping("user/user/user-myInfo/{id}")
    public String ContentMyInfo(@PathVariable("id")long id, Model model) {
        MemberResponseDto memberResponseDto = memberService.findById(id);
        model.addAttribute("member", memberResponseDto);
        return "user/user/user-myInfo";
    }


    @PostMapping("user/user/user-myInfo/modifyUserContent")
    @ResponseBody
    public String modifyUserContent( MemberSaveRequestDto dto){
        boolean pwCheck = service6.pwCheck(dto);

        if (pwCheck) {
            boolean result = memberService.modify(dto);
            if (result) {
                return "<script>alert('정보수정 완료'); location.href='/user/user/user-myInfo/"+ dto.getMemberNo() +"';</script>";
            } else {
                return "<script>alert('정보수정 실패'); history.back();</script>";
            }
        } else {
            return "<script>alert('비밀번호가 다릅니다.'); history.back();</script>";
        }
    }
//    return "<script>alert('정보수정 완료'); location.href='user/user/user-myInfo';</script>";
//} else {
//        return "<script>alert('정보수정 실패'); history.back();</script>";
//        }

    @GetMapping("popup/pop-page4")
    public String popPage4 () {
        return "user/popup/pop-page4";
    }


    @GetMapping("/product/review/{id}") // 상품상세페이지로 수정해야함
    public String Review (@PathVariable("id") String id, Model model){
        List<ReviewResponseDto> dto = service6.findByReview(id);
        model.addAttribute("list", dto);
        return "/user/user/productReview";
    }

//    상품상세 리뷰페이지
@GetMapping("/product/testKB/{itemNo}")
public String productContent(Model model,@PathVariable(value = "itemNo") Long itemNo) {
    ProductResponseDto dto = productService.findById(itemNo); // 1. 페이지및 페이지해당 데이터

    List<ReviewResponseDto> reviewResponseDtos = service6.findByReview(String.valueOf(itemNo)); // 2. 리뷰데이터

    int size = reviewResponseDtos.size();  // 3. 상품 리뷰 갯수

    byte sum = 0;       // 4. 상품별점 평균
    for(int i=0; i<size; i++){
        byte reviewStar = reviewResponseDtos.get(i).getReviewStar();
        sum += reviewStar;
    }
    double avg1 = sum / Double.valueOf(size);
    double avg2 = Math.round(avg1*10);
    double avg3 = avg2 / 10;

    List<ReviewResponseDto> reviewResponseDtos2 = service6.findByImgReview(String.valueOf(itemNo)); //5. 사진리뷰 데이터
    int size2 = reviewResponseDtos2.size(); //6. 사진리뷰 갯수

    model.addAttribute("dto", dto); // ->1
    model.addAttribute("list", reviewResponseDtos); // ->2
    model.addAttribute("listCount", size);  // ->3
    model.addAttribute("avgStar", avg3);    // ->4
    model.addAttribute("listImg",reviewResponseDtos2);  // ->5
    model.addAttribute("listImgCount", size2);  // ->6
    return "/user/product/contentTestKyeongBin";
}

}

