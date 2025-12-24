package com.yupi.yuoj.job.codesand.service;

import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.CodeSandProxy;
import com.yupi.yuoj.job.codesand.factory.CodeSandFactory;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;
import com.yupi.yuoj.model.dto.question.JudgeCase;
import com.yupi.yuoj.model.dto.question.JudgeConfig;
import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.enums.JudgeInfoMessageEnum;
import com.yupi.yuoj.model.enums.StatusEnum;
import com.yupi.yuoj.service.QuestionService;
import com.yupi.yuoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//判题服务的目的是返回判题信息
@Service
public class JudgeServiceImp implements JudgeService {

    @Value("${codeSand.type:example}")
    private String type;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {


        //1.做一些安全性的校验
        if (questionSubmitId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交的问题不存在");

        }
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);

        Long questionId = questionSubmit.getQuestionId();
        String code = questionSubmit.getCode();
        Integer status = questionSubmit.getStatus();
        String language = questionSubmit.getLanguage();

        if (questionId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "问题不存在");
        }
        if (!status.equals(StatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前问题正在判题中");
        }


        //2.调用代码沙箱进行判题(更新题目提交的状态)
        QuestionSubmit questionSubmit1 = new QuestionSubmit();
        questionSubmit1.setId(questionSubmitId);
        questionSubmit1.setStatus(StatusEnum.WAITING.getValue());
        boolean success = questionSubmitService.updateById(questionSubmit1);
        if(!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"当前数据更新失败");
        }

        CodeSandFactory codeSandFactory = new CodeSandFactory(type);

        CodeSand codeSand = codeSandFactory.newInstance();
        CodeSandProxy codeSandProxy = new CodeSandProxy(codeSand);

        //构建处理请求
        CodeSandRequest request = new CodeSandRequest();
        request.setCode(code);
        request.setLanguage(language);
        Question question = questionService.getById(questionId);
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(judgeCase -> judgeCase.getInput()).collect(Collectors.toList());
        List<String> outputList = judgeCaseList.stream().map(judgeCase -> judgeCase.getOutput()).collect(Collectors.toList());
        request.setInputList(inputList);


        //3.处理代码沙箱返回的结果
        CodeSandResponse codeSandResponse = codeSandProxy.executeCode(request);

        JudgeInfo judgeInfo = codeSandResponse.getJudgeInfo();
        List<String> outPutList = codeSandResponse.getOutPutList();

        // 设置判题信息（不清除判题状态和判题信息的区别）
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
        if(outPutList == null ||outPutList.size() != inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.FAILED;
            //表示运行错误
        }
        else{
            for(int i =0;i<outPutList.size();i++){
                if(!Objects.equals(outPutList.get(i), outputList.get(i))){
                    //表示运行错误
                    judgeInfoMessageEnum = JudgeInfoMessageEnum.FAILED;

                }
            }

        }

        //todo 判断题目限制（这里需要更改，因为不同的编程语言花费的时间可能不相同，所以在这里需要使用策略模式，针对不同的语言设置不同的判题限制的方法）
        //todo 最简单的方法是使用if-else的方法
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        if(judgeInfo.getTime() > judgeConfig.getTimeLimit()){
            //超出时间显示
            judgeInfoMessageEnum = JudgeInfoMessageEnum.FAILED;
        }
        if(judgeInfo.getMemory() > judgeConfig.getMemoryLimit()){
            //超出内存限制
            judgeInfoMessageEnum = JudgeInfoMessageEnum.FAILED;
        }


        judgeInfo.setMessage(judgeInfoMessageEnum.getValue());
        String judgeInfoStr = JSONUtil.toJsonStr(judgeInfo);
        //更改响应结果
        QuestionSubmit questionSubmit2 = new QuestionSubmit();
        questionSubmit2.setId(questionSubmitId);
        questionSubmit2.setJudgeInfo(judgeInfoStr);
        questionSubmit2.setStatus(StatusEnum.SUCCESS.getValue());
        boolean update = questionSubmitService.updateById(questionSubmit2);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }

        //重新查询数据库返回数据(这里的getbyId返回的值是问题还是提交的id)
        QuestionSubmit result = questionSubmitService.getById(questionSubmitId);
        return result;
    }
}
