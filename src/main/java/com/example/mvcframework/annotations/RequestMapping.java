package com.example.mvcframework.annotations;

import java.lang.annotation.*;

@Documented     //可以被以文档的形式读取
@Target({ElementType.TYPE, ElementType.METHOD})   //作用范围，既可以加在类上又可以加在方法上
@Retention(RetentionPolicy.RUNTIME) //生命周期，加载到jvm内存中也有效
public @interface RequestMapping {

    String value() default "";

}
