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
    private Long itemNo;
    @Column(name = "item_category", nullable = false)
    private String itemCategory;
    @Column(name = "item_name", nullable = false)
    private String itemName;
    @Column(name = "item_option_color", nullable = true)
    private String itemOptionColor;
    @Column(name = "item_option_size", nullable = true)
    private String itemOptionSize;
    @Column(name = "item_price", nullable = false)
    private Long itemPrice;
    @Column(name = "item_image_url", nullable = true)
    private String itemImageUrl;
    @Column(name = "item_info", nullable = true)
    private String itemInfo;
    @Column(name = "item_info_image_url_1", nullable = true)
    private String itemInfoImageUrl1;
    @Column(name = "item_info_image_url_2", nullable = true)
    private String itemInfoImageUrl2;
    @Column(name = "item_update_datetime", nullable = true)
    private LocalDateTime itemUpdateDatetime = LocalDateTime.now(); // 생성일,수정일

    @Builder
    public ProductEntity(Long itemNo, String itemCategory, String itemName, String itemOptionColor, String itemOptionSize, Long itemPrice, String itemImageUrl, String itemInfo, String itemInfoImageUrl1, String itemInfoImageUrl2) {
        this.itemNo = itemNo;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionSize = itemOptionSize;
        this.itemPrice = itemPrice;
        this.itemImageUrl = itemImageUrl;
        this.itemInfo = itemInfo;
        this.itemInfoImageUrl1 = itemInfoImageUrl1;
        this.itemInfoImageUrl2 = itemInfoImageUrl2;
    }
}
