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
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_no", nullable = false)
    private Long itemNo; //상품번호
    @Column(name = "item_category", nullable = false)
    private String itemCategory; //카테고리
    @Column(name = "item_name", nullable = false)
    private String itemName; //상품명
    @Column(name = "item_option_color", nullable = false)
    private String itemOptionColor; //색상
    @Column(name = "item_option_size", nullable = false)
    private String itemOptionSize; //사이즈
    @Column(name = "item_price", nullable = false)
    private Long itemPrice; //가격
    @Column(name = "item_discount_rate", nullable = false)
    private Long itemDiscountRate; //할인율
    @Column(name = "item_image_url", nullable = false)
    private String itemImageUrl; //메인이미지
    @Column(name = "item_info")
    private String itemInfo; //상품정보(설명)
    @Column(name = "item_exposure")
    private String itemExposure; //노출여부
    @Column(name = "item_sold_out")
    private String itemSoldOut; //품절여부
    @Column(name = "item_update_datetime", nullable = true)
    private LocalDateTime itemUpdateDatetime = LocalDateTime.now(); // 생성일,수정일

    @Builder
    public ProductEntity(Long itemNo, String itemCategory, String itemName, String itemOptionColor, String itemOptionSize, Long itemPrice, Long itemDiscountRate, String itemImageUrl, String itemInfo, String itemExposure, String itemSoldOut, LocalDateTime itemUpdateDatetime) {
        this.itemNo = itemNo;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionSize = itemOptionSize;
        this.itemPrice = itemPrice;
        this.itemDiscountRate = itemDiscountRate;
        this.itemImageUrl = itemImageUrl;
        this.itemInfo = itemInfo;
        this.itemExposure = itemExposure;
        this.itemSoldOut = itemSoldOut;
        this.itemUpdateDatetime = itemUpdateDatetime;
    }
}
