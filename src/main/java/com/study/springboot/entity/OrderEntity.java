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
    private Long order_no; //pk
    @Column(name = "cart_code_1", nullable = false)
    private String cart_code_1; //장바구니 코드
    @Column(name = "cart_code_2")
    private String cart_code_2;
    @Column(name = "cart_code_3")
    private String cart_code_3;
    @Column(name = "cart_code_4")
    private String cart_code_4;
    @Column(name = "cart_code_5")
    private String cart_code_5;
    @Column(name = "order_total_price", nullable = false)
    private Long order_total_price; //주문 총금액
    @Column(name = "order_total_count", nullable = false)
    private Long order_total_count; //주문 상품 개수
    @Column(name = "order_name", nullable = false)
    private String order_name; // 주문자 이름
    @Column(name = "order_phone", nullable = false)
    private String order_phone; // 주문자 연락처
    @Column(name = "order_recipient_name", nullable = false)
    private String order_recipient_name; // 수령자 이름
    @Column(name = "order_recipient_phone", nullable = false)
    private String order_recipient_phone; // 수령자 연락처
    @Column(name = "order_recipient_addr_number", nullable = false)
    private String order_recipient_addr_number; // 수령자 우편번호
    @Column(name = "order_recipient_addr_1", nullable = false)
    private String order_recipient_addr_1; // 수령자 기본주소
    @Column(name = "order_recipient_addr_2", nullable = false)
    private String order_recipient_addr_2; // 수령자 나머지주소
    @Column(name = "member_mileage")
    private Long member_mileage; // 적립금 사용
    @Column(name = "member_coupon")
    private String member_coupon; // 쿠폰 사용
    @Column(name = "order_pay_type", nullable = false)
    private String order_pay_type; // 휴대폰결제 or 무통장입금 선택
    @Column(name = "order_state", nullable = false)
    private String order_state; // 주문상태
    @Column(name = "order_datetime")
    private LocalDateTime order_datetime = LocalDateTime.now(); // 결제시간

    @Builder
    public OrderEntity(Long order_no, String cart_code_1, String cart_code_2, String cart_code_3, String cart_code_4, String cart_code_5, Long order_total_price, Long order_total_count, String order_name, String order_phone, String order_recipient_name, String order_recipient_phone, String order_recipient_addr_number, String order_recipient_addr_1, String order_recipient_addr_2, Long member_mileage, String member_coupon, String order_pay_type, String order_state, LocalDateTime order_datetime) {
        this.order_no = order_no;
        this.cart_code_1 = cart_code_1;
        this.cart_code_2 = cart_code_2;
        this.cart_code_3 = cart_code_3;
        this.cart_code_4 = cart_code_4;
        this.cart_code_5 = cart_code_5;
        this.order_total_price = order_total_price;
        this.order_total_count = order_total_count;
        this.order_name = order_name;
        this.order_phone = order_phone;
        this.order_recipient_name = order_recipient_name;
        this.order_recipient_phone = order_recipient_phone;
        this.order_recipient_addr_number = order_recipient_addr_number;
        this.order_recipient_addr_1 = order_recipient_addr_1;
        this.order_recipient_addr_2 = order_recipient_addr_2;
        this.member_mileage = member_mileage;
        this.member_coupon = member_coupon;
        this.order_pay_type = order_pay_type;
        this.order_state = order_state;
        this.order_datetime = order_datetime;
    }
}
