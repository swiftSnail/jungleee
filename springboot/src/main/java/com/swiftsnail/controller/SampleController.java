package com.swiftsnail.controller;

import com.swiftsnail.Service.EmailService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
//@EnableAutoConfiguration
public class SampleController {

    @Resource
    EmailService emailService;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        emailService.send("dev");
        return "Hello World!";
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SampleController.class, args);
//    }
}
