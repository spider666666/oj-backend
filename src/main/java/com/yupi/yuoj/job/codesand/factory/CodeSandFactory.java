package com.yupi.yuoj.job.codesand.factory;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.imp.ExampleCodeSand;
import com.yupi.yuoj.job.codesand.imp.RemoteCodeSand;
import com.yupi.yuoj.job.codesand.imp.ThirdCodeSand;

public class CodeSandFactory {

    //维护一个字符串，通过字符串的方式来获取到当前需要创建什么代码沙箱的实例
    private final String codeSandType;

    public CodeSandFactory(String type) {
        this.codeSandType = type;
    }

    public CodeSand newInstance(){
        switch(codeSandType){
            case "example" :
                return new ExampleCodeSand();
            case "remote" :
                return new RemoteCodeSand();
            case "third":
                return new ThirdCodeSand();
            default:
                //默认返回实例的对象
                return new ExampleCodeSand();
        }
    }
}
