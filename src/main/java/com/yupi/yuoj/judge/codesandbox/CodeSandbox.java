package com.yupi.yuoj.judge.codesandbox;


/**
 * 代码沙箱接口，提高通用性，只调接口不调用具体的实现类
 */

import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Service;


public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);}
