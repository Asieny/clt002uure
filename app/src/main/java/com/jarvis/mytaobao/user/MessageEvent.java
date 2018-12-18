package com.jarvis.mytaobao.user;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/31 10:49
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
