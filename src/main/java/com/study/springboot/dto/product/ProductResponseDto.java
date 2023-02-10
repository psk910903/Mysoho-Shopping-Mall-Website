package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long item_no;
    private String item_category;
    private String item_image_url;
    private String item_name;
    private Long item_price;
    private String item_option_color;
    private String item_option_size;
    private String item_info;

    public ProductResponseDto(ProductEntity entity) {
        this.item_no = entity.getItem_no();
        this.item_category = entity.getItem_category();
        this.item_image_url = entity.getItem_image_url();
        this.item_name = entity.getItem_name();
        this.item_price = entity.getItem_price();
        this.item_option_color = entity.getItem_option_color();
        this.item_option_size = entity.getItem_option_size();
        this.item_info = entity.getItem_info();
    }
}
