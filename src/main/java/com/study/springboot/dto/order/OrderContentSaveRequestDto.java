package com.study.springboot.dto.order;


import com.study.springboot.entity.OrderEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class OrderContentSaveRequestDto {
    private Long orderNo; //pk
    private String cartCode1; //장바구니 코드
    private Long orderTotalPrice; //주문 총금액
    private Long orderTotalCount; //주문 상품 개수
    private String orderName; // 주문자 이름
    private String orderPhone; // 주문자 연락처
    private String orderRecipientName; // 수령자 이름
    private String orderRecipientPhone; // 수령자 연락처
    private String orderRecipientAddrNumber; // 수령자 우편번호
    private String orderRecipientAddr1; // 수령자 기본주소
    private String orderRecipientAddr2; // 수령자 나머지주소
    private Long memberMileage; // 적립금 사용
    private String memberCoupon; // 쿠폰 사용
    private String orderPayType; // 휴대폰결제 or 무통장입금 선택
    private String orderState; // 주문상태
    private LocalDateTime orderDatetime;
    
    public OrderEntity toEntity(){
        return OrderEntity.builder()
                .orderNo(orderNo)
                .cartCode1(cartCode1)
                .orderTotalPrice(orderTotalPrice)
                .orderTotalCount(orderTotalCount)
                .orderName(orderName)
                .orderPhone(orderPhone)
                .orderRecipientName(orderRecipientName)
                .orderRecipientPhone(orderRecipientPhone)
                .orderRecipientAddrNumber(orderRecipientAddrNumber)
                .orderRecipientAddr1(orderRecipientAddr1)
                .orderRecipientAddr2(orderRecipientAddr2)
                .memberMileage(memberMileage)
                .memberCoupon(memberCoupon)
                .orderPayType(orderPayType)
                .orderState(orderState)
                .orderDatetime(orderDatetime)
                .build();
    }
}
    
