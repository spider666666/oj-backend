package com.yupi.yuoj.job.codesand.model;

import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandResponse {
    //判题信息
    private JudgeInfo judgeInfo;

    //输出结果
    private List<String> outPutList;

    //判题状态
    private String status;

    //额外信息
    private String message;
}
