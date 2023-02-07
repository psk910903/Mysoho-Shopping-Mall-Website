package com.study.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controller2 {

    @GetMapping("/admin/notice")
    public String adminHome() {
        return "/admin/notice/test";
    }



}
