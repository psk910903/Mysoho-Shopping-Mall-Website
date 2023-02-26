package com.study.springboot.dto.cart;

import com.study.springboot.entity.CartEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CartResponseDto {

    private Long cartNo; // PK
    private Long orderNo; // 주문번호
    private String memberId; // 아이디(회원)
    private String sessionId; // 세션아이디(비회원)
    private String itemCode; // 상품코드
    private String itemName; // 상품이름
    private String itemOptionColor; // 색상
    private String itemOptionSize; // 사이즈
    private Long cartItemAmount; // 구매수량
    private Long cartItemPrice; // (할인 적용된 결제당시)상품가격
    private LocalDateTime cartDate; // 장바구니 생성일

    private String itemImageUrl; //상품이미지

    public CartResponseDto(CartEntity entity) {
        this.cartNo = entity.getCartNo();
        this.memberId = entity.getMemberId();
        this.sessionId = entity.getSessionId();
        this.itemCode = entity.getItemCode();
        this.itemName = entity.getItemName();
        this.itemOptionColor = entity.getItemOptionColor();
        this.itemOptionSize = entity.getItemOptionSize();
        this.cartItemAmount = entity.getCartItemAmount();
        this.cartItemPrice = entity.getCartItemPrice();
        this.cartDate = entity.getCartDate();
    }
}
