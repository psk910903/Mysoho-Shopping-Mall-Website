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
    private Long cart_no; //pk
    @Column(name = "member_id")
    private String member_id; //아이디(회원)
    @Column(name = "session_id")
    private String session_id;//세션아이디(비회원)
    @Column(name = "item_code")
    private String item_code;//상품코드
    @Column(name = "item_name")
    private String item_name;//상품이름
    @Column(name = "item_option_color")
    private String item_option_color;//색상
    @Column(name = "item_option_size")
    private String item_option_size;//사이즈
    @Column(name = "cart_item_amount")
    private Long cart_item_amount;//수량
    @Column(name = "cart_date")
    private LocalDateTime cart_date;//장바구니 생성시간

    @Builder
    public CartEntity(Long cart_no, String member_id, String session_id, String item_code, String item_name, String item_option_color, String item_option_size, Long cart_item_amount, LocalDateTime cart_date) {
        this.cart_no = cart_no;
        this.member_id = member_id;
        this.session_id = session_id;
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_option_color = item_option_color;
        this.item_option_size = item_option_size;
        this.cart_item_amount = cart_item_amount;
        this.cart_date = cart_date;
    }
}
