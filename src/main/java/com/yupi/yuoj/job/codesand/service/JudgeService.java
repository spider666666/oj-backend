package com.yupi.yuoj.job.codesand.service;

import com.yupi.yuoj.model.entity.QuestionSubmit;

//判题服务的目的是返回判题信息
public interface JudgeService {
    //通过输入判题信息的id返回当前判题结果
    QuestionSubmit doJudge(Long questionSubmitId);
}