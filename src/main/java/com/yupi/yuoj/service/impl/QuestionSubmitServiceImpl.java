package com.yupi.yuoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.constant.CommonConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.job.codesand.service.JudgeService;
import com.yupi.yuoj.mapper.QuestionSubmitMapper;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.enums.LanguageEnum;
import com.yupi.yuoj.model.enums.StatusEnum;
import com.yupi.yuoj.model.vo.QuestionSubmitVO;
import com.yupi.yuoj.service.QuestionService;
import com.yupi.yuoj.service.QuestionSubmitService;
import com.yupi.yuoj.service.UserService;
import com.yupi.yuoj.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2025-12-15 02:05:21
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;


    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest,User loginUser) {
        //校验编程语言
        String language = questionSubmitAddRequest.getLanguage();
        LanguageEnum languageEnum = LanguageEnum.getEnumByValue(language);
        if(languageEnum == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"编程语言错误");
        }

        // 判断实体是否存在，根据类别获取实体
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 获取当前用户登入的id
        long userId = loginUser.getId();
        // 每个用户串行提交todo后期可能会加锁或者进行限流处理，防止用户多次提交或者恶意提交
        // 根据传入的请求参数封装需要保存的信息
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        // 对于一些其他字段设置初始值，在后续判题的方法可能会对字段进行变化
        questionSubmit.setStatus(0);
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }

        //todo 添加判题逻辑
        QuestionSubmit questionSubmit1 = judgeService.doJudge(questionSubmit.getId());
        System.out.println("代码沙箱判题结果为:" + questionSubmit1.getJudgeInfo());
        //具体返回值，一般对于插入数据而言，最好返回当前记录的id
        return questionSubmit.getId();
    }

    // 条件构造器（其实返回的是分页的数据）todo,这里根据条件查询的业务可能会在之后进行适当的修改
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String language = questionSubmitQueryRequest.getLanguage();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        if(questionId != null){
            queryWrapper.eq("questionId",questionId);
        }
        if(userId != null){
            queryWrapper.eq("userId",userId);
        }
        //查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(StatusEnum.getEnumByValue(status) != null, "status", status);
        //进行排序（这里是questionSubmit对page做了集成，里面都是一些和排序，分页相关的内容）
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    // 获取vo的列表，既然是列表，应该是多个数据才对，缩入传入的参数应该是个集合才对。
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);

        //脱敏：仅用户看到本人的记录，管理员看到所有的提交的代码
        long userId = loginUser.getId();

        //不是当前的登入用户，而且不是管理员，没有查看代码的权限
        if(userId != questionSubmit.getUserId() && userService.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        //只要满足一种条件，就放行
        return questionSubmitVO;
    }

    //分页获取信息(脱敏)
    /*
    在这里其实实现了一段稍微复杂的逻辑，就是为了防止因多次建立连接到导致的资源消耗过多的情况，我们使用尽量批次处理信息的方式
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


}




