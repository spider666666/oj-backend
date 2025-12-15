package com.yupi.yuoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;


//目前没有什么好的想法，先都全部返回算了
@Data
public class QuestionSubmitVO {
    /**
     * id ,添加注解可以实现id的自动回显,可能需要进行编辑或者查看功能
     */
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
     * 代码
     */
    private String code;

    /**
     * 判题状态(0 - 待判题，1-判题中 ，2- 判题成功 ，3-判题失败)
     */
    private Integer status;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 判题信息（json）
     */
    private JudgeInfo judgeInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    //添加userVO
    private UserVO userVO;

    //添加questionVO
    private QuestionVO questionVO;

    //对比两个类，将包装类型转换成对象
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        //先拷贝
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        //对judgeConfig进行序列化
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        //先拷贝
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        //转换为对象类型
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class));
        return questionSubmitVO;
    }

}
