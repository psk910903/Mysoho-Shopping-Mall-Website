package com.study.springboot.comparator;

import com.study.springboot.dto.product.ProductResponseDto;

import java.util.Comparator;

public class SellCountComparator implements Comparator<ProductResponseDto> {
    @Override
    public int compare(ProductResponseDto dto1,ProductResponseDto dto2) {
        if (dto1.getSalesCount() > dto2.getSalesCount()) {
            return -1;
        } else if (dto1.getSalesCount() < dto2.getSalesCount()) {
            return 1;
        }
        return 0;
    }
}
