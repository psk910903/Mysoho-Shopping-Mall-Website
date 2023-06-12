package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto implements Cloneable  {
    private Long itemNo;
    private String itemCategory;
    private String itemImageUrl;
    private String itemName;
    private Long itemPrice;
    private Long itemDiscountPrice; //할인적용한 가격
    private Long itemDiscountRate;
    private String itemOptionColor;
    private String itemOptionSize;
    private String itemInfo;
    private String itemExposure;
    private String itemSoldOut;
    private String itemUpdateDatetime;
    private int salesCount;
    private int ReviewCount;
    private int ReviewStar;


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

    @Override
    public ProductResponseDto clone() {
        try {
            // TODO: 이 복제본이 원본의 내부를 변경할 수 없도록 여기에 가변 상태를 복사합니다
            return (ProductResponseDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
