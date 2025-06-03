package com.carry_guide.carry_guide_admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @RequestMapping("/index")
    public String init() {
        return "It's working!";
    }

}
