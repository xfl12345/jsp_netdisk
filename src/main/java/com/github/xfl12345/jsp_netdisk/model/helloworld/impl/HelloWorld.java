package com.github.xfl12345.jsp_netdisk.model.helloworld.impl;

import com.github.xfl12345.jsp_netdisk.model.helloworld.IHelloWorld;

public class HelloWorld implements IHelloWorld {

    @Override
    public void sayHelloWorld(){
        System.out.println("Hello world!");
        return;
    }

}
