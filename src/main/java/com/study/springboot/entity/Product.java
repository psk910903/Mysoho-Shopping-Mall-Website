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
@Table(name = "ITEM")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_no", nullable = false)
    private Long item_no;
    @Column(name = "item_code", nullable = false)
    private String item_code;
    @Column(name = "item_name", nullable = false)
    private String item_name;
    @Column(name = "item_option_color", nullable = true)
    private String item_option_color;
    @Column(name = "item_option_size", nullable = true)
    private String item_option_size;
    @Column(name = "item_price", nullable = false)
    private int item_price;
    @Column(name = "item_image_url", nullable = true)
    private String item_image_url;
    @Column(name = "item_info", nullable = true)
    private String item_info;
    @Column(name = "item_info_image_url_1", nullable = true)
    private String item_info_image_url_1;
    @Column(name = "item_info_image_url_2", nullable = true)
    private String item_info_image_url_2;
    @Column(name = "item_update_datetime", nullable = true)
    private LocalDateTime item_update_datetime = LocalDateTime.now(); // 생성일,수정일

    @Builder
    public Product(Long item_no, String item_code, String item_name, String item_option_color, String item_option_size, int item_price, String item_image_url, String item_info, String item_info_image_url_1, String item_info_image_url_2, LocalDateTime item_update_datetime) {
        this.item_no = item_no;
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_option_color = item_option_color;
        this.item_option_size = item_option_size;
        this.item_price = item_price;
        this.item_image_url = item_image_url;
        this.item_info = item_info;
        this.item_info_image_url_1 = item_info_image_url_1;
        this.item_info_image_url_2 = item_info_image_url_2;
        this.item_update_datetime = item_update_datetime;
    }
}
