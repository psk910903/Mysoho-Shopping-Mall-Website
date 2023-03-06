package com.study.springboot.comparator;

import com.study.springboot.dto.product.ProductResponseDto;

import java.util.Comparator;

public class ItemPriceComparator implements Comparator<ProductResponseDto> {
    @Override
    public int compare(ProductResponseDto dto1,ProductResponseDto dto2) {
        //판매량기준 비교
        if (dto1.getItemDiscountPrice() < dto2.getItemDiscountPrice()) {
            return -1;
        } else if (dto1.getItemDiscountPrice() > dto2.getItemDiscountPrice()) {
            return 1;
        }
        return 0;
    }
}
