package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long itemNo;
    private String itemCategory;
    private String itemImageUrl;
    private String itemName;
    private Long itemPrice;
    private Long itemDiscountRate;
    private String itemOptionColor;
    private String itemOptionSize;
    private String itemInfo;
    private String itemExposure;
    private String itemSoldOut;
    private String itemUpdateDatetime;


    public ProductResponseDto(ProductEntity entity) {
        this.itemNo = entity.getItemNo();
        this.itemCategory = entity.getItemCategory();
        this.itemImageUrl = entity.getItemImageUrl();
        this.itemName = entity.getItemName();
        this.itemPrice = entity.getItemPrice();
        this.itemDiscountRate = entity.getItemDiscountRate();
        this.itemOptionColor = entity.getItemOptionColor();
        this.itemOptionSize = entity.getItemOptionSize();
        this.itemInfo = entity.getItemInfo();
        this.itemExposure = entity.getItemExposure();
        this.itemSoldOut = entity.getItemSoldOut();
    }
}
