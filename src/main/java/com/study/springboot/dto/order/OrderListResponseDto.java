package com.study.springboot.dto.order;

import com.study.springboot.entity.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderListResponseDto {
    private Long order_no; //pk
    private String cart_code_1; //장바구니 코드
    private String cart_code_2;
    private String cart_code_3;
    private String cart_code_4;
    private String cart_code_5;
    private Long order_total_price; //주문 총금액
    private Long order_total_count; //주문 상품 개수
    private String order_name; // 주문자 이름
    private String order_phone; // 주문자 연락처
    private String order_recipient_name; // 수령자 이름
    private String order_recipient_phone; // 수령자 연락처
    private String order_recipient_addr_number; // 수령자 우편번호
    private String order_recipient_addr_1; // 수령자 기본주소
    private String order_recipient_addr_2; // 수령자 나머지주소
    private Long member_mileage; // 적립금 사용
    private String member_coupon; // 쿠폰 사용
    private String order_pay_type; // 휴대폰결제 or 무통장입금 선택
    private String order_state; // 주문상태
    private LocalDateTime order_datetime;

    public OrderListResponseDto(OrderEntity entity) {
        this.order_no = entity.getOrder_no();
        this.cart_code_1 = entity.getCart_code_1();
        this.cart_code_2 = entity.getCart_code_2();
        this.cart_code_3 = entity.getCart_code_3();
        this.cart_code_4 = entity.getCart_code_4();
        this.cart_code_5 = entity.getCart_code_5();
        this.order_total_price = entity.getOrder_total_price();
        this.order_total_count = entity.getOrder_total_count();
        this.order_name = entity.getOrder_name();
        this.order_phone = entity.getOrder_phone();
        this.order_recipient_name = entity.getOrder_recipient_name();
        this.order_recipient_phone = entity.getOrder_recipient_phone();
        this.order_recipient_addr_number = entity.getOrder_recipient_addr_number();
        this.order_recipient_addr_1 = entity.getOrder_recipient_addr_1();
        this.order_recipient_addr_2 = entity.getOrder_recipient_addr_2();
        this.member_mileage = entity.getMember_mileage();
        this.member_coupon = entity.getMember_coupon();
        this.order_pay_type = entity.getOrder_pay_type();
        this.order_state = entity.getOrder_state();
        this.order_datetime = entity.getOrder_datetime();
    }

    public OrderEntity toEntity(){
        return OrderEntity.builder()
                .order_no(order_no)
                .cart_code_1(cart_code_1)
                .order_total_price(order_total_price)
                .order_total_count(order_total_count)
                .order_name(order_name)
                .order_phone(order_phone)
                .order_recipient_name(order_recipient_name)
                .order_recipient_phone(order_recipient_phone)
                .order_recipient_addr_number(order_recipient_addr_number)
                .order_recipient_addr_1(order_recipient_addr_1)
                .order_recipient_addr_2(order_recipient_addr_2)
                .member_mileage(member_mileage)
                .member_coupon(member_coupon)
                .order_pay_type(order_pay_type)
                .order_state(order_state)
                .order_datetime(order_datetime)
                .build();
    }
}
