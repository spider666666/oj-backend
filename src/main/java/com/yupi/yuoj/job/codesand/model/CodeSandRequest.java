package com.yupi.yuoj.job.codesand.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandRequest {

    //请求其实就是当时提交的题目信息

    //代码
    private String code;

    //语言
    private String language;

    //题目的id
    private long questionId;

}
