package com.yupi.yuoj.judge.codesandbox;


import com.yupi.yuoj.judge.codesandbox.impl.ExampleSandboxImpl;
import com.yupi.yuoj.judge.codesandbox.impl.RemoteSandboxImpl;
import com.yupi.yuoj.judge.codesandbox.impl.ThirdPartySandboxImpl;
import org.springframework.stereotype.Component;

/**
 * 静态代码沙箱工厂（根据字符串来选择对应的代码沙箱)
 * */
@Component
public class CodeSandBoxFactory {
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteSandboxImpl();
            case "thirdParty":
                return new ThirdPartySandboxImpl();
            case "example":
                return new ExampleSandboxImpl();
            default:
                return new ExampleSandboxImpl();
        }
    }
}
