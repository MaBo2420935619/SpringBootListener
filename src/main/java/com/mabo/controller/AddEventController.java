package com.mabo.controller;

import com.mabo.listener.AddEvent;
import com.mabo.annotation.AddEventAop;
import com.mabo.annotation.AddEventListener;
import com.mabo.listener.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AddEventAop
@RestController
@RequestMapping("addEvent")
public class AddEventController {
    @Autowired
    private AddEvent addEvent;

    /**
     * @Author mabo
     * @Description   该方法主动调用
     */

    @RequestMapping("add")
    public void add(){
        String test = addEvent.addEvent("测试");
        System.out.println("add方法的返回值为:"+test);
    }

    @AddEventListener(EventType.BEFOREEVENT)
    public void test1(String s) {
        System.out.println("test1执行成功，参数:"+s);
    }

    @AddEventListener(EventType.BEFOREEVENT)
    public void test2(String s) {
        System.out.println("test2执行成功，参数:"+s);
    }

    @AddEventListener(EventType.AFTEREVENT)
    public void test3(String s) {
        System.out.println("test3执行成功，参数:"+s);
    }

    @AddEventListener(EventType.AFTEREVENT)
    public void test4(String s) {
        System.out.println("test4执行成功，参数:"+s);
    }
}
