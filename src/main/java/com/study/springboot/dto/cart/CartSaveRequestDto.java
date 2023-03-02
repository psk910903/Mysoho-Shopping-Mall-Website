package com.study.springboot.dto.cart;

import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.NoticeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CartSaveRequestDto {

    private Long cartNo; // PK
    private String cartCode; //장바구니 코드(UUID포맷-32자리)
    private Long orderNo; //주문정보 PK
    private String memberId; // 아이디(회원)
    private String sessionId; // 세션아이디(비회원)
    private String itemCode; // 상품코드
    private String itemName; // 상품이름
    private String itemOptionColor; // 색상
    private String itemOptionSize; // 사이즈
    private Long cartItemAmount; // 구매수량
    private Long cartItemOriginalPrice;//(할인 전)상품가격

    private Long cartDiscountPrice;//할인율이 적용된 차감될 금액
    private Long cartItemPrice; // (할인 적용된 결제당시)상품가격
    private LocalDateTime cartDate; // 장바구니 생성일

    @Builder
    public CartSaveRequestDto(Long cartNo, String cartCode, Long orderNo, String memberId, String sessionId, String itemCode, String itemName, String itemOptionColor, String itemOptionSize, Long cartItemAmount, Long cartItemOriginalPrice, Long cartDiscountPrice, Long cartItemPrice, LocalDateTime cartDate) {
        this.cartNo = cartNo;
        this.cartCode = cartCode;
        this.orderNo = orderNo;
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionSize = itemOptionSize;
        this.cartItemAmount = cartItemAmount;
        this.cartItemOriginalPrice = cartItemOriginalPrice;
        this.cartDiscountPrice = cartDiscountPrice;
        this.cartItemPrice = cartItemPrice;
        this.cartDate = cartDate;
    }

    public CartEntity toEntity(){
        return CartEntity.builder()
                .cartCode(cartCode)
                .orderNo(orderNo)
                .memberId(memberId)
                .itemCode(itemCode)
                .itemName(itemName)
                .itemOptionColor(itemOptionColor)
                .itemOptionSize(itemOptionSize)
                .cartItemAmount(cartItemAmount)
                .cartItemOriginalPrice(cartItemOriginalPrice)
                .cartDiscountPrice(cartDiscountPrice)
                .cartItemPrice(cartItemPrice)
                .cartDate(cartDate)
                .build();
    }
}
