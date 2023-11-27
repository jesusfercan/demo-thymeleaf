package com.example.demothymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) Integer error, Model model){

        if(error!=null)
            model.addAttribute("error"+error,true);

        return "auth/login";
    }
}
