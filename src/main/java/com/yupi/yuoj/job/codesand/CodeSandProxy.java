package com.yupi.yuoj.job.codesand;

import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;
import lombok.extern.slf4j.Slf4j;

//这是一个静态代理类用于实现功能的增强
@Slf4j
public class CodeSandProxy {

    private final CodeSand codeSand;

    public CodeSandProxy(CodeSand codeSand){
        this.codeSand = codeSand;
    }

    public CodeSandResponse executeCode(CodeSandRequest request){
        //添加日志，输出参数信息
        log.info("代码沙箱的请求参数为：{},{}", request.getCode(), request.getLanguage());
        CodeSandResponse response = codeSand.executeCode(request);
        log.info("代码沙箱的返回结果为：{}", response);
        return response;
    }

}
