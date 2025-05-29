package com.yupi.yuoj.judge;

import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.yuoj.model.entity.QuestionSubmit;
import com.yupi.yuoj.model.vo.QuestionSubmitVO;



public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
