package com.mabo.listener;


import com.mabo.annotation.AddEventAop;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AddEventAop
public class AddEvent{
    /**
     * @Description : 如果该方法被调用了，则AddEventListener修饰的方法也执行
     */
    @AddEventAop
    public String addEvent(String s){
        System.out.println("addEvent执行,参数:"+s);
        return s;
    }
}
