package com.study.springboot.comparator;

import com.study.springboot.dto.product.ProductResponseDto;

import java.util.Comparator;

public class ItemReviewStarComparator implements Comparator<ProductResponseDto> {
    @Override
    public int compare(ProductResponseDto dto1,ProductResponseDto dto2) {
        if (dto1.getReviewStar() > dto2.getReviewStar()) {
            return -1;
        } else if (dto1.getReviewStar() < dto2.getReviewStar()) {
            return 1;
        }
        return 0;
    }
}
