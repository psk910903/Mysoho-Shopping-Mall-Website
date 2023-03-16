package com.study.springboot.controller.User;

import com.study.springboot.CaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class CaptchaController {

    // captcha 이미지 가져오는 메서드
    @GetMapping("/captchaImg.do")
    @ResponseBody
    public void captchaImg(HttpServletRequest req, HttpServletResponse res) throws Exception{
        new CaptchaUtil().getImgCaptCha(req, res);
    }
}
