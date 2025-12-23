package com.yupi.yuoj.job.codesand;

import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;

// 对于代码沙箱的一些统一规范接口
public interface CodeSand {
    //执行代码的接口
    CodeSandResponse executeCode(CodeSandRequest codeSandRequest);

}
