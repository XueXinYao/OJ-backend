package com.yupi.yuoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yuoj.annotation.AuthCheck;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.constant.UserConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.question.QuestionQueryRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.yuoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.yuoj.model.entity.Question;
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
@RequestMapping("/quesiton_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService quesitonSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param quesitonSubmitAddRequest
     * @param request
     * @return 提交记录id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest quesitonSubmitAddRequest,
            HttpServletRequest request) {
        if (quesitonSubmitAddRequest == null || quesitonSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        final User loginUser = userService.getLoginUser(request);

        Long questionSubmitId = quesitonSubmitService.doQuestionSubmit(quesitonSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
    * 分页获取题目提交列表（除了管理员外，普通用户只能看到答案，提交代码等公开信息）
     *
     * */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据库中查询原始的题目信息
        Page<QuestionSubmit> questionSubmitPage = quesitonSubmitService.page(new Page<>(current, size),
                quesitonSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        //脱敏
        return ResultUtils.success(quesitonSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));

 }
}
