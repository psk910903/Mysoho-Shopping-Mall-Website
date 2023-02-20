package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSaveRequestDto {
    private Long itemNo;
    private String itemCategory;
    private String itemName;
    private Long itemPrice;
    private String itemOptionColor;
    private String itemOptionSize;
    private String itemInfo;
    private String itemImageUrl;

    public ProductEntity toSaveEntity() {
        return ProductEntity.builder()
                .itemCategory(itemCategory)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemOptionColor(itemOptionColor)
                .itemOptionSize(itemOptionSize)
                .itemInfo(itemInfo)
                .itemImageUrl(itemImageUrl)
                .build();
    }


    public ProductEntity toUpdateEntity() {
        return ProductEntity.builder()
                .itemNo(itemNo)
                .itemCategory(itemCategory)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemOptionColor(itemOptionColor)
                .itemOptionSize(itemOptionSize)
                .itemInfo(itemInfo)
                .itemImageUrl(itemImageUrl)
                .build();
    }
}
