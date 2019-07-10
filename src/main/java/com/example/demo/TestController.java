package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ={"/hello","hello1","hello2"})
public class TestController {


    @GetMapping(value = {"/test","9527"})
    public String test() {
        return "OK";
    }
}
