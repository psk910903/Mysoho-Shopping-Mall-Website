package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductSaveRequestDto {
    private Long itemNo;
    private String itemCategory;
    private String itemName;
    private Long itemPrice;
    private Long itemDiscountRate;
    private String itemOptionColor;
    private String itemOptionSize;
    private String itemInfo;
    private String itemImageUrl;
    private String itemExposure;
    private String itemSoldOut;
    private LocalDateTime itemUpdateDatetime;

    public ProductEntity toSaveEntity() {
        return ProductEntity.builder()
                .itemCategory(itemCategory)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemDiscountRate(itemDiscountRate)
                .itemOptionColor(itemOptionColor)
                .itemOptionSize(itemOptionSize)
                .itemInfo(itemInfo)
                .itemImageUrl(itemImageUrl)
                .itemExposure(itemExposure)
                .itemSoldOut(itemSoldOut)
                .itemUpdateDatetime(itemUpdateDatetime)
                .build();
    }

    public ProductEntity toUpdateEntity() {
        return ProductEntity.builder()
                .itemNo(itemNo)
                .itemCategory(itemCategory)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemDiscountRate(itemDiscountRate)
                .itemOptionColor(itemOptionColor)
                .itemOptionSize(itemOptionSize)
                .itemInfo(itemInfo)
                .itemImageUrl(itemImageUrl)
                .itemExposure(itemExposure)
                .itemSoldOut(itemSoldOut)
                .itemUpdateDatetime(itemUpdateDatetime)
                .build();
    }
}
