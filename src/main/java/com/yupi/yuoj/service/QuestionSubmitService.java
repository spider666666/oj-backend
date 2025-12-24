package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.dto.question.QuestionQueryRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionSubmitVO;
import com.yupi.yuoj.model.vo.QuestionVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-12-15 02:05:21
*/
@Service
public interface QuestionSubmitService extends IService<QuestionSubmit> {


    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    //条件构造器
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    //返回前端想要的值
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    //这里不是真正的分页查询，是根据mybatisPlus的分页查询方法得到的结果进行脱敏（过滤信息）
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}

