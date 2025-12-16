package com.yupi.yuoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionSubmitVO;
import com.yupi.yuoj.service.QuestionSubmitService;
import com.yupi.yuoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交 / 取消提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次提交变化数
     */
    @PostMapping("/doSumit")
    public BaseResponse<Long> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交，其实获取用户登入信息的操作可以在service层进行
        final User loginUser = userService.getLoginUser(request);
        // 调用方法进行题目的提交
        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取管理员或者个人的检查记录(分页查询)
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/listQuestionSubmitVO")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        // 根据查询请求获取分页参数进行方法的调用
        int pageSize = questionSubmitQueryRequest.getPageSize();
        int current = questionSubmitQueryRequest.getCurrent();
        Page<QuestionSubmit> page = questionSubmitService.page(new Page<>(current, pageSize), questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));

        //获取用户信息
        User loginUser = userService.getLoginUser(request);
        //对获取到的数据进行脱敏
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(page,loginUser));
    }
}
