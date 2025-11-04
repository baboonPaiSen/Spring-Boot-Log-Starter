package cn.com.riven.starter.log.service;

import cn.com.riven.starter.log.model.Result;

public interface InvokeExceptionService {


    Result<?> doSomeThingWithReturn(Exception  exception);

    void doSomeThing(Exception  exception);

    default Integer  handler(){
        return 1;
    };

}