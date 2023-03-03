package com.study.springboot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`cart`")
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_no", nullable = false)
    private Long cartNo; //pk
    @Column(name = "cart_code")
    private String cartCode; //장바구니 코드(UUID포맷-32자리)
    @Column(name = "order_code")
    private Long orderCode; //주문정보 코드
    @Column(name = "member_id")
    private String memberId; //아이디(회원)
    @Column(name = "session_id")
    private String sessionId;//세션아이디(비회원)
    @Column(name = "item_code")
    private String itemCode;//상품코드
    @Column(name = "item_name")
    private String itemName;//상품이름
    @Column(name = "item_option_color")
    private String itemOptionColor;//색상
    @Column(name = "item_option_size")
    private String itemOptionSize;//사이즈
    @Column(name = "cart_item_amount")
    private Long cartItemAmount;//수량
    @Column(name = "cart_item_original_price")
    private Long cartItemOriginalPrice;//(할인 전)상품가격
    @Column(name = "cart_discount_price")
    private Long cartDiscountPrice;//할인율이 적용된 차감될 금액
    @Column(name = "cart_item_price")
    private Long cartItemPrice;//(할인 적용된 결제당시)상품가격
    @Column(name = "cart_date")
    private LocalDateTime cartDate;//장바구니 생성시간

    @Builder
    public CartEntity(Long cartNo, String cartCode, Long orderCode, String memberId, String sessionId, String itemCode, String itemName,
                      String itemOptionColor, String itemOptionSize, Long cartItemAmount, Long cartItemOriginalPrice, Long cartDiscountPrice,
                      Long cartItemPrice, LocalDateTime cartDate) {
        this.cartNo = cartNo;
        this.cartCode = cartCode;
        this.orderCode = orderCode;
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
}