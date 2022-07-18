package com.example.mvcframework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatchServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        //加载配置文件 springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);

        //扫描注解
        doScan("");

        //初始化bean对象(基于注解实现ioc容器)
        doInstance();

        //实现依赖注入
        doAutowired();

        //构造一个handlermapping，将配置好的url和method建立映射关系
        initHandlerMapping();

        //处理请求
    }

    private void initHandlerMapping() {

    }

    //实现依赖注入
    private void doAutowired() {

    }

    //ioc容器
    private void doInstance() {

    }

    //扫描类
    private void doScan(String scanPackage) {

    }

    private void doLoadConfig(String contextConfigLocation) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
