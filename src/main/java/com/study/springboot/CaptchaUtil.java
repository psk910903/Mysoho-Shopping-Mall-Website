package com.study.springboot;

import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import nl.captcha.Captcha;

import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
public class CaptchaUtil {
    private static int width = 150;    /*보안문자 이미지 가로크기*/
    private static int height = 50; /*보안문자 이미지 세로크기*/

    /*CaptCha Image 생성*/
    public void getImgCaptCha(HttpServletRequest req, HttpServletResponse res) {
        /*폰트 및 컬러 설정*/
        List<Font> fontList = new ArrayList<Font>();
        fontList.add(new Font("", Font.HANGING_BASELINE, 30));
        fontList.add(new Font("Courier", Font.ITALIC, 30));
        fontList.add(new Font("", Font.PLAIN, 30));
        List<Color> colorList = new ArrayList<Color>();
        colorList.add(Color.BLACK);

        Captcha captcha = new Captcha.Builder(width, height)
                // .addText() 또는 아래와 같이 정의 : 6자리 숫자와 폰트 및 컬러 설정
                .addText(new NumbersAnswerProducer(9), new DefaultWordRenderer(colorList, fontList))
                .addNoise().addBorder()
                .addBackground(new GradiatedBackgroundProducer())
                .build();

        /*JSP에서 Captcha 객체에 접근할 수 있도록 session에 저장*/
        req.getSession().setAttribute(Captcha.NAME, captcha);
        CaptchaServletUtil.writeImage(res, captcha.getImage());
    }

}