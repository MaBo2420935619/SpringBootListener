package com.mabo.annotation;
/**
 * @Description : 在当前修饰的方法前后执行其他的方法
 * @Author : mabo
*/

import com.mabo.listener.EventType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface AddEventListener {
    EventType value() ;
}
