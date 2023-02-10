package com.study.springboot.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {

    private String productCategory;
    private String keyWord;

}
