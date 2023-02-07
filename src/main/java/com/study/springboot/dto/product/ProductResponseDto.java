package com.study.springboot.dto.product;

import com.study.springboot.entity.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long item_no;
    private String item_code;
    private String item_image_url;
    private String item_name;
    private int item_price;

    public ProductResponseDto(Product entity) {
        this.item_no = entity.getItem_no();
        this.item_code = entity.getItem_code();
        this.item_image_url = entity.getItem_image_url();
        this.item_name = entity.getItem_name();
        this.item_price = entity.getItem_price();
    }
}
