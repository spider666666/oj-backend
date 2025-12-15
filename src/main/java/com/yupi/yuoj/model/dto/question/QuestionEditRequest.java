package com.yupi.yuoj.model.dto.question;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 编辑请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）//对于题型进行一个标识
     */
    private List<String> tags;

    /**
     * 标准答案
     */
    private String answer;

    /**
     * 判题配置（json）
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题用例（json）
     */
    private List<JudgeCase> judgeCase;
}