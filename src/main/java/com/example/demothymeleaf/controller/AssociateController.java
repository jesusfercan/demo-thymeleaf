package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.service.AssociateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class AssociateController {

    @Autowired
    private AssociateService associateService;

    @GetMapping
    public ModelAndView listAllAssociates(){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("associates");
        mv.setStatus(HttpStatus.OK);
        mv.addObject("associateList",associateService.getAllAssociates());

        return mv;
    }
}
