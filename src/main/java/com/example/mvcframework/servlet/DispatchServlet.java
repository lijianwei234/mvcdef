package com.example.mvcframework.servlet;

import com.example.mvcframework.annotations.Autowired;
import com.example.mvcframework.annotations.Controller;
import com.example.mvcframework.annotations.Service;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class DispatchServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();//缓存扫描到的类的全限定类名

    private Map<String, Object> ioc = new HashMap<>();

    @Override
    public void init() throws ServletException {
        //加载配置文件 springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        try {
            doLoadConfig(contextConfigLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //扫描注解
        doScan(properties.getProperty("scanPackage"));

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
    private void doAutowired() throws IllegalAccessException {
        if (ioc.isEmpty()) return;

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取bean对象中的字段
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                if (!declaredField.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Autowired annotation = declaredField.getAnnotation(Autowired.class);
                String beanName = annotation.value();
                if ("".equals(beanName)) {
                    beanName = declaredField.getType().getName();
                }

                declaredField.setAccessible(true);
                declaredField.set(entry.getValue(), ioc.get(beanName));
            }
        }
    }

    //ioc容器
    private void doInstance() {
        if (classNames.size() == 0) {
            return;
        }

        try{
            for (int i = 0; i < classNames.size(); i++) {
                String className = classNames.get(i);

                //反射
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(Controller.class)) {
                    String simpleName = aClass.getSimpleName();
                    String s = lowerFirst(simpleName);
                    Object o = aClass.newInstance();
                    ioc.put(s, o);
                } else if (aClass.isAnnotationPresent(Service.class)) {
                    Service annotation = aClass.getAnnotation(Service.class);
                    //获取注解值, 指定了id就以id为
                    String beanName = annotation.value();
                    if (!"".equals(beanName)) {
                        ioc.put(beanName, aClass.newInstance());
                    } else {
                        beanName = lowerFirst(aClass.getSimpleName());
                        ioc.put(beanName, aClass.newInstance());
                    }

                    //如果继承了接口，那么再存一份
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j = 0; j < interfaces.length; j++) {
                        Class<?> anInterface = interfaces[i];
                        //以接口的全限定类名放入容器
                        ioc.put(anInterface.getName(), aClass.newInstance());
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if ('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    //扫描类
    private void doScan(String scanPackage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackage);

        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        properties.load(resourceAsStream);
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
