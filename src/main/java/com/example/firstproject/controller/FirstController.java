package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hi")
    public String niceToMeetYou(Model model) { //모델 받아오기 파라미터에 추가
        model.addAttribute("username","지수");     //변수등록 : addAttribute(템플릿변수, 보낼 값)
        return "greetings";
    }

    @GetMapping("/bye")
    public String seeYou(Model model){
        model.addAttribute("nickname","지구");
        return "goodbye";
    }

}
