package com.yupi.yuoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.judge.codesandbox.CodeSandbox;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 * */

public class RemoteSandboxImpl implements CodeSandbox {

    //定义请求头和密钥
    private  static  final String AUTH_REQUEST_HEADER="auth";

    private  static  final  String AUTH_REQUEST_SECRET="secretKey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程沙箱");
        String url =  "http://192.168.130.130:8090/executeCode";
        String json= JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();

        if (StringUtils.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_RESULT_ERROR,"execute remoteSandBox error msssage="+responseStr);
        }
        return JSONUtil.toBean(responseStr,ExecuteCodeResponse.class);
    }
}
