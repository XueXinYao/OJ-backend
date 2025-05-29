package com.yupi.yuoj.judge;

import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.judge.codesandbox.CodeSandBoxFactory;
import com.yupi.yuoj.judge.codesandbox.CodeSandBoxProxy;
import com.yupi.yuoj.judge.codesandbox.CodeSandbox;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.yuoj.judge.strategy.DefaultJudgeStrategy;
import com.yupi.yuoj.judge.strategy.JudgeContext;
import com.yupi.yuoj.judge.strategy.JudgeStrategy;
import com.yupi.yuoj.model.dto.question.JudgeCase;
import com.yupi.yuoj.judge.codesandbox.model.JudgeInfo;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.yuoj.service.QuestionService;
import com.yupi.yuoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JudgeServiceImpl implements JudgeService {
    /**
     * 判题服务业务流程：
     * 1）传入题目的提交id，获取到对应的题目、提交信息（包含代码、编程语言等）
     * 2)调用沙箱，获取到执行结果
     * 3）根据沙箱的执行结果，设置题目的判题状态和信息
     * */


    @Resource
    private QuestionService questionService;
//    @Resource
//    private CodeSandbox codeSandbox;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {


        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交不存在");
        }

        Question question = questionService.getById(questionSubmit.getQuestionId());
        if (question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }

        //如果不是等待状态，则不能执行判题
       if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WATTING.getValue())){
           throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
       }
        //更新状态
       QuestionSubmit quetionSubmitUpdate = new QuestionSubmit();
       quetionSubmitUpdate.setId(questionSubmitId);
       quetionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
       boolean update = questionSubmitService.updateById(quetionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目更新错误");
        }

        // 调用沙箱
        CodeSandbox codeSandbox = CodeSandBoxFactory.newInstance(type);
        //使用代理的增强方法
        codeSandbox = new CodeSandBoxProxy(codeSandbox);
        String language= questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList=JSONUtil.toList(judgeCaseStr,JudgeCase.class);

        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);


        //根据沙箱的执行结果 设置题目状态
        List<String> outputList = executeCodeResponse.getOutput();


        JudgeContext  judgeContext =  new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeStrategy  judgeStrategy = new DefaultJudgeStrategy();


        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //修改数据库中的结果
        quetionSubmitUpdate = new QuestionSubmit();
        quetionSubmitUpdate.setId(questionSubmitId);
        quetionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        quetionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(quetionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}
