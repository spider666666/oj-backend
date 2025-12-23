package com.yupi.yuoj.job.codesand.imp;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;

public class ThirdCodeSand implements CodeSand {
    @Override
    public CodeSandResponse executeCode(CodeSandRequest codeSandRequest) {
        System.out.println("创建第三方代码沙箱");
        return null;
    }
}
