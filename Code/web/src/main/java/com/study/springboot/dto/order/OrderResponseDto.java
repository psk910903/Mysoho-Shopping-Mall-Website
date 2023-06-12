package com.study.springboot.dto.order;

import com.study.springboot.entity.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderNo; //pk
    private Long orderCode; // 주문 코드
    private String cartCode1; //장바구니 코드
    private String cartCode2; //장바구니 코드
    private String cartCode3; //장바구니 코드
    private String cartCode4; //장바구니 코드
    private String cartCode5; //장바구니 코드
    private Long orderTotalPrice; //주문 총금액
    private Long orderTotalCount; //주문 상품 개수
    private String orderName; // 주문자 이름
    private String orderPhone; // 주문자 연락처
    private String orderRecipientName; // 수령자 이름
    private String orderRecipientPhone; // 수령자 연락처
    private String orderRecipientAddrNumber; // 수령자 우편번호
    private String orderRecipientAddr1; // 수령자 기본주소
    private String orderRecipientAddr2; // 수령자 나머지주소
    private String memberId; // 회원 아이디
    private Long memberMileage; // 적립금 사용
    private String memberCoupon; // 쿠폰 사용
    private String orderPayType; // 휴대폰결제 or 무통장입금 선택
    private String orderState; // 주문상태
    private LocalDateTime orderDatetime;

    private Long orderItemOriginalPrice;//(할인 전)상품가격
    private Long orderDiscountPrice;//할인율이 적용된 차감될 금액
    private Long orderItemPrice; // (할인 적용된 결제당시)상품가격

    public OrderResponseDto(OrderEntity entity) {
        this.orderNo = entity.getOrderNo();
        this.orderCode = entity.getOrderCode();
        this.cartCode1 = entity.getCartCode1();
        this.cartCode2 = entity.getCartCode2();
        this.cartCode3 = entity.getCartCode3();
        this.cartCode4 = entity.getCartCode4();
        this.cartCode5 = entity.getCartCode5();
        this.orderTotalPrice = entity.getOrderTotalPrice();
        this.orderTotalCount = entity.getOrderTotalCount();
        this.orderName = entity.getOrderName();
        this.orderPhone = entity.getOrderPhone();
        this.orderRecipientName = entity.getOrderRecipientName();
        this.orderRecipientPhone = entity.getOrderRecipientPhone();
        this.orderRecipientAddrNumber = entity.getOrderRecipientAddrNumber();
        this.orderRecipientAddr1 = entity.getOrderRecipientAddr1();
        this.orderRecipientAddr2 = entity.getOrderRecipientAddr2();
        this.memberId = entity.getMemberId();
        this.memberMileage = entity.getMemberMileage();
        this.memberCoupon = entity.getMemberCoupon();
        this.orderPayType = entity.getOrderPayType();
        this.orderState = entity.getOrderState();
        this.orderDatetime = entity.getOrderDatetime();
    }

    public OrderEntity toEntity(){
        return OrderEntity.builder()
                .orderNo(orderNo)
                .orderCode(orderCode)
                .cartCode1(cartCode1)
                .cartCode2(cartCode2)
                .cartCode3(cartCode3)
                .cartCode4(cartCode4)
                .cartCode5(cartCode5)
                .orderTotalPrice(orderTotalPrice)
                .orderTotalCount(orderTotalCount)
                .orderName(orderName)
                .orderPhone(orderPhone)
                .orderRecipientName(orderRecipientName)
                .orderRecipientPhone(orderRecipientPhone)
                .orderRecipientAddrNumber(orderRecipientAddrNumber)
                .orderRecipientAddr1(orderRecipientAddr1)
                .orderRecipientAddr2(orderRecipientAddr2)
                .memberId(memberId)
                .memberMileage(memberMileage)
                .memberCoupon(memberCoupon)
                .orderPayType(orderPayType)
                .orderState(orderState)
                .orderDatetime(orderDatetime)
                .build();
    }
}
