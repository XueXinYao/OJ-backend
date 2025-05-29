package com.yupi.yuoj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    private List<String> output;
    /**
     * 接口信息
     * */
    private String message;
    /**
     * 执行状态
     * */
    private String status;
    /**
     * 执行信息
     * */
    private JudgeInfo  judgeInfo;
     
}
