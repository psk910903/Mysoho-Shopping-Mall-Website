package com.study.springboot.dto.cart;

import com.study.springboot.entity.CartEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CartResponseDto {

    private Long cartNo; // PK
    private String memberId; // 아이디(회원)
    private String sessionId; // 세션아이디(비회원)
    private String itemCode; // 상품코드
    private String itemName; // 상품이름
    private String itemOptionColor; // 색상
    private String itemOptionSize; // 사이즈
    private Long cartItemAmount; // 구매수량
    private LocalDateTime cartDate; // 장바구니 생성일

    public CartResponseDto(CartEntity entity) {
        this.cartNo = entity.getCartNo();
        this.memberId = entity.getMemberId();
        this.sessionId = entity.getSessionId();
        this.itemCode = entity.getItemCode();
        this.itemName = entity.getItemName();
        this.itemOptionColor = entity.getItemOptionColor();
        this.itemOptionSize = entity.getItemOptionSize();
        this.cartItemAmount = entity.getCartItemAmount();
        this.cartDate = entity.getCartDate();
    }
}
