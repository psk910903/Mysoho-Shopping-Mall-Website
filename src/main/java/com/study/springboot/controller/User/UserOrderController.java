package com.study.springboot.controller.User;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.cart.CartSaveRequestDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.dto.security.SessionUser;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.repository.MemberRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class UserOrderController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final HttpSession httpSession;

    //장바구니 페이지
    @GetMapping("/order")
    public String order(Model model, HttpServletRequest request, @AuthenticationPrincipal User user) {

        List<ProductResponseDto> itemList = new ArrayList<>();
        List<CartResponseDto> cartList = new ArrayList<>();

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        // 쿠키 정렬하기
        Arrays.sort(cookies, new Comparator<Cookie>() {
            @Override
            public int compare(Cookie c1, Cookie c2) {
                String c1Name = c1.getName();
                String c2Name = c2.getName();

                return c2Name.compareTo(c1Name);
            }
        });


        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기

                if (name.startsWith("item_idx.")) {

                    // 변수 선언
                    Long itemNo = Long.parseLong(name.split("\\.")[1]);
                    Long cartItemAmount = Long.parseLong(value);
                    String itemOptionColor = "";
                    try {
                        itemOptionColor = URLDecoder.decode(name.split("\\.")[2], "UTF-8");
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    String itemOptionSize = name.split("\\.")[3];

                    // itemList
                    ProductResponseDto productResponseDto = productService.findById(itemNo);
                    itemList.add(productResponseDto);

                    // cartList
                    String cartCode = UUID.randomUUID().toString();
                    Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
                    Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) /100 * 100;
                    cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

                    CartResponseDto cartResponseDto = CartResponseDto.builder()
                            .cartCode(cartCode)
                            .itemCode(String.valueOf(itemNo))
                            .itemName(productResponseDto.getItemName())
                            .itemOptionColor(itemOptionColor)
                            .itemOptionSize(itemOptionSize)
                            .cartItemAmount(cartItemAmount)
                            .cartItemOriginalPrice(productResponseDto.getItemPrice())
                            .cartDiscountPrice(cartDiscountPrice)
                            .cartItemPrice(cartItemPrice)
                            .build();
                    cartList.add(cartResponseDto);

                }
            }
        }

        // member
        MemberResponseDto memberResponseDto = null;
        String memberId = "";
        if(user != null){//시큐리티 가입한 회원이면
            memberId = user.getUsername();
        }else {//sns가입한 회원이면
            try{
                SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
                memberId = memberService.findByMemberEmail(snsUser.getEmail());
            }catch (NullPointerException e){//snsUser가 null일때
                System.out.println("비회원입니다");
            }
        }
        if (memberId != null) {
            memberResponseDto = memberService.findByMemberId(memberId);
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("cartList", cartList);
        model.addAttribute("member", memberResponseDto);

        return "/user/order/shopping-basket";

    }

    //장바구니 상품 삭제
    @PostMapping("/order/deleteAllAction")
    @ResponseBody
    public String orderDeleteAllAction(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();

                if (name.startsWith("item_idx.")) {
                    c.setPath("/");
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }

        return "<script>alert('장바구니가 비었습니다.\\n관심있는 상품을 담아보세요.');location.href='/';</script>";
    }

    //장바구니 상품 삭제
    @PostMapping("/order/deleteAction")
    @ResponseBody
    public String orderDeleteAction(@RequestParam String itemOptionColor, @RequestParam String itemOptionSize,
                                    @RequestParam String itemNo, HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String encodedItemOptionColor = itemOptionColor;
        int cartNum = 0;

        try {
            encodedItemOptionColor = URLEncoder.encode(itemOptionColor, "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "<script>alert('삭제 중 오류가 발생했습니다.\\n다시 입력해주세요.'); location.href='/order';</script>";
        }

        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();
                String value = c.getValue();
                if (name.startsWith("item_idx.")){
                    cartNum++;
                }
                if (name.equals("item_idx."+ itemNo + "." + encodedItemOptionColor + "." + itemOptionSize)) {
                    Cookie cookie = new Cookie(name, value);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        // 장바구니 비었을 떄
        if (cartNum == 1){
            return "<script>alert('장바구니가 비었습니다.\\n관심있는 상품을 담아보세요.');location.href='/';</script>";
        }

        return "<script>location.href='/order';</script>";
    }

    //장바구니 수정
    @PostMapping("/order/modifyAction")
    @ResponseBody
    public String orderModifyAction(@RequestParam String changedSize, @RequestParam String changedColor, @RequestParam String changedAmount,
                                    @RequestParam String originalColor, @RequestParam String originalSize, HttpServletResponse response,
                                    @RequestParam String itemNo, HttpServletRequest request) {

        // 기존에 있던 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        String encodedOriginalColor = originalColor;

        try {
            encodedOriginalColor = URLEncoder.encode(originalColor, "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "<script>alert('수정 중에 오류가 발생했습니다.\\n다시 수정해주세요.');location.href='/order';</script>";
        }

        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();
                String value = c.getValue();

                if (name.equals("item_idx."+ itemNo + "." + encodedOriginalColor + "." + originalSize)) {
                    Cookie cookie = new Cookie(name, value);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        // 쿠키 재생성
        Cookie cookie;
        String encodedChangedColor;

        try {
            encodedChangedColor = URLEncoder.encode(changedColor, "UTF-8");
            cookie = new Cookie("item_idx."+ itemNo + "." + encodedChangedColor + "." + changedSize, changedAmount);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*3);
            response.addCookie(cookie);;

        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('수정 중에 오류가 발생했습니다.\\n다시 수정해주세요.');location.href='/order';</script>";
        }
        return "<script>location.href='/order';</script>";

    }

    //결제
    @PostMapping("/order/payAction")
    @ResponseBody
    public String orderPayAction(OrderContentSaveRequestDto orderContentSaveRequestDto, HttpServletRequest request,
                                 @AuthenticationPrincipal User user, HttpServletResponse response,
                                 @RequestParam String[] colorList, @RequestParam String[] sizeList,
                                 @RequestParam String[] amountList, @RequestParam String[] itemCodeList,
                                 @Valid MemberJoinDto memberJoinDto, BindingResult bindingResult) {

        ////////////////////////////////////// member DB에 넣기 (회원가입) /////////////////////////////////
        String memberId = null;

        if (memberJoinDto.getUsername() != "") {//empty가 아니면(내용이 있으면) 회원가입

            LocalDate today = LocalDate.now();
            memberJoinDto.setMemberJoinDatetime(today);

            if (bindingResult.hasErrors()) {
                String detail = bindingResult.getFieldError().getDefaultMessage();
                return "<script>alert('" + detail + "'); history.back();</script>";
            }

            String encodedPassword = passwordEncoder.encode(memberJoinDto.getPassword());
            memberJoinDto.setPassword(encodedPassword);
            try {
                MemberEntity enity = memberJoinDto.toSaveEntity();
                memberRepository.save(enity);
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
                return "<script>alert('사용중인 아이디나 이메일입니다.');history.back();</script>";
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "<script>alert('회원가입 실패했습니다.');history.back();</script>";
            }

            memberId = memberJoinDto.getUsername();
        }

//        if (user != null) {
//            memberId = user.getUsername();
//        }

        if(user != null){
            memberId = user.getUsername();
        }else {
            try{
                SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
                memberId = memberService.findByMemberEmail(snsUser.getEmail());
            }catch (NullPointerException e){//snsUser가 null일때
                System.out.println("비회원입니다");
            }
        }

        ////////////////////////////////////// cart DB에 넣기 ////////////////////////////////////////////
        String[] cartCodeList = {null, null, null, null, null};
        Long orderCode = null;

        // orderNo
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderCode1 = format.format(new Date());
        String orderCode2 = String.format("%04d", (long) (Math.random() * 10000));
        orderCode = Long.parseLong(orderCode1 + orderCode2);

        for (int i=0; i<itemCodeList.length; i++) {

            // cartItemAmount
            Long cartItemAmount = Long.parseLong(amountList[i]);

            // itemOptionColor
            String itemOptionColor = colorList[i];

            // itemOptionSize
            String itemOptionSize = sizeList[i];

            // itemCode
            String itemCode = itemCodeList[i];
            ProductResponseDto productResponseDto = productService.findById(Long.parseLong(itemCode));

            // cartCode
            String cartCode = UUID.randomUUID().toString();
            cartCodeList[i] = cartCode;



            // cartDiscountPrice, cartItemPrice
            Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
            Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) / 100 * 100;
            cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

            CartSaveRequestDto cartSaveRequestDto = CartSaveRequestDto.builder()
                    .cartCode(cartCode)
                    .orderCode(orderCode)
                    .memberId(memberId)
                    .itemCode(itemCode)
                    .itemName(productResponseDto.getItemName())
                    .itemOptionColor(itemOptionColor)
                    .itemOptionSize(itemOptionSize)
                    .cartItemAmount(cartItemAmount)
                    .cartItemOriginalPrice(productResponseDto.getItemPrice())
                    .cartDiscountPrice(cartDiscountPrice)
                    .cartItemPrice(cartItemPrice)
                    .cartDate(LocalDateTime.now())
                    .build();

            // DB에 넣기
            boolean success = cartService.saveCart(cartSaveRequestDto);
            if (success) {
                try {
                    // 기존에 있던 쿠키 삭제하기
                    String encodedColor = URLEncoder.encode(itemOptionColor, "UTF-8");
                    Cookie cookie = new Cookie("item_idx." + itemCode +"." + encodedColor + "." + itemOptionSize, String.valueOf(cartItemAmount));
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }catch (Exception e){
                    e.printStackTrace();
                    return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
                }
            } else {
                return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
            }

        }



        ////////////////////////////////////// order DB에 넣기 ////////////////////////////////////////////

        // orderDTO 세팅하기
        orderContentSaveRequestDto.setOrderCode(orderCode);
        orderContentSaveRequestDto.setCartCode1(cartCodeList[0]);
        orderContentSaveRequestDto.setCartCode2(cartCodeList[1]);
        orderContentSaveRequestDto.setCartCode3(cartCodeList[2]);
        orderContentSaveRequestDto.setCartCode4(cartCodeList[3]);
        orderContentSaveRequestDto.setCartCode5(cartCodeList[4]);
        orderContentSaveRequestDto.setOrderDatetime(LocalDateTime.now());
        orderContentSaveRequestDto.setMemberId(memberId);
        orderContentSaveRequestDto.setMemberMileage(0L);
        orderContentSaveRequestDto.setMemberCoupon("0");
        if (orderContentSaveRequestDto.getOrderPayType().contains("휴대폰결제") ||
                orderContentSaveRequestDto.getOrderPayType().contains("삼성페이")) {
            orderContentSaveRequestDto.setOrderState("배송대기");
        }else{
            orderContentSaveRequestDto.setOrderState("결제대기");
        }

        boolean success = orderService.saveOrderDto(orderContentSaveRequestDto);
        if (!success) {
            return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
        }

        return "<script>location.href='/order/complete?orderCode=" + orderCode +"';</script>";
    }

    //주문완료페이지
    @GetMapping("/order/complete")
    public String orderComplete(@RequestParam Long orderCode, Model model){

        OrderResponseDto dto = orderService.findByOrderCode(orderCode);
        model.addAttribute("order", dto);

        return "/user/order/order-complete";
    }
}
