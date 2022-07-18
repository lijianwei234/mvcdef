package com.example.demo.controller;

import com.example.demo.service.DemoService;
import com.example.mvcframework.annotations.Autowired;
import com.example.mvcframework.annotations.Controller;
import com.example.mvcframework.annotations.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    public String query(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

}
