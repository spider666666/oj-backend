package com.yupi.yuoj.job.codesand.imp;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;
import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;


public class ExampleCodeSand implements CodeSand {
    @Override
    public CodeSandResponse executeCode(CodeSandRequest codeSandRequest) {
        //获取参数信息，传入当前模型，进行判断
        String code = codeSandRequest.getCode();
        String language = codeSandRequest.getLanguage();
        System.out.println("创建示例代码沙箱");


        //自主创建返回参数
        CodeSandResponse response = new CodeSandResponse();
        response.setStatus("waiting");
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(1000L);
        judgeInfo.setTime(1000L);
        judgeInfo.setMessage("waiting");
        response.setJudgeInfo(judgeInfo);
        return response;
    }
}
