package com.yupi.yuoj.judge.codesandbox;

import com.yupi.yuoj.judge.codesandbox.impl.ExampleSandboxImpl;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yuoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.yuoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CodeSandboxTest {


   @Value("${codesandbox.type:example}")
    private String type;
    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new ExampleSandboxImpl();
        String code="int main(){}";
        String language= QuestionSubmitLanguageEnum.Java.getValue();
        List<String> inputList = List.of("1 2", "1 3");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
    @Test
    void executeCodeByValue() {
        System.out.println(type);
        CodeSandbox codeSandbox = CodeSandBoxFactory.newInstance(type);
        System.out.println(codeSandbox);
        String code="int main(){}";
        String language= QuestionSubmitLanguageEnum.Java.getValue();
        List<String> inputList = List.of("1 2", "1 3");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
    @Test
    void executeCodeByProxy() {

        CodeSandbox codeSandbox = CodeSandBoxFactory.newInstance(type);
        codeSandbox = new CodeSandBoxProxy(codeSandbox);
        String code="public class Main{\n" +
                "    public static void main(String[] args) {\n" +
                "        int a =Integer.parseInt(args[0]);\n" +
                "        int b =Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果\"+(a+b));\n" +
                "    }\n" +
                "}\n";
        String language= QuestionSubmitLanguageEnum.Java.getValue();
        List<String> inputList = List.of("1 2", "1 3");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
        Assertions.assertNotNull(executeCodeResponse);
    }
}