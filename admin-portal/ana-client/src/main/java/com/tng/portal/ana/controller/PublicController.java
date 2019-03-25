package com.tng.portal.ana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Dell on 2019/3/5.
 */
@Controller
public class PublicController {
    @GetMapping("/")
    public String redirect(){
        return "redirect:/swagger-ui.html";
    }
}
