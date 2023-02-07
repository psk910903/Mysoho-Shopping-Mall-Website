package com.study.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller5 {
    @GetMapping("templates/admin/qna/listForm")
    public String main() {
        return "templates/admin/qna/listForm";
    }


}
