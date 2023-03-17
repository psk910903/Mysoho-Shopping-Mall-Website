package com.study.springboot.comparator;

import com.study.springboot.dto.product.ProductResponseDto;

import javax.servlet.http.Cookie;
import java.util.Comparator;

public class CookiesComparator implements Comparator<Cookie> {
    @Override
    public int compare(Cookie c1, Cookie c2) {
        String c1Name = c1.getName();
        String c2Name = c2.getName();

        return c2Name.compareTo(c1Name);
    }
}
