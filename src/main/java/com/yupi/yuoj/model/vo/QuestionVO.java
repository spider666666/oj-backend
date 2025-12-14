package com.yupi.yuoj.model.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionVO {
    /**
     * id
     */
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
     * 通过数量
     */
    private Integer acceptedNum;

    /**
     * 提交数量
     */
    private Integer submittedNum;

    /**
     * 判题配置（json）
     */
    private String judgeConfig;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    //自动改变字段，自动管理
    @TableLogic
    private Integer isDelete;



}
