package com.yupi.yuoj.model.dto.questionsubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yupi.yuoj.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 创建请求,用于查询返回提交记录
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 判题状态(0 - 待判题，1-判题中 ，2- 判题成功 ，3-判题失败)
     */
    private Integer status;

    /**
     * 编程语言
     */
    private String language;
}