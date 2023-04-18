package com.study.springboot.controller.User;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.dto.security.SessionUser;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.repository.MemberRepository;
import com.study.springboot.service.CartService;
import com.study.springboot.service.MemberService;
import com.study.springboot.service.OrderService;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserInfoController {

    private final OrderService orderService;
    private final CartService cartService;
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final HttpSession httpSession;

    // 주문조회-----------------------------------------------------------------------------
    @GetMapping("/myorder")
    public String myorderList() {
        return "user/user/myorder";
    }

    //비회원
    @RequestMapping("/order/myorder/list")
    public String myOrder(OrderSearchDto dto, Model model) {

        List<OrderResponseDto> orderList = orderService.findByOrderNonMember(dto);
        List<CartResponseDto> cartList;
        List<List<CartResponseDto>> cartListModel = new ArrayList<>();
        int[] stateType = orderService.orderStateType(orderList);

        for (OrderResponseDto orderDto : orderList) {

            //비회원 주문번호에서 카트정보 가져오기
            cartList = cartService.getCartList(orderDto, "nonMember");
            cartListModel.add(cartList);
            long[] priceSetting = cartService.priceSetting(cartList, orderDto);
            orderDto.setOrderItemOriginalPrice(priceSetting[0]); //(할인 전)상품가격
            orderDto.setOrderDiscountPrice(priceSetting[1]);//할인율이 적용된 차감될 금액
            orderDto.setOrderItemPrice(priceSetting[2]); // (할인 적용된 결제당시)상품가격
        }

        model.addAttribute("stateType1", stateType[0]);
        model.addAttribute("stateType2", stateType[1]);
        model.addAttribute("stateType3", stateType[2]);
        model.addAttribute("stateType4", stateType[3]);
        model.addAttribute("stateType5", stateType[4]);
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("cartCount", cartListModel.size());
        model.addAttribute("orderList", orderList);
        model.addAttribute("cartListModel", cartListModel);

        return "user/user/myorder-list";
    }

    //마이페이지 홈 회원
    @RequestMapping("/myorder/lists")
    public String myInfo(@AuthenticationPrincipal User user,
                         HttpServletRequest request, Model model) {

        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        MemberEntity entity = memberService.findByUserId(memberId);
        request.getSession().setAttribute("username", entity.getMemberName());
        request.getSession().setAttribute("memberMileage", entity.getMemberMileage());
        request.getSession().setAttribute("memberCoupon", entity.getMemberCoupon());

        List<OrderResponseDto> orderList = cartService.findByOrderList(memberId);
        List<CartResponseDto> cartList;
        List<List<CartResponseDto>> cartListModel = new ArrayList<>();
        List<ReviewResponseDto> ReviewList = reviewService.findByMemberId(memberId); //사용자가 작성한 리뷰
        int[] stateType = orderService.orderStateType(orderList);

        for (OrderResponseDto orderDto : orderList) {

            //회원 주문번호에서 카트정보 가져오기
            cartList = cartService.getCartList(orderDto,"member");
            cartListModel.add(cartList);
            long[] priceSetting = cartService.priceSetting(cartList, orderDto);
            orderDto.setOrderItemOriginalPrice(priceSetting[0]); //(할인 전)상품가격
            orderDto.setOrderDiscountPrice(priceSetting[1]);//할인율이 적용된 차감될 금액
            orderDto.setOrderItemPrice(priceSetting[2]); // (할인 적용된 결제당시)상품가격
        }

        model.addAttribute("ReviewList", ReviewList);
        model.addAttribute("stateType1", stateType[0]);
        model.addAttribute("stateType2", stateType[1]);
        model.addAttribute("stateType3", stateType[2]);
        model.addAttribute("stateType4", stateType[3]);
        model.addAttribute("stateType5", stateType[4]);
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("cartCount", cartListModel.size());
        model.addAttribute("orderList", orderList);
        model.addAttribute("cartListModel", cartListModel);

        return "user/user/myorder-list-user";
    }

    //탈퇴 하기
    @RequestMapping("/user/exited")
    @ResponseBody
    public String exited(@AuthenticationPrincipal User user,
                         HttpServletRequest request) throws Exception {
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        boolean result = memberService.exited(memberId);
        request.getSession().invalidate();//세션종료
        if (result) {
            return "<script>alert('회원탈퇴 성공했습니다.'); location.href='/';</script>";
        } else {
            return "<script>alert('회원탈퇴 실패했습니다.'); history.back();</script>";
        }
    }
    //로그인 전 아이디 찾기
    @RequestMapping("/find/id")
    @ResponseBody
    public String findId(@RequestParam("memberName") String memberName,
                         @RequestParam("memberPhone")  String memberPhone
    ){
        String id = memberService.findByMemberNameAndMemberPhone(memberName,memberPhone);
        if(id==null){
            return "<script>alert('가입된 회원 정보가 없습니다. 이름/휴대폰번호를 확인해주세요.'); history.back();</script>";
        } else {
            return "<script>alert('회원님의 아이디는 "+id+" 입니다.'); location.href='/user/login';</script>";
        }
    }

    //가입하기
    @PostMapping("/user/joinAction")
    @ResponseBody
    public String joinAction(@Valid MemberJoinDto dto, BindingResult bindingResult) {
        LocalDate today = LocalDate.now();
        dto.setMemberJoinDatetime(today);

        if (bindingResult.hasErrors()) {
            String detail = bindingResult.getFieldError().getDefaultMessage();
            return "<script>alert('" + detail + "'); history.back();</script>";
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        try {
            MemberEntity enity = dto.toSaveEntity();
            memberRepository.save(enity);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            if(e.getRootCause().toString().contains("member_id")){
                return "<script>alert('이미 등록된 아이디입니다. 다른 아이디를 사용해 주세요.');history.back();</script>";
            }else if(e.getRootCause().toString().contains("member_email")){
                return "<script>alert('이미 등록된 이메일 주소입니다. 다른 이메일 주소를 사용해 주세요.');history.back();</script>";
            }else if(e.getRootCause().toString().contains("member_phone")){
                return "<script>alert('이미 등록된 전화번호입니다. 다른 전화번호를 사용해 주세요.');history.back();</script>";
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "<script>alert('회원가입 실패했습니다.');history.back();</script>";
        }

        HttpStatus status = HttpStatus.OK;
        if (status == HttpStatus.OK) {
            return "<script>alert('회원가입을 축하합니다.'); location.href='/user/login';</script>";
        } else {
            return "<script>alert('회원가입 실패했습니다.'); history.back();</script>";
        }
    }

    //로그인 전 비밀번호 변경url이 있는 메일 보내기
    @RequestMapping("/find/password")
    @ResponseBody
    public String findPw(@RequestParam("getEmail") String getEmail)  {
        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(getEmail);
        if (optional.isEmpty()){
            return "<script> alert('회원 정보가 없습니다.'); history.back(); </script>";
        }else {
            boolean result = memberService.sendEmail(getEmail);
            if(result){
                return "<script> alert('해당 이메일로 비밀번호 변경 링크를 발송하였습니다.\\n 이메일을 확인해주세요.'); history.back(); </script>";
            } else {
                return "<script> alert('해당 이메일로 비밀번호 변경 링크를 발송에 실패했습니다'); history.back(); </script>";
            }
        }

    }

    //비밀번호 변경 폼
    @RequestMapping("/find/password2")
    public String emailPassword(@RequestParam("getEmail") String getEmail, Model model){
        model.addAttribute("getEmail",getEmail);
        return "user/user/userPassTest";
    }

    //비밀번호 변경
    @RequestMapping("/find/passwordAction")
    @ResponseBody
    public String changPassword(@RequestParam("getEmail")String getEmail,
                                @RequestParam("password")String password){
        boolean result = memberService.changePassword(getEmail,password);
        if(result){
            return "<script> alert('비밀번호 변경 성공');location.href='/user/login'</script>";
        }else {
            return "<script> alert('비밀번호 변경실패'); history.back(); </script>";
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
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
            MemberEntity entity = memberService.findByUserId(memberId);
            String encodePassword = entity.getPassword();
            if (passwordEncoder.matches(getPassword, encodePassword)) {
                return "<script> alert('비밀번호 확인완료'); location.href='/user/myInfo';</script>";
            } else {
                return "<script> alert('비밀번호 확인실패'); history.back(); </script>";
            }
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
            if(memberId.isEmpty()){
                return "<script> alert('비밀번호 확인실패'); history.back(); </script>";
            }else {
                return "<script> alert('비밀번호 확인완료'); location.href='/user/myInfo';</script>";
            }
        }
    }

    //개인 정보 수정 폼
    @RequestMapping("/user/myInfo")
    public String modifyMyInfo(@AuthenticationPrincipal User user, Model model) {
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        MemberEntity member = memberService.findByUserId(memberId);
        model.addAttribute("member", member);
        return "user/user/user-myInfo";
    }

    //개인 정보 수정
    @RequestMapping("user/myInfoModify")
    @ResponseBody
    public String myInfoModify(@AuthenticationPrincipal User user,
                               MemberJoinDto dto) {
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        MemberEntity entity = memberService.findByUserId(memberId);
        String entityPassword = entity.getPassword();
        if(dto.getPassword() == ""){
            dto.setPassword(entityPassword);
        }else{
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        boolean result = memberService.update(dto);
        if (result) {
            return "<script> alert('회원정보 수정에 성공했습니다.'); location.href='/';</script>";
        } else {
            return "<script> alert('회원정보 수정에 실패했습니다.'); history.back(); </script>";
        }
    }

    //마일리지 상세
    @RequestMapping("/user/mileage")
    public String mileage(@AuthenticationPrincipal User user,
                          Model model) {
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        MemberEntity entity = memberService.findByUserId(memberId);
        model.addAttribute("memberMileage", entity.getMemberMileage());

        return "user/user/user-mileage";
    }

    //쿠폰 상세
    @RequestMapping("/coupons/mylist")
    public String coupons(@AuthenticationPrincipal User user,
                           Model model) {
        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        MemberEntity entity = memberService.findByUserId(memberId);
        model.addAttribute("memberCoupon", entity.getMemberCoupon());


        return "user/user/coupons-mylist";
    }

    //sns 로그인
    @RequestMapping("/snsLoginSuccess")
    @ResponseBody
    public String googleLoginSuccess(HttpServletRequest request,Model model){
        SessionUser user = (SessionUser)httpSession.getAttribute("user");
        String userName = "";
        String userEmail = "";
        if( user != null ){
            userName = user.getName();
            userEmail = user.getEmail();
        }
        System.out.println("snsLoginSuccess userName:"+userName);
        System.out.println("snsLoginSuccess userEmail:"+userEmail);

        Optional<MemberEntity> optional =  memberRepository.findByMemberEmail(userEmail);
        if (optional.isEmpty()){
            request.getSession().setAttribute("userName",userName);
            request.getSession().setAttribute("userEmail",userEmail);
            return "<script> location.href='/snsJoin';</script>";
        }else {
            System.out.println(optional.get().getUsername());
            System.out.println(optional.get().getMemberEmail());
            return "<script>alert('"+ userName +"님 로그인 성공 했습니다.'); location.href='/';</script>";
        }
    }
    //sns 회원가입 폼
    @RequestMapping("/snsJoin")
    public String snsJoin(
            HttpServletRequest request,
            Model model
    ){
        return "user/user/user-sns-join";
    }
    //sns 회원가입 등록하기
    @RequestMapping("/snsLoginAction")
    @ResponseBody
    public String snsLoginAction(MemberSaveRequestDto dto){
        try {
            LocalDate today = LocalDate.now();
            dto.setMemberJoinDatetime(today);
            memberRepository.save( dto.toEntity() );
            return "<script> alert('회원등록에 성공했습니다.'); location.href='/';</script>";
        }catch (Exception e){
            e.printStackTrace();
            return "<script> alert('회원등록에 실패했습니다.'); history.back();</script>";
        }
    }

    //sns로그인 실패시
    @RequestMapping("/snsLoginFailure")
    @ResponseBody
    public String googleLoginFailure(){
        return "<script>alert('sns로그인 실패했습니다.'); history.back();</script>";
    }

}
