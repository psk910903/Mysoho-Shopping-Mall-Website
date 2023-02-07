package com.study.springboot.controller;

import com.study.springboot.dto.BoardResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.product.Product;
import com.study.springboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class Controller1 {
    final ProductService productService;

    @RequestMapping("/adminHome/productList")
    public String listForm( Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Product> paging = productService.getList(page); //페이징처리
        model.addAttribute("paging", paging);

        List<ProductResponseDto> list = new ArrayList<>();
        for (Product entity : paging) {
            list.add(new ProductResponseDto(entity));
        }
        model.addAttribute("list", list);

        return "/admin/product/productList";
    }



}
