package com.yupi.yuoj.model.vo;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yupi.yuoj.model.dto.question.JudgeConfig;
import com.yupi.yuoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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
    private JudgeConfig judgeConfig;

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


    //添加返回人员相关的字段
    private UserVO userVO;

    /**
     * 是否删除
     */
    //自动改变字段，自动管理
    @TableLogic
    private Integer isDelete;

    //对比两个类，将包装类型转换成对象
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        //先拷贝
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        //对标签进行序列化
        List<String> tagList = questionVO.getTags();
        question.setTags(JSONUtil.toJsonStr(tagList));
        //对judgeConfig进行序列化
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }//
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        //
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        //转换为对象类型
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVO;
    }

}
