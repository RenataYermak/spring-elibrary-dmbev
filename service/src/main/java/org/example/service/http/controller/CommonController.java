package org.example.service.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "user/login";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
}
