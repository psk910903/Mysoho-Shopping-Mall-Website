package com.study.springboot.dto.product;

import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSaveRequestDto {
    private Long item_no;
    private String item_category;
    private String item_name;
    private Long item_price;
    private String item_option_color;
    private String item_option_size;
    private String item_info;
    private String item_image_url;

    public ProductEntity toSaveEntity() {
        return ProductEntity.builder()
                .item_category(item_category)
                .item_name(item_name)
                .item_price(item_price)
                .item_option_color(item_option_color)
                .item_option_size(item_option_size)
                .item_info(item_info)
                .item_image_url(item_image_url)
                .build();
    }


    public ProductEntity toUpdateEntity() {
        return ProductEntity.builder()
                .item_no(item_no)
                .item_category(item_category)
                .item_name(item_name)
                .item_price(item_price)
                .item_option_color(item_option_color)
                .item_option_size(item_option_size)
                .item_info(item_info)
                .item_image_url(item_image_url)
                .build();
    }
}
