package com.yupi.yuoj.judge.codesandbox.impl;

import com.yupi.yuoj.judge.codesandbox.CodeSandbox;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Service;

/**
 * 第三方代码沙箱（调用网上现成的沙箱）
 * */

public class ThirdPartySandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方沙箱");
        return null;
    }
}
