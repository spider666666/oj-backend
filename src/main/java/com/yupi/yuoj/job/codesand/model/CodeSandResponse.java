package com.yupi.yuoj.job.codesand.model;

import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandResponse {
    //判题信息
    private JudgeInfo judgeInfo;

    //判题时间
    private long timeLimit;

    //判题占用内存
    private long memoryLimit;

    //判题状态
    private String status;

    //最后的判题结果
    private String result;
}
