package com.yupi.yuoj.job.codesand.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    //判题用例
    private List<String> inputList;

}
