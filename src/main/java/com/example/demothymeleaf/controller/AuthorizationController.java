package com.example.demothymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) Boolean error){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("auth/login");

        if(error!=null && error)
            mv.addObject("error",true);

        return mv;
    }
}
