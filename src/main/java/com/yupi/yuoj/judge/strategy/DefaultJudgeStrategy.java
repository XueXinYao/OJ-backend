package com.yupi.yuoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.model.dto.question.JudgeCase;
import com.yupi.yuoj.model.dto.question.JudgeConfig;
import com.yupi.yuoj.judge.codesandbox.model.JudgeInfo;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;


/**
 * 默认判题策略
 * */
public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.Accept;
        JudgeInfo judgeInfoRes = new JudgeInfo();
        judgeInfoRes.setMemory(memory);
        judgeInfoRes.setTime(time);

        //首先判断沙箱执行结果的输出是否和预期输出相等
        if (outputList.size()!=inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.Wrong_Answer;
            judgeInfoRes.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRes;
        }

        //依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++){   //judgeCase就是题目的输入输出用例，outputList是沙箱的运行结果
            JudgeCase judgeCase = judgeCaseList.get(i);
            if(!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.Wrong_Answer;
                judgeInfoRes.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoRes;
            }
        }
        //判断限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needmemoryLimit = judgeConfig.getMemoryLimit();
        Long needtimeLimit = judgeConfig.getTimeLimit();
        if (memory > needmemoryLimit ){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.Memory_Limit_Exceeded;
            judgeInfoRes.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRes;
        }
        if (time > needtimeLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.Time_Limit_Exceeded;
            judgeInfoRes.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoRes;
        }
        judgeInfoRes.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoRes;
    }
}
