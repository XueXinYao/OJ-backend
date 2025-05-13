package com.yupi.yuoj.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 */

@Data
public class JudgeConfig {



    /**
     * 时间限制 （ms)
     */
    private Long timeLimit;

    /**
     * 内存限制 （MB)
     */
    private Long memoryLimit;

    /**
     * 堆栈限制
     * */
    private Long stackLimit;

}
