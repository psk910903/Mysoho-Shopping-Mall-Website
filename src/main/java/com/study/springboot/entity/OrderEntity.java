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
@Table(name = "`order`")
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no", nullable = false)
    private Long orderNo; //pk
    @Column(name = "order_code", nullable = false)
    private Long orderCode; //주문정보 코드
    @Column(name = "cart_code_1", nullable = false)
    private String cartCode1; //장바구니 코드
    @Column(name = "cart_code_2")
    private String cartCode2;
    @Column(name = "cart_code_3")
    private String cartCode3;
    @Column(name = "cart_code_4")
    private String cartCode4;
    @Column(name = "cart_code_5")
    private String cartCode5;
    @Column(name = "order_total_price", nullable = false)
    private Long orderTotalPrice; //주문 총금액
    @Column(name = "order_total_count", nullable = false)
    private Long orderTotalCount; //주문 상품 개수
    @Column(name = "order_name", nullable = false)
    private String orderName; // 주문자 이름
    @Column(name = "order_phone", nullable = false)
    private String orderPhone; // 주문자 연락처
    @Column(name = "order_recipient_name", nullable = false)
    private String orderRecipientName; // 수령자 이름
    @Column(name = "order_recipient_phone", nullable = false)
    private String orderRecipientPhone; // 수령자 연락처
    @Column(name = "order_recipient_addr_number", nullable = false)
    private String orderRecipientAddrNumber; // 수령자 우편번호
    @Column(name = "order_recipient_addr_1", nullable = false)
    private String orderRecipientAddr1; // 수령자 기본주소
    @Column(name = "order_recipient_addr_2", nullable = false)
    private String orderRecipientAddr2; // 수령자 나머지주소
    @Column(name = "member_id")
    private String memberId; // 회원 아이디
    @Column(name = "member_mileage")
    private Long memberMileage; // 적립금 사용
    @Column(name = "member_coupon")
    private String memberCoupon; // 쿠폰 사용
    @Column(name = "order_pay_type", nullable = false)
    private String orderPayType; // 휴대폰결제 or 무통장입금 선택
    @Column(name = "order_state", nullable = false)
    private String orderState; // 주문상태
    @Column(name = "order_datetime")
    private LocalDateTime orderDatetime = LocalDateTime.now(); // 결제시간

    @Builder
    public OrderEntity(Long orderNo, Long orderCode, String cartCode1, String cartCode2, String cartCode3, String cartCode4, String cartCode5, Long orderTotalPrice, Long orderTotalCount, String orderName, String orderPhone, String orderRecipientName, String orderRecipientPhone, String orderRecipientAddrNumber, String orderRecipientAddr1, String orderRecipientAddr2, String memberId, Long memberMileage, String memberCoupon, String orderPayType, String orderState, LocalDateTime orderDatetime) {
        this.orderNo = orderNo;
        this.orderCode = orderCode;
        this.cartCode1 = cartCode1;
        this.cartCode2 = cartCode2;
        this.cartCode3 = cartCode3;
        this.cartCode4 = cartCode4;
        this.cartCode5 = cartCode5;
        this.orderTotalPrice = orderTotalPrice;
        this.orderTotalCount = orderTotalCount;
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderRecipientName = orderRecipientName;
        this.orderRecipientPhone = orderRecipientPhone;
        this.orderRecipientAddrNumber = orderRecipientAddrNumber;
        this.orderRecipientAddr1 = orderRecipientAddr1;
        this.orderRecipientAddr2 = orderRecipientAddr2;
        this.memberId = memberId;
        this.memberMileage = memberMileage;
        this.memberCoupon = memberCoupon;
        this.orderPayType = orderPayType;
        this.orderState = orderState;
        this.orderDatetime = orderDatetime;
    }
}
