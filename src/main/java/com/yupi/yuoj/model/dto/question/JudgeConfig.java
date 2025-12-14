package com.yupi.yuoj.model.dto.question;

import lombok.Data;

//判题配置
@Data
public class JudgeConfig {

    private long timeLimit;

    private long MemoryLimit;

    private long StackLimit;
}
