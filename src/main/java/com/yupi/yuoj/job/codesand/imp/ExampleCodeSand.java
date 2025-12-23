package com.yupi.yuoj.job.codesand.imp;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;

public class ExampleCodeSand implements CodeSand {
    @Override
    public CodeSandResponse executeCode(CodeSandRequest codeSandRequest) {
        System.out.println("创建示例代码沙箱");
        return null;
    }
}
