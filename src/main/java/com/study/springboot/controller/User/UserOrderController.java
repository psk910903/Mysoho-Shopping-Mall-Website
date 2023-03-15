package com.study.springboot.controller.User;

import com.study.springboot.comparator.CookiesComparator;
import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.cart.CartSaveRequestDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
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
import javax.validation.Valid;
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

    //장바구니 페이지
    @GetMapping("/order")
    public String order(Model model, HttpServletRequest request, @AuthenticationPrincipal User user) {

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        Arrays.sort(cookies, new CookiesComparator());// 쿠키 정렬하기


        List<ProductResponseDto> itemList = new ArrayList<>();
        List<CartResponseDto> cartList = new ArrayList<>();
        if(cookies!=null){
            itemList = productService.itemListByCookies(cookies);
            cartList = productService.cartListByCookies(cookies);
        }

        MemberResponseDto memberResponseDto = null;
        if (user != null) {
            memberResponseDto = memberService.findByMemberId(user.getUsername());
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
    public String orderPayAction(OrderContentSaveRequestDto orderSaveRequestDto, HttpServletRequest request,
                                 @AuthenticationPrincipal User user, HttpServletResponse response,
                                 @RequestParam String[] colorList, @RequestParam String[] sizeList,
                                 @RequestParam String[] amountList, @RequestParam String[] itemCodeList,
                                 @Valid MemberJoinDto memberJoinDto, BindingResult bindingResult) {

        ////////////////////////////////////// member DB에 넣기 (회원가입) /////////////////////////////////
        String memberId = null;

        if (memberJoinDto.getUsername() != "") {

            memberJoinDto.setMemberJoinDatetime(LocalDate.now());

            if (bindingResult.hasErrors()) {
                String detail = bindingResult.getFieldError().getDefaultMessage();
                return "<script>alert('" + detail + "'); history.back();</script>";
            }

            memberJoinDto.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
            try {
                memberRepository.save(memberJoinDto.toSaveEntity());
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

        if (user != null) {
            memberId = user.getUsername();
        }

        ////////////////////////////////////// cart DB에 넣기 ////////////////////////////////////////////
        String[] cartCodeList = {null, null, null, null, null};
        Long orderCode = orderService.generateOrderNo();

        for (int i=0; i<itemCodeList.length; i++) {


            Long cartItemAmount = Long.parseLong(amountList[i]); // cartItemAmount
            String itemOptionColor = colorList[i]; // itemOptionColor
            String itemOptionSize = sizeList[i]; // itemOptionSize
            String itemCode = itemCodeList[i]; // itemCode
            String cartCode = UUID.randomUUID().toString(); // cartCode
            cartCodeList[i] = cartCode;
            CartSaveRequestDto cartSaveRequestDto = cartService.saveCartDto(cartCode, orderCode,
                    itemCode,cartItemAmount, itemOptionColor, itemOptionSize, memberId);

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
        OrderContentSaveRequestDto orderDto = orderService.saveOrderDtoComplete(orderSaveRequestDto, orderCode, cartCodeList, memberId);
        boolean success = orderService.saveOrderDto(orderDto);
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
