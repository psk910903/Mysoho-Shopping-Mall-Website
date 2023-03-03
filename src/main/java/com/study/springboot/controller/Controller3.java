package com.study.springboot.controller;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.repository.CartRepository;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.ReviewService;
import com.study.springboot.service.Service3;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Controller
public class Controller3 {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final MemberRepository memberRepository;
    final private PasswordEncoder passwordEncoder;
    final private Service3 service3;
    final private OrderRepository orderRepository;
    final private CartRepository cartRepository;



    @RequestMapping("/admin/review")
    public String main() {
        return "redirect:/admin/review/list";
    }

    //list만 사용하는 전체 출력
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
        //출력페이지 5개로 고정
        totalPage = list.getTotalPages();
        pageList = reviewService.getPageList(totalPage, page);

        List<String> itemList = new ArrayList<>();
        for (ReviewResponseDto dto : list) {
            String itemNo = dto.getItemNo();//리뷰의 itemNo으로 product에서 정보를(dto)를 찾아옴
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

    //삭제하기
    @RequestMapping("/admin/review/delete")
    public String delete(@RequestParam("reviewNo") String reviewNo) {
        reviewService.delete(reviewNo);
        return "redirect:/admin/review/list";
    }

    @ResponseBody
    @RequestMapping("admin/review/status/modify")
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

    /////////////////////////////////////////  시큐리티  /////////////////////////////////////////////


    //로그인 폼
    @GetMapping("/user/login")
    public String login() {
        return "user/user/userlogin";
    }

    //가입 폼
    @GetMapping("/user/join")
    public String join() {
        return "user/user/userjoin";
    }

    //가입하기
    @PostMapping("/user/joinAction")
    @ResponseBody
    public String joinAction(@Valid MemberJoinDto dto, BindingResult bindingResult) {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        dto.setMemberJoinDatetime(today);

        if (bindingResult.hasErrors()) {
            String detail = bindingResult.getFieldError().getDefaultMessage();
            String bindResultCode = bindingResult.getFieldError().getCode();
            System.out.println(detail + ":" + bindResultCode);
            return "<script>alert('" + detail + "'); history.back();</script>";
        }
        System.out.println(dto.getUsername());
        System.out.println(dto.getPassword());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        System.out.println("encodedPassword:" + encodedPassword);
        dto.setPassword(encodedPassword);
        try {
            MemberEntity enity = dto.toSaveEntity();
            memberRepository.save(enity);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "<script>alert('사용중인 아이디나 멜 주소 입니다.');history.back();</script>";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "<script>alert('회원가입 실패했습니다.');history.back();</script>";
        }

        HttpStatus status = HttpStatus.OK;
        if (status == HttpStatus.OK) {
            System.out.println("회원가입 성공!");
            return "<script>alert('회원가입 성공!'); location.href='/user/login';</script>";
        } else {
            return "<script>alert('회원가입 실패'); history.back();</script>";
        }
    }

    //탈퇴 회원으로 변경
    @RequestMapping("/user/exited")
    @ResponseBody
    public String exited(@AuthenticationPrincipal User user,
                         HttpServletRequest request) throws Exception {
        String username = user.getUsername();
        System.out.println("탈퇴할 회원 id:" + user.getUsername());
        boolean result = service3.exited(username);
        request.getSession().invalidate();//세션종료
        if (result) {
            return "<script>alert('회원탈퇴 성공했습니다.'); location.href='/';</script>";
        } else {
            return "<script>alert('회원탈퇴 실패했습니다.'); history.back();</script>";
        }
    }

    //비밀 번호 확인 폼
    @RequestMapping("/user/myInfoPswd")
    public String password() {
        return "user/user/user-myInfo-Pswd";
    }

    //비밀 번호 재확인
    @RequestMapping("/user/checkPswd")
    @ResponseBody
    public String checkPswd(@AuthenticationPrincipal User user,
                            @RequestParam("getPassword") String getPassword) {
        MemberEntity entity = service3.findByUserId(user.getUsername());
        String encodePassword = entity.getPassword();//
        if (passwordEncoder.matches(getPassword, encodePassword)) {
            return "<script> alert('비밀번호 확인완료'); location.href='/user/myInfo';</script>";
        } else {
            return "<script> alert('비밀번호 확인실패'); history.back(); </script>";
        }
    }

    //개인 정보 수정 폼
    @RequestMapping("/user/myInfo")
    public String modifyMyInfo(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        MemberEntity member = service3.findByUserId(username);
        model.addAttribute("member", member);
        return "user/user/user-myInfo";
    }

    //개인 정보 수정
    @RequestMapping("user/myInfoModify")
    @ResponseBody
    public String myInfoModify(@RequestParam("getPassword") String getPassword,
                               @RequestParam("password") String newPassword,
                               @AuthenticationPrincipal User user,
                               MemberJoinDto dto) {
        MemberEntity entity = service3.findByUserId(user.getUsername());
        String encodePassword = entity.getPassword();
        if (passwordEncoder.matches(getPassword, encodePassword)) {//암호 같으면
            String encodedPassword = passwordEncoder.encode(newPassword);
            //dto에 수정된 정보를 받았음
            dto.setPassword(encodedPassword);
            boolean result = service3.update(dto);
            if (result) {
                return "<script> alert('회원정보 수정에 성공했습니다.'); location.href='/';</script>";
            } else {
                return "<script> alert('회원정보 수정에 실패했습니다.'); history.back(); </script>";
            }
        } else {
            return "<script> alert('비밀번호가 다릅니다'); history.back(); </script>";
        }
    }

    //마일리지 상세
    @RequestMapping("/user/mileage")
    public String mileage(@AuthenticationPrincipal User user,
                          Model model) {
        if (user == null) {
            System.out.println("no user");
        } else {
            String username = user.getUsername();
            System.out.println("mileage username:" + username);
            MemberEntity entity = service3.findByUserId(username);
            System.out.println("mileage :" + entity.getMemberMileage());
            model.addAttribute("memberMileage", entity.getMemberMileage());
        }
        return "/user/user/user-mileage";
    }

    //쿠폰 상세
    @RequestMapping("/coupons/mylist")
    public String coupons(@AuthenticationPrincipal User user,
                          Model model) {
        if (user == null) {
            System.out.println("no user");
        } else {
            String username = user.getUsername();
            System.out.println("coupons username:" + username);
            MemberEntity entity = service3.findByUserId(username);
            System.out.println("coupons :" + entity.getMemberCoupon());
            model.addAttribute("memberCoupon", entity.getMemberCoupon());
        }
        return "user/user/coupons-mylist";
    }
    //로그인 전 아이디 찾기
    @RequestMapping("/find/id")
    @ResponseBody
    public String findId(@RequestParam("memberName") String memberName,
                         @RequestParam("memberPhone")  String memberPhone
    ){
        System.out.println(memberName);
        System.out.println(memberPhone);

        String id = service3.findByMemberNameAndMemberPhone(memberName,memberPhone);
        if(id==null){
            return "<script>alert('가입된 회원 정보가 없습니다. 이름/휴대폰번호를 확인해주세요.'); history.back();</script>";
        } else {
            return "<script>alert('회원님의 아이디는 "+id+" 입니다.'); location.href='/user/login';</script>";
        }
    }
    //로그인 전 패스워드 재설정 멜 보내기
    @RequestMapping("/find/password")
    @ResponseBody
    public String findPw(@RequestParam("getEmail") String getEmail)  {
        String result = service3.changPassword(getEmail);
        if(result.equals("0")){
            return "<script> alert('멜에서 새로운 비밀번호를 확인하세요.'); history.back(); </script>";
        }else if(result.equals("1")){
            return "<script> alert('메일 주소를 확인해 주세요.'); history.back(); </script>";
        }else {
            return "<script> alert('멜보내기 실패했습니다.'); history.back(); </script>";
        }
    }

    //나의 상품 문의 내역 -> controller2에 있음
    //나의 Q&A->controller2에 있음

    //나의 후기 내역 - 없어서 페이지만 연결
    @RequestMapping("/review/myList")
    public String myReview(@AuthenticationPrincipal User user) {
        String memberId = user.getUsername();
        List<Long> list = orderRepository.findByMemberIdAndOrderState(memberId,"배송완료");
        for (Long l : list){
            System.out.println(l);
            List<CartEntity> list1 = cartRepository.findByOrderCode(l);
        }
        return "user/user/review-mylist";
    }





}//class