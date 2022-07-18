package com.example.demo.service.impl;

import com.example.demo.service.DemoService;
import com.example.mvcframework.annotations.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Override
    public String get(String name) {
        System.out.println("service 找那个的name = " + name );
        return name;
    }
}
