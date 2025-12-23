package com.yupi.yuoj.job.codesand.imp;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;

public class RemoteCodeSand implements CodeSand {
    @Override
    public CodeSandResponse executeCode(CodeSandRequest codeSandRequest) {
        //在这里填写执行代码的具体信息
        System.out.println("创建远程代码沙箱");
        return null;
    }
}
