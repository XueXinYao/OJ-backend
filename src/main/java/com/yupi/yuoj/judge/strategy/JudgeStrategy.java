package com.yupi.yuoj.judge.strategy;

import com.yupi.yuoj.judge.codesandbox.model.JudgeInfo;


/**
 * 判题策略
 * */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
